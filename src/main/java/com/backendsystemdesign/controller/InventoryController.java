package com.backendsystemdesign.controller;

import com.backendsystemdesign.models.Product;

import com.backendsystemdesign.service.InventoryService;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(
            InventoryService inventoryService
    ) {

        this.inventoryService = inventoryService;
    }

    /*
        CREATE
     */
    @PostMapping
    public Product createProduct(
            @RequestBody Product product
    ) {

        return inventoryService
                .createProduct(product);
    }

    /*
        READ
     */
    @GetMapping("/{id}")
    public Product getProduct(
            @PathVariable Long id
    ) {

        return inventoryService
                .getProduct(id);
    }

    /*
        UPDATE
     */
    @PutMapping
    public Product updateProduct(
            @RequestBody Product product
    ) {

        return inventoryService
                .updateProduct(product);
    }

    /*
        DELETE
     */
    @DeleteMapping("/{id}")
    public String deleteProduct(
            @PathVariable Long id
    ) {

        return inventoryService
                .deleteProduct(id);
    }

    /*
        PURCHASE
     */
    @PostMapping("/purchase/{id}")
    public Product purchaseProduct(
            @PathVariable Long id
    ) {

        return inventoryService
                .purchaseProduct(id);
    }
}