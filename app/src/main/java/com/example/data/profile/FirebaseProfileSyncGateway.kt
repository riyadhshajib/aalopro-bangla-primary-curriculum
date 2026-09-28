package com.example.data.profile

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class FirebaseProfileSyncGateway(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) : ProfileSyncGateway {
    override suspend fun push(userId: String, payload: ProfileSyncPayload): Result<Unit> = suspendCancellableCoroutine { continuation ->
        val userDocument = firestore.collection("users").document(userId)
        val childrenCollection = userDocument.collection("children")
        val parentMetadata = mapOf(
            "profileSchemaVersion" to payload.schemaVersion,
            "guardianName" to payload.guardianName,
            "guardianPhone" to payload.guardianPhone,
            "phoneVerified" to payload.phoneVerified,
            "consentGiven" to true,
            "consentVersion" to payload.consentVersion,
            "consentGivenAtEpochMs" to payload.consentGivenAtEpochMs,
            "activeChildId" to payload.activeChildId,
            "childIds" to payload.children.map { it.id },
            "updatedAt" to FieldValue.serverTimestamp()
        )

        // Read only child document ids so one atomic batch can also remove profiles
        // deleted on this device. Guardian data is owner-scoped and never sent to Analytics.
        childrenCollection.get()
            .addOnSuccessListener { snapshot ->
                if (!continuation.isActive) return@addOnSuccessListener
                val currentIds = payload.children.mapTo(linkedSetOf()) { it.id }
                val batch = firestore.batch()
                batch.set(userDocument, parentMetadata, SetOptions.merge())
                payload.children.forEach { child ->
                    batch.set(childrenCollection.document(child.id), mapOf(
                        "id" to child.id,
                        "displayName" to child.displayName,
                        "classNumber" to child.classNumber,
                        "avatarId" to child.avatarId,
                        "language" to child.language,
                        "updatedAt" to FieldValue.serverTimestamp()
                    ))
                }
                snapshot.documents.filterNot { it.id in currentIds }.forEach { batch.delete(it.reference) }
                batch.commit()
                    .addOnSuccessListener { if (continuation.isActive) continuation.resume(Result.success(Unit)) }
                    .addOnFailureListener { error -> if (continuation.isActive) continuation.resume(Result.failure(error)) }
            }
            .addOnFailureListener { error ->
                if (continuation.isActive) continuation.resume(Result.failure(error))
            }
    }
}
