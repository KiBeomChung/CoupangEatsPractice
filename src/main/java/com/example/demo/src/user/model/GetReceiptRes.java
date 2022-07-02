package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class GetReceiptRes {

    private String name;
    private long orderId;
    private Timestamp createdAt;
    private int price;
    private int deliveryFee;
    private int changes;
    private int sum;
    private int paySum;
    private String cardName;
    private String realAddress;
    private String cardNum;
}
