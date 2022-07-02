package com.example.demo.src.user;


import com.example.demo.config.BaseException;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.AES128;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

//Provider : Read의 비즈니스 로직 처리
@Service
public class UserProvider {

    private final UserDao userDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public UserProvider(UserDao userDao, JwtService jwtService) {
        this.userDao = userDao;
        this.jwtService = jwtService;
    }

    public List<GetUserRes> getUsers() throws BaseException{
       // try{
            List<GetUserRes> getUserRes = userDao.getUsers();
            return getUserRes;
       // }
//        catch (Exception exception) {
//            throw new BaseException(DATABASE_ERROR);
//        }
    }

    public List<GetUserRes> getUsersByName(String name) throws BaseException{
        try{
            List<GetUserRes> getUsersRes = userDao.getUsersByName(name);
            return getUsersRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
                    }


    public GetUserRes getUser(String user_Id) throws BaseException {
        try {
            GetUserRes getUserRes = userDao.getUser(user_Id);
            return getUserRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkEmail(String email) throws BaseException{
        try{
            return userDao.checkEmail(email);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PostLoginRes logIn(PostLoginReq postLoginReq) throws BaseException{
        User user = userDao.getPwd(postLoginReq);
        String password;
        try {
            password = new AES128(Secret.USER_INFO_PASSWORD_KEY).decrypt(user.getPassword());
        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_DECRYPTION_ERROR);
        }

        if(postLoginReq.getPassword().equals(password)){
            int userIdx = userDao.getPwd(postLoginReq).getUserIdx();
            String jwt = jwtService.createJwt(userIdx);
            return new PostLoginRes(userIdx,jwt);
        }
        else{
            throw new BaseException(FAILED_TO_LOGIN);
        }

    }

    public List<GetEatsOnlyMainRes> getEatsOnlyMain() throws BaseException{
        List<GetEatsOnlyMainRes> getEatsOnlyMainRes = userDao.getEatsOnlyMain();
        return getEatsOnlyMainRes;
    }

    public List<GetEventRes> getEventRes() throws BaseException{
        List<GetEventRes> getEventRes = userDao.getEventList();
        return getEventRes;
    }

    public List<GetRestaurantMenuRes> getRestaurantMenu(long restaurant_Id){
        List<GetRestaurantMenuRes> getRestaurantMenuRes = userDao.getRestaurantMenuById(restaurant_Id);
        return getRestaurantMenuRes;
    }

    public List<GetFavoriteRestaurantRes> getFavoriteRestaurant(String user_Id){
        List<GetFavoriteRestaurantRes> getFavoriteRestaurantRes = userDao.getFavoriteRestaurant(user_Id);
        return getFavoriteRestaurantRes;
    }

    public List<GetRestaurantListRes> getRestaurantList(String category_Name, int low_Price, int high_Price){
        List<GetRestaurantListRes> getRestaurantList = userDao.getRestaurantList(category_Name, low_Price, high_Price);
        return getRestaurantList;
    }

//    public List<GetRestaurantListRes> getRestaurantList(String category_Name){
//        List<GetRestaurantListRes> getRestaurantList = userDao.getRestaurantList(category_Name);
//        return getRestaurantList;
//    }

    public List<GetRestaurantReviewRes> getRestaurantReviewList(long restaurantId){
        List<GetRestaurantReviewRes> getRestaurantReview = userDao.getRestaurantReview(restaurantId);
        return getRestaurantReview;

    }

    public GetReceiptRes getReceiptRes(String userId, long orderId){
        GetReceiptRes getReceipt = userDao.getReceipt(userId, orderId);
        return getReceipt;
    }
    public List<GetOrderListRes> getOrderListRes(String userId){
        List<GetOrderListRes> getOrderList = userDao.getOrderList(userId);
        return getOrderList;
    }

    public GetUserReviewRes getUserReviewRes(String userId, long orderId){
        GetUserReviewRes getUserReview = userDao.getUserReview(userId, orderId);
        return getUserReview;
    }

}