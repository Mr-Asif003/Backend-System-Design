package com.backendsystemdesign.service;

import com.backendsystemdesign.models.Product;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ProductService {
    private final Map<Long, Product> fakeDatabase=new HashMap<>();

    public ProductService(){
        fakeDatabase.put(1L,new Product().builder()
                .id(1L)
                .name("Asif ")
                .price(95213)
                .build());
        fakeDatabase.put(2L,new Product().builder()
                .id(2L)
                .name("Ashab ")
                .price(9999)
                .build());

    }

    @Cacheable(
            value = "products",
            key = "#id"
    )
    public Product getProduct(Long id){
        simulateSlowDatabase();
        System.out.println(
                "Fetching from database..."
        );
        return fakeDatabase.get(id);
    }

    @CachePut(
            value = "products",
            key="#product.id()"

    )
    public Product updateProduct(
            Product product
    ){
        fakeDatabase.put(
                product.getId(),
                product
        );
        return product;
    }

    @CacheEvict(
            value = "products",
            key = "#id"
    )
    public void deleteProduct(Long id){
        fakeDatabase.remove(id);
    }

    private void simulateSlowDatabase() {

        try {

            Thread.sleep(3000);

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();
        }
    }
}
