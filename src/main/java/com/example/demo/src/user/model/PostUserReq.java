package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class PostUserReq {
    private String userId;
    private String name;
    private String password;
    private String phone_Num;
    private String address;
}
