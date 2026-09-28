package com.example.data.profile

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

sealed interface ProfileSyncState {
    data object Idle : ProfileSyncState
    data object Syncing : ProfileSyncState
    data object Synced : ProfileSyncState
    data class RetryPending(val reason: String) : ProfileSyncState
}

/** Offline-first coordinator. Local save succeeds independently; failed cloud sync remains retryable. */
class ProfileSyncCoordinator(
    context: Context,
    private val gateway: ProfileSyncGateway
) {
    private val prefs = context.applicationContext.getSharedPreferences(SharedPreferencesProfileRepository.PREFS_NAME, Context.MODE_PRIVATE)
    private val mutableState = MutableStateFlow<ProfileSyncState>(
        if (prefs.getBoolean(KEY_PENDING, false)) ProfileSyncState.RetryPending("restored_pending_sync") else ProfileSyncState.Idle
    )
    val state: StateFlow<ProfileSyncState> = mutableState.asStateFlow()

    fun defer(reason: String) {
        prefs.edit().putBoolean(KEY_PENDING, true).apply()
        mutableState.value = ProfileSyncState.RetryPending(reason)
    }

    suspend fun sync(userId: String, household: ProfileHousehold): Result<Unit> {
        if (userId.isBlank()) return Result.failure(IllegalArgumentException("blank_user_id"))
        mutableState.value = ProfileSyncState.Syncing
        val result = gateway.push(userId, household.toSyncPayload())
        if (result.isSuccess) {
            prefs.edit().putBoolean(KEY_PENDING, false).apply()
            mutableState.value = ProfileSyncState.Synced
        } else {
            defer(result.exceptionOrNull()?.javaClass?.simpleName ?: "sync_failed")
        }
        return result
    }

    companion object { private const val KEY_PENDING = "profile_sync_pending_v2" }
}
