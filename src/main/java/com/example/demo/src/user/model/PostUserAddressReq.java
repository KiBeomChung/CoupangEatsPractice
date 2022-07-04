package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostUserAddressReq {

    private String userId;
    private String addressName;
    private String realAddress;
    private String status;

}
