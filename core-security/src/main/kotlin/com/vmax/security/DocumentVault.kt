package com.vmax.security

import com.vmax.common.Result

/**
 * VMAX Enterprise v2.6
 *
 * Stage 1 — Skeleton
 * File 17 — DocumentVault
 *
 * Secure storage for sensitive documents like Aadhaar, PAN, Photo.
 * Platform-independent — no Android dependencies.
 * No external dependencies.
 * No business logic.
 */
interface DocumentVault {

    data class Document(
        val id: String,
        val type: DocumentType,
        val name: String,
        val data: String, // Encrypted data
        val metadata: Map<String, String> = emptyMap(),
        val timestamp: Long = System.currentTimeMillis()
    )

    enum class DocumentType {
        AADHAAR,
        PAN,
        PHOTO,
        OTHER
    }

    data class VaultConfig(
        val encryptionEnabled: Boolean = true,
        val maxDocuments: Int = 50,
        val storagePath: String? = null
    )

    fun storeDocument(document: Document): Result<Unit, VaultError>

    fun retrieveDocument(id: String): Result<Document, VaultError>

    fun deleteDocument(id: String): Result<Unit, VaultError>

    fun listDocuments(type: DocumentType? = null): Result<List<Document>, VaultError>

    fun containsDocument(id: String): Boolean

    fun getDocumentCount(): Int

    fun clearAll(): Result<Unit, VaultError>

    fun getConfig(): VaultConfig

    fun updateConfig(config: VaultConfig)
}

data class VaultError(
    val code: String,
    val message: String,
    val documentId: String? = null,
    val documentType: DocumentVault.DocumentType? = null
)
