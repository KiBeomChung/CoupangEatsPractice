package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetRestaurantListRes {

    private String categoryName;
    private String file;
    private String name;
    private int reviewScore;
    private int deliveryFee;
    private String fastDelivery;
    private String packaging;
}
