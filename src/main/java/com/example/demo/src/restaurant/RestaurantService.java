package com.example.demo.src.restaurant;

import com.example.demo.src.user.UserDao;
import com.example.demo.src.user.UserProvider;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RestaurantService {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final RestaurantDAO restaurantDAO;
    private final RestaurantProvider restaurantProvider;
    private final JwtService jwtService;

    @Autowired
    public RestaurantService(RestaurantDAO restaurantDAO, RestaurantProvider restaurantProvider, JwtService jwtService) {
        this.restaurantDAO = restaurantDAO;
        this.restaurantProvider = restaurantProvider;
        this.jwtService = jwtService;
    }
}
