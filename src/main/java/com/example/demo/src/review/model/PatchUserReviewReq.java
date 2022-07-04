package com.example.demo.src.review.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class PatchUserReviewReq {

    String userId;
    long orderId;
    String reviewText;
    Timestamp orderedAt;
}
