package com.example.demo.src.restaurant;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.restaurant.model.*;
import com.example.demo.src.user.UserDao;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class RestaurantProvider {

    private final RestaurantDAO restaurantDAO;
    private final UserDao userDao;
    private final JwtService jwtService;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public RestaurantProvider(RestaurantDAO restaurantDAO, UserDao userDao, JwtService jwtService) {
        this.restaurantDAO = restaurantDAO;
        this.userDao = userDao;
        this.jwtService = jwtService;
    }

    public List<GetRestaurantMenuRes> getRestaurantMenu(long restaurant_Id) throws BaseException {

        if(restaurantDAO.checkRestaurantId(restaurant_Id) != 1){ //해당 가게 id가 디비에 없을 경우
            throw new BaseException(NOT_EXISTS_RESTAURANT_ID);
        }

        List<GetRestaurantMenuRes> getRestaurantMenuRes = restaurantDAO.getRestaurantMenuById(restaurant_Id);
        return getRestaurantMenuRes;
    }

    public List<GetFavoriteRestaurantRes> getFavoriteRestaurant(String userId) throws BaseException {

        if(userDao.checkUserId(userId) == 0){ // db에 입력받은 userId가 존재하는지 확인
            throw new BaseException(NOT_EXISTS_USERID);
        }

        List<GetFavoriteRestaurantRes> getFavoriteRestaurantRes = restaurantDAO.getFavoriteRestaurant(userId);
        return getFavoriteRestaurantRes;
    }

    public List<GetRestaurantListRes> getRestaurantList(String categoryName, int lowPrice, int highPrice) throws BaseException {

        if(restaurantDAO.checkCategoryName(categoryName) != 1){ //카테고리 이름이 제대로 입력되어있는지 확인
            throw new BaseException(NOT_EXISTS_RESTAURANT_CATEGORY);
        }
        if(restaurantDAO.checkDeliveryFee(lowPrice, highPrice)){ //배달비가 제대로 설정되어 있는지 확인
            throw new BaseException(INVALID_DELIVERY_FEE);
        }

        List<GetRestaurantListRes> getRestaurantList = restaurantDAO.getRestaurantList(categoryName, lowPrice, highPrice);
        return getRestaurantList;
    }

    public List<GetRestaurantReviewRes> getRestaurantReviewList(long restaurantId) throws BaseException {

        if(restaurantDAO.checkRestaurantId(restaurantId) != 1){ //해당 가게 id가 데이터베이스에 없을 경우
            throw new BaseException(NOT_EXISTS_RESTAURANT_ID);
        }

        List<GetRestaurantReviewRes> getRestaurantReview = restaurantDAO.getRestaurantReview(restaurantId);
        return getRestaurantReview;

    }

    public List<GetFamousFranchiseRes> getFamousFranchiseResList(){
        List<GetFamousFranchiseRes> getFamousFranchiseResList = restaurantDAO.getFamousFranchiseList();
        return getFamousFranchiseResList;
    }

    public List<GetEatsOnlyMainRes> getEatsOnlyMain(String status) throws BaseException {

        if(!restaurantDAO.checkStatus(status)){ //status 의 스펠링이 틀렸을 경우
            throw new BaseException(WRONG_STATUS);
        }

        List<GetEatsOnlyMainRes> getEatsOnlyMainRes = restaurantDAO.getEatsOnlyMain(status);
        return getEatsOnlyMainRes;
    }

//    public List<GetEventRes> checkEventEndDate(){
//
//
//    }

    public List<GetEventRes> getEventRes() throws BaseException{

        List<GetEventRes> getEventRes = restaurantDAO.getEventList();
        return getEventRes;
    }
}
