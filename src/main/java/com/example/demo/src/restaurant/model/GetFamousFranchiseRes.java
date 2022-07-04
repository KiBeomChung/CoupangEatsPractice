package com.example.demo.src.restaurant.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetFamousFranchiseRes {

    private String file;
    private String name;
    private String fastDelivery;
    private int reviewScore;
    private int deliveryFee;
}
