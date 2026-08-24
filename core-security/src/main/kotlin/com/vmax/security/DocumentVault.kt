package com.vmax.security

import com.vmax.common.Result

/**
 * VMAX Enterprise v2.6.1
 *
 * File 17 — DocumentVault
 *
 * Contract for secure document storage.
 * Platform-independent, no Android dependencies.
 */
interface DocumentVault {

    data class Document(
        val id: String,
        val type: DocumentType,
        val name: String,
        val data: String,
        val metadata: Map<String, String> = emptyMap(),
        val size: Long = data.toByteArray().size.toLong(),
        val mimeType: String? = null,
        val createdAt: Long = System.currentTimeMillis(),
        val updatedAt: Long = createdAt
    )

    enum class DocumentType {
        AADHAAR,
        PAN,
        PHOTO,
        OTHER,
        RECEIPT,
        TICKET
    }

    data class VaultConfig(
        val encryptionEnabled: Boolean = true,
        val maxDocuments: Int = 100,
        val storagePath: String? = null,
        val allowedTypes: Set<DocumentType> = DocumentType.entries.toSet()
    )

    fun storeDocument(document: Document): Result<Unit, VaultError>

    fun retrieveDocument(id: String): Result<Document, VaultError>

    fun deleteDocument(id: String): Result<Unit, VaultError>

    fun listDocuments(
        type: DocumentType? = null
    ): Result<List<Document>, VaultError>

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
