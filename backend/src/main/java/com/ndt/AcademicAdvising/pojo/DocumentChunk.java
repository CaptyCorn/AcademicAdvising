/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndt.AcademicAdvising.pojo;

import com.pgvector.PGvector;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Map;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/**
 *
 * @author ngodo
 */
@Entity
@Table(name = "tbl_document_chunk")
public class DocumentChunk {
    @Id
    @Column(name = "embedding_id")
    private UUID embeddingId;
    @Column(columnDefinition = "TEXT")
    private String text;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> metadata;
    @Column(columnDefinition = "vector(1536)")
    private PGvector embedding;

    public DocumentChunk() {
    }  

    public DocumentChunk(UUID embeddingId, String text, Map<String, Object> metadata, PGvector embedding) {
        this.embeddingId = embeddingId;
        this.text = text;
        this.metadata = metadata;
        this.embedding = embedding;
    }

    /**
     * @return the embeddingId
     */
    public UUID getEmbeddingId() {
        return embeddingId;
    }

    /**
     * @param embeddingId the embeddingId to set
     */
    public void setEmbeddingId(UUID embeddingId) {
        this.embeddingId = embeddingId;
    }

    /**
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * @param text the text to set
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * @return the metadata
     */
    public Map<String, Object> getMetadata() {
        return metadata;
    }

    /**
     * @param metadata the metadata to set
     */
    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    /**
     * @return the embedding
     */
    public PGvector getEmbedding() {
        return embedding;
    }

    /**
     * @param embedding the embedding to set
     */
    public void setEmbedding(PGvector embedding) {
        this.embedding = embedding;
    }

    
    
}
