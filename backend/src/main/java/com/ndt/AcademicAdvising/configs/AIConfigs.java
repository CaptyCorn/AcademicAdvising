/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndt.AcademicAdvising.configs;

import com.ndt.AcademicAdvising.rag.RAGAssistant;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiChatModelName;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModelName;
import dev.langchain4j.rag.DefaultRetrievalAugmentor;
import dev.langchain4j.rag.RetrievalAugmentor;
import dev.langchain4j.rag.content.injector.ContentInjector;
import dev.langchain4j.rag.content.injector.DefaultContentInjector;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.pgvector.PgVectorEmbeddingStore;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author ngodo
 */
@Configuration
public class AIConfigs {
    @Value("${open.ai.api.key}")
    private String apiKey;
    
    @Value("${spring.datasource.username}")
    private String dbUsername;
    
    @Value("${spring.datasource.password}")
    private String dbPassword;
    
    @Bean
    public EmbeddingStore<TextSegment> embeddingStrore() {
        return PgVectorEmbeddingStore.builder()
                .host("localhost")
                .port(5555)
                .database("academic")
                .user(dbUsername)
                .password(dbPassword)
                .table("tbl_document_chunk")
                .dimension(embeddingModel().dimension())
                .build();
    }
    
    @Bean
    public EmbeddingModel embeddingModel() {
        return OpenAiEmbeddingModel.builder()
                .apiKey(apiKey)
                .modelName(OpenAiEmbeddingModelName.TEXT_EMBEDDING_3_SMALL)
                .build();
    }
    
    @Bean
    public ChatModel chatModel() {
        return OpenAiChatModel.builder()
                .apiKey(apiKey)
                .modelName(OpenAiChatModelName.GPT_4_1_MINI)
                .build();
    }
    
    @Bean
    public RAGAssistant ragAssistant() {
        ContentRetriever contentRetriever = EmbeddingStoreContentRetriever.builder()
                .embeddingModel(embeddingModel())
                .embeddingStore(embeddingStrore())
                .maxResults(5)
                .minScore(0.7)
                .build();
        
        ContentInjector contentInjector = DefaultContentInjector.builder()
                .metadataKeysToInclude(Arrays.asList("file_name", "section", "page", "chunk_index", "document_type"))
                .build();
        
        RetrievalAugmentor retrievalAugmentor = DefaultRetrievalAugmentor.builder()
                .contentRetriever(contentRetriever)
                .contentInjector(contentInjector)
                .build();
        
        return AiServices.builder(RAGAssistant.class)
                .chatModel(chatModel())
                .retrievalAugmentor(retrievalAugmentor)
                .chatMemoryProvider(memberId -> MessageWindowChatMemory.withMaxMessages(10))
                .build();
    }
}
