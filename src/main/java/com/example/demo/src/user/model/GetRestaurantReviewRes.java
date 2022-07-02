package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class GetRestaurantReviewRes {

    private String name;
    private String file;
    private String userName;
    private int reviewScore;
    private String reviewText;
    private String foodName;
    private Timestamp createdAt;

}
