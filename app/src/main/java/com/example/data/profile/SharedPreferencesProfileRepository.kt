package com.example.data.profile

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

class SharedPreferencesProfileRepository(
    context: Context,
    private val idFactory: () -> String = { UUID.randomUUID().toString() }
) : ProfileRepository {
    private val prefs: SharedPreferences = context.applicationContext
        .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val mutableHousehold = MutableStateFlow(loadOrMigrate())
    override val household: StateFlow<ProfileHousehold> = mutableHousehold.asStateFlow()

    override fun addChild(child: ChildProfile): Result<ProfileHousehold> {
        val normalized = ProfileValidator.normalizeChild(child.copy(id = child.id.ifBlank(idFactory)))
        val validation = ProfileValidator.validateChild(normalized)
        if (validation is ProfileValidation.Invalid) return Result.failure(IllegalArgumentException(validation.reason))
        if (mutableHousehold.value.children.any { it.id == normalized.id }) return Result.failure(IllegalArgumentException("duplicate_child_id"))
        return commit(mutableHousehold.value.copy(
            children = mutableHousehold.value.children + normalized,
            activeChildId = mutableHousehold.value.activeChildId ?: normalized.id
        ))
    }

    override fun updateChild(child: ChildProfile): Result<ProfileHousehold> {
        val normalized = ProfileValidator.normalizeChild(child)
        val validation = ProfileValidator.validateChild(normalized)
        if (validation is ProfileValidation.Invalid) return Result.failure(IllegalArgumentException(validation.reason))
        if (mutableHousehold.value.children.none { it.id == child.id }) return Result.failure(NoSuchElementException("child_not_found"))
        return commit(mutableHousehold.value.copy(children = mutableHousehold.value.children.map { if (it.id == child.id) normalized else it }))
    }

    override fun removeChild(childId: String): Result<ProfileHousehold> {
        val current = mutableHousehold.value
        if (current.children.none { it.id == childId }) return Result.failure(NoSuchElementException("child_not_found"))
        val remaining = current.children.filterNot { it.id == childId }
        return commit(current.copy(children = remaining, activeChildId =
            if (current.activeChildId == childId) remaining.firstOrNull()?.id else current.activeChildId))
    }

    override fun selectChild(childId: String): Result<ProfileHousehold> {
        if (mutableHousehold.value.children.none { it.id == childId }) return Result.failure(NoSuchElementException("child_not_found"))
        return commit(mutableHousehold.value.copy(activeChildId = childId))
    }

    override fun updateParent(parent: ParentProfile): ProfileHousehold {
        val updated = mutableHousehold.value.copy(parent = mutableHousehold.value.parent.copy(
            displayName = parent.displayName.trim().take(40),
            guardianPhone = parent.guardianPhone.trim().take(20),
            phoneVerified = parent.phoneVerified,
            consentVersion = parent.consentVersion,
            consentGivenAtEpochMs = parent.consentGivenAtEpochMs,
        ))
        return commit(updated).getOrThrow()
    }

    private fun commit(value: ProfileHousehold): Result<ProfileHousehold> = runCatching {
        check(prefs.edit().putString(KEY_HOUSEHOLD, encode(value)).commit())
        mutableHousehold.value = value
        value
    }

    private fun loadOrMigrate(): ProfileHousehold {
        prefs.getString(KEY_HOUSEHOLD, null)?.let { encoded ->
            runCatching { decode(encoded) }.getOrNull()?.let { return repair(it) }
        }
        val legacyName = prefs.getString("name", "").orEmpty().trim().take(40)
        val legacyPhone = prefs.getString("guardian_phone", "").orEmpty().trim().take(20)
        val child = legacyName.takeIf { it.isNotBlank() }?.let {
            ChildProfile(id = "legacy-child", displayName = it)
        }
        return ProfileHousehold(
            parent = ParentProfile(id = "local-parent", guardianPhone = legacyPhone),
            children = listOfNotNull(child), activeChildId = child?.id
        ).also { prefs.edit().putString(KEY_HOUSEHOLD, encode(it)).commit() }
    }

    private fun repair(value: ProfileHousehold): ProfileHousehold {
        val valid = value.children.map(ProfileValidator::normalizeChild).filter { ProfileValidator.validateChild(it) == ProfileValidation.Valid }
            .distinctBy { it.id }
        val repaired = value.copy(schemaVersion = ProfileHousehold.CURRENT_SCHEMA_VERSION, children = valid,
            activeChildId = value.activeChildId?.takeIf { id -> valid.any { it.id == id } } ?: valid.firstOrNull()?.id)
        if (repaired != value) prefs.edit().putString(KEY_HOUSEHOLD, encode(repaired)).commit()
        return repaired
    }

    private fun encode(value: ProfileHousehold): String = JSONObject().apply {
        put("schemaVersion", value.schemaVersion)
        put("parentId", value.parent.id)
        put("parentName", value.parent.displayName)
        put("guardianPhone", value.parent.guardianPhone)
        put("phoneVerified", value.parent.phoneVerified)
        put("consentVersion", value.parent.consentVersion)
        put("consentGivenAtEpochMs", value.parent.consentGivenAtEpochMs)
        put("activeChildId", value.activeChildId ?: JSONObject.NULL)
        put("children", JSONArray().apply { value.children.forEach { child -> put(JSONObject().apply {
            put("id", child.id); put("displayName", child.displayName); put("classNumber", child.classNumber)
            put("avatarId", child.avatarId); put("language", child.language.storageValue)
        }) } })
    }.toString()

    private fun decode(encoded: String): ProfileHousehold {
        val root = JSONObject(encoded)
        val array = root.optJSONArray("children") ?: JSONArray()
        val children = buildList { for (index in 0 until array.length()) array.getJSONObject(index).let { item -> add(ChildProfile(
            id = item.getString("id"), displayName = item.getString("displayName"), classNumber = item.optInt("classNumber", 1),
            avatarId = item.optString("avatarId", ChildProfile.DEFAULT_AVATAR_ID), language = ProfileLanguage.fromStorage(item.optString("language"))
        )) } }
        return ProfileHousehold(root.optInt("schemaVersion", 1), ParentProfile(
            id = root.optString("parentId", "local-parent"),
            displayName = root.optString("parentName"),
            guardianPhone = root.optString("guardianPhone"),
            phoneVerified = root.optBoolean("phoneVerified", false),
            consentVersion = root.optString("consentVersion", CURRENT_CONSENT_VERSION),
            consentGivenAtEpochMs = root.optLong("consentGivenAtEpochMs", 0L),
        ),
            children, root.optString("activeChildId").takeIf { it.isNotBlank() && it != "null" })
    }

    companion object {
        const val PREFS_NAME = "aalo_profile"
        private const val KEY_HOUSEHOLD = "profile_household_v2"
    }
}
