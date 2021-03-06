package com.example.demo.src.restaurant.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetRestaurantMenuRes {

    private String categoryName;
    private String foodName;
    private String mostReviewed;
    private String mostOrdered;
    private int price;
    private String foodIntroduction;
    private String file;
    private String status;

}
