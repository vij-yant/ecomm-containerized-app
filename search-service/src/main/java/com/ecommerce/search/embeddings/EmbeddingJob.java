package com.ecommerce.search.embeddings;

import com.ecommerce.search.model.Product;
import com.ecommerce.search.service.EmbeddingService;
import com.ecommerce.search.service.ProductFetcherService;
import com.ecommerce.search.service.QdrantService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.annotation.PostConstruct;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class EmbeddingJob {

    private final ProductFetcherService productFetcherService;
    private final EmbeddingService embeddingService;
    private final QdrantService qdrantService;

    public EmbeddingJob(ProductFetcherService productFetcherService,
                        EmbeddingService embeddingService,
                        QdrantService qdrantService) {
        this.productFetcherService = productFetcherService;
        this.embeddingService = embeddingService;
        this.qdrantService = qdrantService;
    }

    @Scheduled(initialDelay = 60 * 1000, fixedRate = 5 * 60 * 1000)
    public void run() throws JsonProcessingException {
        Product[] products = productFetcherService.fetchUnembeddedProducts();
        for (Product product : products) {
            String combinedText = product.getName() + " " + product.getBrand() + " " + product.getDescription();
            float[] embedding = embeddingService.generateEmbedding(combinedText);
            qdrantService.uploadEmbedding(product.getId(), embedding,null);
        }
    }
}
