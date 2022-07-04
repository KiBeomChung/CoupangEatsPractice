package com.example.demo.src.restaurant;

import com.example.demo.config.BaseException;
import com.example.demo.src.restaurant.model.*;
import com.example.demo.src.user.UserDao;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantProvider {

    private final RestaurantDAO restaurantDAO;
    private final JwtService jwtService;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public RestaurantProvider(RestaurantDAO restaurantDAO, JwtService jwtService) {
        this.restaurantDAO = restaurantDAO;
        this.jwtService = jwtService;
    }

    public List<GetRestaurantMenuRes> getRestaurantMenu(long restaurant_Id){
        List<GetRestaurantMenuRes> getRestaurantMenuRes = restaurantDAO.getRestaurantMenuById(restaurant_Id);
        return getRestaurantMenuRes;
    }

    public List<GetFavoriteRestaurantRes> getFavoriteRestaurant(String user_Id){
        List<GetFavoriteRestaurantRes> getFavoriteRestaurantRes = restaurantDAO.getFavoriteRestaurant(user_Id);
        return getFavoriteRestaurantRes;
    }

    public List<GetRestaurantListRes> getRestaurantList(String category_Name, int low_Price, int high_Price){
        List<GetRestaurantListRes> getRestaurantList = restaurantDAO.getRestaurantList(category_Name, low_Price, high_Price);
        return getRestaurantList;
    }

    public List<GetRestaurantReviewRes> getRestaurantReviewList(long restaurantId){
        List<GetRestaurantReviewRes> getRestaurantReview = restaurantDAO.getRestaurantReview(restaurantId);
        return getRestaurantReview;

    }

    public List<GetFamousFranchiseRes> getFamousFranchiseResList(){
        List<GetFamousFranchiseRes> getFamousFranchiseResList = restaurantDAO.getFamousFranchiseList();
        return getFamousFranchiseResList;
    }

    public List<GetEatsOnlyMainRes> getEatsOnlyMain() throws BaseException {
        List<GetEatsOnlyMainRes> getEatsOnlyMainRes = restaurantDAO.getEatsOnlyMain();
        return getEatsOnlyMainRes;
    }

    public List<GetEventRes> getEventRes() throws BaseException{
        List<GetEventRes> getEventRes = restaurantDAO.getEventList();
        return getEventRes;
    }
}
