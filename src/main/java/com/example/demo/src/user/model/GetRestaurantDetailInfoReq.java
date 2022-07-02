package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter

public class GetRestaurantDetailInfoReq {
    private String restaurantLocationImg;
    private String restaurantLocationText;
    private String restaurantNum;
    private String ceoName;
    private String companyRegNum;
    private String name;
}
