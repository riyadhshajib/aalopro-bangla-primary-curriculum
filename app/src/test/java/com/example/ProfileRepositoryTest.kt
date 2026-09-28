package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.profile.ChildProfile
import com.example.data.profile.ProfileLanguage
import com.example.data.profile.ParentProfile
import com.example.data.profile.ProfileValidation
import com.example.data.profile.ProfileValidator
import com.example.data.profile.SharedPreferencesProfileRepository
import com.example.data.profile.toSyncPayload
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
class ProfileRepositoryTest {
    private lateinit var context: Context

    @Before
    fun resetPreferences() {
        context = ApplicationProvider.getApplicationContext()
        context.getSharedPreferences(SharedPreferencesProfileRepository.PREFS_NAME, Context.MODE_PRIVATE).edit().clear().commit()
    }

    @Test
    fun `legacy profile migrates without deleting existing keys`() {
        val prefs = context.getSharedPreferences(SharedPreferencesProfileRepository.PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString("name", " রিয়া ").putString("guardian_phone", "01700000000")
            .putStringSet("completed_topics", setOf("c1_math_1")).commit()

        val household = SharedPreferencesProfileRepository(context).household.value

        assertEquals("রিয়া", household.activeChild?.displayName)
        assertEquals(1, household.activeChild?.classNumber)
        assertEquals("01700000000", household.parent.guardianPhone)
        assertEquals(setOf("c1_math_1"), prefs.getStringSet("completed_topics", emptySet()))
    }

    @Test
    fun `multiple children persist and active child survives recreation`() {
        val ids = ArrayDeque(listOf("child-one", "child-two"))
        val repository = SharedPreferencesProfileRepository(context) { ids.removeFirst() }
        assertTrue(repository.addChild(ChildProfile("", "Asha", 2, "learner_2", ProfileLanguage.ENGLISH)).isSuccess)
        assertTrue(repository.addChild(ChildProfile("", "আলো", 4, "learner_3", ProfileLanguage.BANGLISH)).isSuccess)
        assertTrue(repository.selectChild("child-two").isSuccess)

        val restored = SharedPreferencesProfileRepository(context).household.value
        assertEquals(2, restored.children.size)
        assertEquals("child-two", restored.activeChildId)
        assertEquals(ProfileLanguage.BANGLISH, restored.activeChild?.language)
        assertEquals(4, restored.activeChild?.classNumber)
    }

    @Test
    fun `invalid child is rejected and normalization is bounded`() {
        assertTrue(ProfileValidator.validateChild(ChildProfile("id", "A", 1)) is ProfileValidation.Invalid)
        val repository = SharedPreferencesProfileRepository(context) { "bounded" }
        val result = repository.addChild(ChildProfile("", "  শেখার্থী  ", 9, "INVALID AVATAR"))
        assertTrue(result.isSuccess)
        val child = result.getOrThrow().activeChild!!
        assertEquals("শেখার্থী", child.displayName)
        assertEquals(5, child.classNumber)
        assertEquals(ChildProfile.DEFAULT_AVATAR_ID, child.avatarId)
    }

    @Test
    fun `sync payload includes unverified guardian phone and consent metadata`() {
        val repository = SharedPreferencesProfileRepository(context) { "child" }
        repository.updateParent(
            ParentProfile(
                id = "parent",
                displayName = "Parent",
                guardianPhone = "01700000000",
                phoneVerified = false,
                consentVersion = "profile-v1",
                consentGivenAtEpochMs = 1234L,
            )
        )
        repository.addChild(ChildProfile("", "Child", 3))
        val payload = repository.household.value.toSyncPayload()
        assertEquals("Parent", payload.guardianName)
        assertEquals("01700000000", payload.guardianPhone)
        assertFalse(payload.phoneVerified)
        assertEquals("profile-v1", payload.consentVersion)
        assertEquals(1234L, payload.consentGivenAtEpochMs)
        assertTrue(payload.children.any { it.displayName == "Child" })
    }

    @Test
    fun `removing active child selects remaining child`() {
        val ids = ArrayDeque(listOf("one", "two"))
        val repository = SharedPreferencesProfileRepository(context) { ids.removeFirst() }
        repository.addChild(ChildProfile("", "First", 1))
        repository.addChild(ChildProfile("", "Second", 2))
        repository.selectChild("one")
        val result = repository.removeChild("one")
        assertTrue(result.isSuccess)
        assertEquals("two", result.getOrThrow().activeChildId)
    }
}
