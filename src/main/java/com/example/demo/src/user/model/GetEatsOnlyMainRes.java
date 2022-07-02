package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetEatsOnlyMainRes {
   private String eatsOnly;
   private String file;
   private String name;
   private double reviewScore;
   private int deliveryFee;
   private String packaging;
}
