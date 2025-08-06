package com.ecommerce.search.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class QdrantService {

    private final RestTemplate restTemplate;
    private final String qdrantUrl;
    private final String collectionName;
    private final int embeddingSize;

    public QdrantService(
            RestTemplate restTemplate,
            @Value("${qdrant.url}") String qdrantUrl,
            @Value("${qdrant.collection}") String collectionName,
            @Value("${qdrant.embedding-size}") int embeddingSize
    ) {
        this.restTemplate = restTemplate;
        this.qdrantUrl = qdrantUrl;
        this.collectionName = collectionName;
        this.embeddingSize = embeddingSize;
    }

    public void uploadEmbedding(Long id, float[] embedding, Map<String, Object> payloadMetadata) throws JsonProcessingException {
        ensureCollectionExists();

        Map<String, Object> point = new HashMap<>();
        point.put("id", id);
        point.put("vector", embedding);
        point.put("payload", payloadMetadata); // Optional metadata

        Map<String, Object> requestBody = Map.of("points", List.of(point));
        System.out.println("URL: " + qdrantUrl + "/collections/" + collectionName + "/points");
        System.out.println("Body: " + new ObjectMapper().writeValueAsString(requestBody));

        restTemplate.exchange(
                qdrantUrl + "/collections/" + collectionName + "/points",
                HttpMethod.PUT,
                new HttpEntity<>(requestBody),
                String.class
        );
    }

    public List<String> searchSimilar(float[] queryVector, int topK) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("vector", queryVector);
        payload.put("top", topK);

        Map<String, Object> response = restTemplate.postForObject(
                qdrantUrl + "/collections/" + collectionName + "/points/search",
                payload,
                Map.class
        );

        if (response == null || response.get("result") == null) return List.of();

        List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("result");
        List<String> ids = new ArrayList<>();
        for (Map<String, Object> item : results) {
            ids.add(String.valueOf(item.get("id")));
        }

        return ids;
    }

    private void ensureCollectionExists() {
        String collectionUrl = qdrantUrl + "/collections/" + collectionName;

        try {
            restTemplate.getForEntity(collectionUrl, String.class);
        } catch (Exception ex) {
            // If not found (404), create it
            Map<String, Object> vectors = Map.of(
                    "size", embeddingSize,
                    "distance", "Cosine"
            );

            Map<String, Object> createPayload = Map.of("vectors", vectors);

            restTemplate.put(collectionUrl, createPayload);
        }
    }
}
