package com.ecommerce.search.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.*;


import java.util.*;

@Service
public class EmbeddingService {

    private final RestTemplate restTemplate;
    private final String embeddingUrl;

    public EmbeddingService(RestTemplate restTemplate,
                            @Value("${embedding.url}") String embeddingUrl) {
        this.restTemplate = restTemplate;
        this.embeddingUrl = embeddingUrl;
    }

    public float[] generateEmbedding(String text) {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("text", text);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(embeddingUrl, entity, Map.class);
        List<Double> embeddingList = (List<Double>) response.getBody().get("embedding");

        // Convert List<Double> to float[]
        float[] embeddingArray = new float[embeddingList.size()];
        for (int i = 0; i < embeddingList.size(); i++) {
            embeddingArray[i] = embeddingList.get(i).floatValue();
        }

        return embeddingArray;
    }
}
