package com.backendsystemdesign.controller;



import com.backendsystemdesign.models.Product;
import com.backendsystemdesign.service.ProductService;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(
            ProductService productService
    ) {

        this.productService = productService;
    }

    @GetMapping("/{id}")
    public Product getProduct(
            @PathVariable Long id
    ) {

        return productService.getProduct(id);
    }

    @PutMapping
    public Product updateProduct(
            @RequestBody Product product
    ) {

        return productService.updateProduct(
                product
        );
    }

    @DeleteMapping("/{id}")
    public String deleteProduct(
            @PathVariable Long id
    ) {

        productService.deleteProduct(id);

        return "Deleted";
    }
}
