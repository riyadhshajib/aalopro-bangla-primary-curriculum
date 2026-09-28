package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.profile.ChildProfile
import com.example.data.profile.ParentProfile
import com.example.data.profile.ProfileHousehold
import com.example.data.profile.ProfileLanguage
import com.example.data.profile.SharedPreferencesProfileRepository
import com.example.data.profile.toSyncPayload
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
class ProfileAcceptanceRegressionTest {
    private lateinit var context: Context

    @Before
    fun clearProfilePreferences() {
        context = ApplicationProvider.getApplicationContext()
        context.getSharedPreferences(SharedPreferencesProfileRepository.PREFS_NAME, Context.MODE_PRIVATE)
            .edit().clear().commit()
    }

    @Test
    fun `corrupt household falls back to legacy profile without erasing learning keys`() {
        val prefs = context.getSharedPreferences(SharedPreferencesProfileRepository.PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putString("profile_household_v2", "{not-json")
            .putString("name", " পুরোনো শিশু ")
            .putString("guardian_phone", "01700000000")
            .putStringSet("completed_topics", setOf("nctb2026_c1_math_1"))
            .commit()

        val restored = SharedPreferencesProfileRepository(context).household.value

        assertEquals("পুরোনো শিশু", restored.activeChild?.displayName)
        assertEquals("01700000000", restored.parent.guardianPhone)
        assertEquals(setOf("nctb2026_c1_math_1"), prefs.getStringSet("completed_topics", emptySet()))
    }

    @Test
    fun `cloud payload includes consented guardian details but excludes internal parent id`() {
        val household = ProfileHousehold(
            parent = ParentProfile("guardian-1", "অভিভাবক", "01700000000"),
            children = listOf(ChildProfile("child-1", "শিশু", 3, "learner_1", ProfileLanguage.BANGLA)),
            activeChildId = "child-1",
        )

        val payload = household.toSyncPayload().toString()

        assertTrue(payload.contains("অভিভাবক"))
        assertTrue(payload.contains("01700000000"))
        assertFalse(payload.contains("guardian-1"))
        assertTrue(payload.contains("শিশু"))
    }

    @Test
    fun `removing the only child leaves a valid empty household ready for onboarding`() {
        val repository = SharedPreferencesProfileRepository(context) { "only-child" }
        assertTrue(repository.addChild(ChildProfile("", "রিয়া", 1)).isSuccess)

        val result = repository.removeChild("only-child").getOrThrow()

        assertTrue(result.children.isEmpty())
        assertNull(result.activeChildId)
        assertNull(result.activeChild)
        val restored = SharedPreferencesProfileRepository(context).household.value
        assertTrue(restored.children.isEmpty())
        assertNull(restored.activeChildId)
    }
}
