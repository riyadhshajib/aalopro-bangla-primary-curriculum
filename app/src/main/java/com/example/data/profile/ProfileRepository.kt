package com.example.data.profile

import kotlinx.coroutines.flow.StateFlow

interface ProfileRepository {
    val household: StateFlow<ProfileHousehold>
    fun addChild(child: ChildProfile): Result<ProfileHousehold>
    fun updateChild(child: ChildProfile): Result<ProfileHousehold>
    fun removeChild(childId: String): Result<ProfileHousehold>
    fun selectChild(childId: String): Result<ProfileHousehold>
    fun updateParent(parent: ParentProfile): ProfileHousehold
}

interface ProfileSyncGateway {
    suspend fun push(userId: String, payload: ProfileSyncPayload): Result<Unit>
}

class NoOpProfileSyncGateway : ProfileSyncGateway {
    override suspend fun push(userId: String, payload: ProfileSyncPayload): Result<Unit> = Result.success(Unit)
}
