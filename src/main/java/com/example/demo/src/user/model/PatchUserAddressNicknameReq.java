package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PatchUserAddressNicknameReq {

    private String userId;
    private long addressId;
    private String addressName;
}
