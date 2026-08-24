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

    /**
     * Document data model.
     */
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

    /**
     * Supported document categories.
     */
    enum class DocumentType {
        AADHAAR,
        PAN,
        PHOTO,
        OTHER,
        RECEIPT,
        TICKET
    }

    /**
     * Vault configuration.
     */
    data class VaultConfig(
        val encryptionEnabled: Boolean = true,
        val maxDocuments: Int = 100,
        val storagePath: String? = null,
        val allowedTypes: Set<DocumentType> = DocumentType.entries.toSet()
    )

    /**
     * Store a document.
     */
    fun storeDocument(
        document: Document
    ): Result<Unit, VaultError>

    /**
     * Retrieve a document.
     */
    fun retrieveDocument(
        id: String
    ): Result<Document, VaultError>

    /**
     * Delete a document.
     */
    fun deleteDocument(
        id: String
    ): Result<Unit, VaultError>

    /**
     * List documents.
     */
    fun listDocuments(
        type: DocumentType? = null
    ): Result<List<Document>, VaultError>

    /**
     * Check whether a document exists.
     */
    fun containsDocument(
        id: String
    ): Boolean

    /**
     * Return current document count.
     */
    fun getDocumentCount(): Int

    /**
     * Clear stored documents.
     */
    fun clearAll(): Result<Unit, VaultError>

    /**
     * Return current configuration.
     */
    fun getConfig(): VaultConfig

    /**
     * Update configuration.
     */
    fun updateConfig(
        config: VaultConfig
    )
}

/**
 * Document Vault error model.
 */
data class VaultError(
    val code: String,
    val message: String,
    val documentId: String? = null,
    val documentType: DocumentVault.DocumentType? = null
)
