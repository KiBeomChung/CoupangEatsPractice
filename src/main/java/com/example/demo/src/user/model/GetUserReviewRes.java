package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class GetUserReviewRes {

    private String name;
    private int reviewScore;
    private String reviewText;
    private Timestamp createdAt;
    private String foodName;
}
