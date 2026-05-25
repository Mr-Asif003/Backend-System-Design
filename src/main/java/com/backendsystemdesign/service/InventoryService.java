package com.backendsystemdesign.service;

import com.backendsystemdesign.models.Product;

import org.redisson.api.RLock;

import org.redisson.api.RedissonClient;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import org.springframework.stereotype.Service;

import java.util.Map;

import java.util.HashMap;

import java.util.concurrent.TimeUnit;

@Service
public class InventoryService {

    private final RedissonClient redissonClient;

    private final Map<Long, Product> fakeDatabase =
            new HashMap<>();

    public InventoryService(
            RedissonClient redissonClient
    ) {

        this.redissonClient = redissonClient;

        fakeDatabase.put(
                1L,
                Product.builder()
                        .id(1L)
                        .name("iPhone 16")
                        .price(95000)
                        .quantity(5)
                        .build()
        );

        fakeDatabase.put(
                2L,
                Product.builder()
                        .id(2L)
                        .name("MacBook Pro")
                        .price(210000)
                        .quantity(3)
                        .build()
        );
    }

    /*
        CREATE
     */
    public Product createProduct(
            Product product
    ) {

        fakeDatabase.put(
                product.getId(),
                product
        );

        return product;
    }

    /*
        READ + CACHE
     */
    @Cacheable(
            value = "products",
            key = "#id"
    )
    public Product getProduct(Long id) {

        simulateSlowDatabase();

        System.out.println(
                "Fetching product from DB..."
        );

        return fakeDatabase.get(id);
    }

    /*
        UPDATE + CACHE UPDATE
     */
    @CachePut(
            value = "products",
            key = "#product.id"
    )
    public Product updateProduct(
            Product product
    ) {

        fakeDatabase.put(
                product.getId(),
                product
        );

        return product;
    }

    /*
        DELETE
     */
    @CacheEvict(
            value = "products",
            key = "#id"
    )
    public String deleteProduct(Long id) {

        fakeDatabase.remove(id);

        return "Deleted Successfully";
    }

    /*
        PURCHASE WITH DISTRIBUTED LOCK
     */
    @CachePut(
            value = "products",
            key = "#productId"
    )
    public Product purchaseProduct(
            Long productId
    ) {

        RLock lock = redissonClient
                .getLock(
                        "product-lock-" + productId
                );

        try {

            boolean isLocked = lock.tryLock(
                    10,
                    5,
                    TimeUnit.SECONDS
            );

            if(isLocked) {

                Product product =
                        fakeDatabase.get(productId);

                if(product == null) {

                    throw new RuntimeException(
                            "Product not found"
                    );
                }

                if(product.getQuantity() <= 0) {

                    throw new RuntimeException(
                            "Out of stock"
                    );
                }

                System.out.println(
                        "Processing Order..."
                );

                Thread.sleep(3000);

                product.setQuantity(
                        product.getQuantity() - 1
                );

                fakeDatabase.put(
                        productId,
                        product
                );

                System.out.println(
                        "Order Completed"
                );

                return product;
            }

            throw new RuntimeException(
                    "Could not acquire lock"
            );

        } catch (Exception e) {

            throw new RuntimeException(e);

        } finally {

            if(lock.isHeldByCurrentThread()) {

                lock.unlock();
            }
        }
    }

    private void simulateSlowDatabase() {

        try {

            Thread.sleep(3000);

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();
        }
    }
}