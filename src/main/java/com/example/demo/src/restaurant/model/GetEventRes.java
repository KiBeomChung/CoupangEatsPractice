package com.example.demo.src.restaurant.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class GetEventRes {

    private String eventName;
    private String file;
    private Timestamp endDate;
    private String status;
}
