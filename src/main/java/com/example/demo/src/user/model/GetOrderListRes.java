package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class GetOrderListRes {

    private String name;
    private Timestamp createdAt;
    private String status;
    private String foodName;
    private String file;
    private int totalPrice;

}
