package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetEventRes {

    private String eventName;
    private String file;
    private String endDate;
    private String status;
}
