package com.backendsystemdesign.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Product implements Serializable {
    private  Long id;
    private  String name;
    private double price;
    private int quantity;

}
