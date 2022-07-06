package com.example.demo.src.review;

import com.example.demo.config.BaseException;
import com.example.demo.src.review.model.GetUserReviewRes;
import com.example.demo.src.user.UserDao;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.NOT_EXISTS_ORDERID;
import static com.example.demo.config.BaseResponseStatus.NOT_EXISTS_USERID;

@Service
public class ReviewProvider {

    private final ReviewDAO reviewDao;
    private final UserDao userDao;
    private final JwtService jwtService;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public ReviewProvider(ReviewDAO reviewDao, UserDao userDao, JwtService jwtService) {
        this.reviewDao = reviewDao;
        this.userDao = userDao;
        this.jwtService = jwtService;
    }


    public GetUserReviewRes getUserReviewRes(String userId, long orderId) throws BaseException {

        if(userDao.checkOrderId(orderId) == 0){ //db에 입력받은 orderId가 존재하는지 확인
            throw new BaseException(NOT_EXISTS_ORDERID);
        }
        if(userDao.checkUserId(userId) == 0){ // db에 입력받은 userId가 존재하는지 확인
            throw new BaseException(NOT_EXISTS_USERID);
        }

        GetUserReviewRes getUserReview = reviewDao.getUserReview(userId, orderId);
        return getUserReview;
    }
}
