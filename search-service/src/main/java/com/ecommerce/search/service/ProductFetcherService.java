package com.ecommerce.search.service;

import com.ecommerce.search.model.Product;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ProductFetcherService {

    private final RestTemplate restTemplate;
    private final String productServiceUrl;

    public ProductFetcherService(RestTemplate restTemplate, @Value("${services.product-service.url}") String productServiceUrl) {
        this.restTemplate = restTemplate;
        this.productServiceUrl = productServiceUrl;
    }


    public Product[] fetchUnembeddedProducts() {
        return restTemplate.getForObject(productServiceUrl + "/unembedded", Product[].class);
    }

    public Product fetchProductById(String id) {
        return restTemplate.getForObject(productServiceUrl + "/" + id, Product.class);
    }
}
