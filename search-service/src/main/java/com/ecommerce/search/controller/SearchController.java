package com.ecommerce.search.controller;


import com.ecommerce.search.model.Product;
import com.ecommerce.search.service.EmbeddingService;
import com.ecommerce.search.service.ProductFetcherService;
import com.ecommerce.search.service.QdrantService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    private final EmbeddingService embeddingService;
    private final QdrantService qdrantService;
    private final ProductFetcherService productFetcherService;

    public SearchController(EmbeddingService embeddingService,
                            QdrantService qdrantService,
                            ProductFetcherService productFetcherService) {
        this.embeddingService = embeddingService;
        this.qdrantService = qdrantService;
        this.productFetcherService = productFetcherService;
    }

    @GetMapping
    public List<Product> search(@RequestParam String query) {
        float[] embedding = embeddingService.generateEmbedding(query);
        List<String> productIds = qdrantService.searchSimilar(embedding, 5);
        List<Product> matchedProducts = new ArrayList<>();

        for (String id : productIds) {
            Product product = productFetcherService.fetchProductById(id);
            if (product != null) {
                matchedProducts.add(product);
            }
        }

        return matchedProducts;
    }
}
