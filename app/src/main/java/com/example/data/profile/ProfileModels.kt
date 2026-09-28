package com.example.data.profile

enum class ProfileLanguage(val storageValue: String) {
    BANGLA("bn"), ENGLISH("en"), BANGLISH("bn_latn");

    companion object {
        fun fromStorage(value: String?): ProfileLanguage =
            entries.firstOrNull { it.storageValue == value } ?: BANGLA
    }
}

data class ParentProfile(
    val id: String,
    val displayName: String = "",
    val guardianPhone: String = "",
    val phoneVerified: Boolean = false,
    val consentVersion: String = CURRENT_CONSENT_VERSION,
    val consentGivenAtEpochMs: Long = 0L,
)

const val CURRENT_CONSENT_VERSION = "profile-v1"

data class ChildProfile(
    val id: String,
    val displayName: String,
    val classNumber: Int = 1,
    val avatarId: String = DEFAULT_AVATAR_ID,
    val language: ProfileLanguage = ProfileLanguage.BANGLA
) {
    companion object {
        const val DEFAULT_AVATAR_ID = "learner_1"
        val ALLOWED_AVATAR_IDS = setOf("learner_1", "learner_2", "learner_3", "learner_4")
    }
}

data class ProfileHousehold(
    val schemaVersion: Int = CURRENT_SCHEMA_VERSION,
    val parent: ParentProfile,
    val children: List<ChildProfile> = emptyList(),
    val activeChildId: String? = children.firstOrNull()?.id
) {
    val activeChild: ChildProfile?
        get() = children.firstOrNull { it.id == activeChildId } ?: children.firstOrNull()

    companion object { const val CURRENT_SCHEMA_VERSION = 2 }
}

sealed interface ProfileValidation {
    data object Valid : ProfileValidation
    data class Invalid(val reason: String) : ProfileValidation
}

object ProfileValidator {
    fun validateChild(profile: ChildProfile): ProfileValidation = when {
        profile.displayName.trim().length !in 2..40 -> ProfileValidation.Invalid("child_name_length")
        profile.classNumber !in 1..5 -> ProfileValidation.Invalid("class_out_of_range")
        profile.avatarId !in ChildProfile.ALLOWED_AVATAR_IDS -> ProfileValidation.Invalid("invalid_avatar_id")
        else -> ProfileValidation.Valid
    }

    fun normalizeChild(profile: ChildProfile): ChildProfile = profile.copy(
        displayName = profile.displayName.trim().take(40),
        classNumber = profile.classNumber.coerceIn(1, 5),
        avatarId = profile.avatarId.takeIf { it in ChildProfile.ALLOWED_AVATAR_IDS } ?: ChildProfile.DEFAULT_AVATAR_ID
    )
}

/** Minimal cloud shape. Guardian phone and study content are intentionally absent. */
data class ProfileSyncPayload(
    val schemaVersion: Int,
    val guardianName: String,
    val guardianPhone: String,
    val phoneVerified: Boolean,
    val consentVersion: String,
    val consentGivenAtEpochMs: Long,
    val activeChildId: String?,
    val children: List<ChildSyncPayload>
)

data class ChildSyncPayload(
    val id: String,
    val displayName: String,
    val classNumber: Int,
    val avatarId: String,
    val language: String
)

fun ProfileHousehold.toSyncPayload(): ProfileSyncPayload = ProfileSyncPayload(
    schemaVersion = schemaVersion,
    guardianName = parent.displayName,
    guardianPhone = parent.guardianPhone,
    phoneVerified = parent.phoneVerified,
    consentVersion = parent.consentVersion,
    consentGivenAtEpochMs = parent.consentGivenAtEpochMs,
    activeChildId = activeChildId,
    children = children.map {
        ChildSyncPayload(it.id, it.displayName, it.classNumber, it.avatarId, it.language.storageValue)
    }
)
