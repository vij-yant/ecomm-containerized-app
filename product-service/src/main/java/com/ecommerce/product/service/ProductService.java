package com.ecommerce.product.service;

import com.ecommerce.product.model.Product;
import com.ecommerce.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public Product saveProduct(Product product) {
        return repository.save(product);
    }

    public List<Product> getAllProducts() {
        return repository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return repository.findById(id);
    }

    public void deleteProduct(Long id) {
        repository.deleteById(id);
    }

    public Optional<Product> updateProduct(Long id, Product updatedProduct) {
        return repository.findById(id).map(existing -> {
            existing.setName(updatedProduct.getName());
            existing.setBrand(updatedProduct.getBrand());
            existing.setSize(updatedProduct.getSize());
            existing.setPrice(updatedProduct.getPrice());
            existing.setDescription(updatedProduct.getDescription());
            existing.setQuantityInStock(updatedProduct.getQuantityInStock());
            return repository.save(existing);
        });
    }

    public List<Product> getUnembeddedProducts() {
        return repository.findByIsEmbeddedFalse()
                .stream()
                .map(product -> product)
                .collect(Collectors.toList());
    }

    public void markAsEmbedded(Long id) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setEmbedded(true);
        repository.save(product);
    }
}
