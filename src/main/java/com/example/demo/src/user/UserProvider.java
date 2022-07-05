package com.example.demo.src.user;


import com.example.demo.config.BaseException;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.restaurant.model.*;
import com.example.demo.src.review.model.GetUserReviewRes;
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

    public int checkUserId(String userId) throws BaseException{
        try{
            return userDao.checkUserId(userId);
        } catch(Exception e) {
            throw new BaseException(NOT_EXISTS_USERID);
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

    public GetReceiptRes getReceiptRes(String userId, long orderId) throws Exception{

        if(userDao.checkUserId(userId) == 0){ // db에 입력받은 userId가 존재하는지 확인
            throw new BaseException(NOT_EXISTS_USERID);
        }

        if(userDao.checkOrderId(orderId) == 0){ //db에 입력받은 orderId가 존재하는지 확인
            throw new BaseException(NOT_EXISTS_ORDERID);
        }

        GetReceiptRes getReceipt = userDao.getReceipt(userId, orderId);
        return getReceipt;
    }

    public List<GetOrderListRes> getOrderListRes(String userId) throws BaseException {

        if(userDao.checkUserId(userId) == 0){ // db에 입력받은 userId가 존재하는지 확인
            throw new BaseException(NOT_EXISTS_USERID);
        }

        List<GetOrderListRes> getOrderList = userDao.getOrderList(userId);
        return getOrderList;
    }

    public List<GetUserAddressRes> getUserAddressList(String userId){
        List<GetUserAddressRes> getUserAddressResList = userDao.getUserAddressList(userId);
        return getUserAddressResList;
    }

}
