package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetFavoriteRestaurantRes {

    private String name;
    private String fastDelivery;
    private int reviewScore;
    private int deliveryFee;
    private String packaging;
    private String file;
}
