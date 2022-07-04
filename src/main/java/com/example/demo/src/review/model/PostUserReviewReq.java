package com.example.demo.src.review.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostUserReviewReq {

    private String userId;
    private String reviewText;
    private int reviewScore;
    private int orderId;
}
