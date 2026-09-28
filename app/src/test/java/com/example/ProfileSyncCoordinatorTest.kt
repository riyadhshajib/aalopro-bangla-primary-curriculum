package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.profile.ChildProfile
import com.example.data.profile.ParentProfile
import com.example.data.profile.ProfileHousehold
import com.example.data.profile.ProfileSyncCoordinator
import com.example.data.profile.ProfileSyncGateway
import com.example.data.profile.ProfileSyncPayload
import com.example.data.profile.ProfileSyncState
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
class ProfileSyncCoordinatorTest {
    private lateinit var context: Context

    @Before
    fun resetPreferences() {
        context = ApplicationProvider.getApplicationContext()
        context.getSharedPreferences("aalo_profile", Context.MODE_PRIVATE).edit().clear().commit()
    }

    @Test
    fun `failed sync persists retry state across recreation`() = runTest {
        val failed = ProfileSyncCoordinator(context, FakeGateway(Result.failure(IllegalStateException("offline"))))
        assertTrue(failed.sync("user", household()).isFailure)
        assertTrue(failed.state.value is ProfileSyncState.RetryPending)

        val restored = ProfileSyncCoordinator(context, FakeGateway(Result.success(Unit)))
        assertTrue(restored.state.value is ProfileSyncState.RetryPending)
    }

    @Test
    fun `successful retry clears persisted pending state`() = runTest {
        val coordinator = ProfileSyncCoordinator(context, FakeGateway(Result.success(Unit)))
        coordinator.defer("offline")
        assertTrue(coordinator.sync("user", household()).isSuccess)
        assertEquals(ProfileSyncState.Synced, coordinator.state.value)

        val restored = ProfileSyncCoordinator(context, FakeGateway(Result.success(Unit)))
        assertEquals(ProfileSyncState.Idle, restored.state.value)
    }

    private fun household() = ProfileHousehold(
        parent = ParentProfile("local-parent", "Parent", "01700000000"),
        children = listOf(ChildProfile("child", "Child", 2, "learner_2")),
        activeChildId = "child"
    )

    private class FakeGateway(private val result: Result<Unit>) : ProfileSyncGateway {
        override suspend fun push(userId: String, payload: ProfileSyncPayload): Result<Unit> = result
    }
}
