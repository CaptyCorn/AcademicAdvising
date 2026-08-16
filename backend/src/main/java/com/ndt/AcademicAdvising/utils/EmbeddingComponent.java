/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndt.AcademicAdvising.utils;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.parser.markdown.MarkdownDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentByRegexSplitter;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author ngodo
 */
@Component
public class EmbeddingComponent {
    @Autowired
    private EmbeddingModel embeddingModel;
    
    @Autowired
    private EmbeddingStore embeddingStore;
    
    public void loadDocument() {
        Document document = FileSystemDocumentLoader.loadDocument(
            "C:/Users/ngodo/Desktop/SO-TAY-SINH-VIEN-2023_semantic.md", 
            new MarkdownDocumentParser());
        
        DocumentSplitter markdownSplit = new DocumentByRegexSplitter(
                "(?m)(?=^#{1,6}\\s+)",
                "\n", 
                1200, 
                200, 
                DocumentSplitters.recursive(1200, 200)
        );
        
        List<TextSegment> segments = markdownSplit.split(document);
        List<Embedding> embeddings = embeddingModel.embedAll(segments).content();

        this.embeddingStore.addAll(embeddings, segments);
        
        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .build();
    }
}
