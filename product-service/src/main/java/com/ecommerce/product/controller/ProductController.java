package com.ecommerce.product.controller;

import com.ecommerce.product.model.Product;
import com.ecommerce.product.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product) {
        return ResponseEntity.status(201).body(service.saveProduct(product));
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(service.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Long id) {
        return service.getProductById(id)
                .map(product -> ResponseEntity.ok().body(product))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @Valid @RequestBody Product product) {
        return service.updateProduct(id, product)
                .map(updated -> ResponseEntity.ok().body(updated))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        service.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/unembedded")
    public ResponseEntity<List<Product>> getUnembeddedProducts() {
        return ResponseEntity.ok(service.getUnembeddedProducts());
    }

    @PutMapping("/{id}/mark-embedded")
    public ResponseEntity<Void> markProductAsEmbedded(@PathVariable Long id) {
        service.markAsEmbedded(id);
        return ResponseEntity.ok().build();
    }
}
