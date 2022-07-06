package com.example.demo.src.review;

import com.example.demo.config.BaseException;
import com.example.demo.src.review.model.PatchUserReviewDeleteReq;
import com.example.demo.src.review.model.PatchUserReviewReq;
import com.example.demo.src.review.model.PostUserReviewReq;
import com.example.demo.src.review.model.PostUserReviewRes;
import com.example.demo.src.user.UserDao;
import com.example.demo.src.user.UserProvider;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class ReviewService {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ReviewDAO reviewDao;
    private final ReviewProvider reviewProvider;
    private final JwtService jwtService;
    private final UserDao userDao;

    @Autowired
    public ReviewService(ReviewDAO reviewDao, com.example.demo.src.review.ReviewProvider reviewProvider, JwtService jwtService, UserDao userDao) {
        this.reviewDao = reviewDao;
        this.reviewProvider = reviewProvider;
        this.jwtService = jwtService;
        this.userDao = userDao;
    }

    public PostUserReviewRes writeReview(PostUserReviewReq postUserReviewReq, String userId, long orderId) throws BaseException {

        if(userDao.checkOrderId(orderId) == 0){ //db에 입력받은 orderId가 존재하는지 확인
            throw new BaseException(NOT_EXISTS_ORDERID);
        }
        if(userDao.checkUserId(postUserReviewReq.getUserId()) == 0){ // db에 입력받은 userId가 존재하는지 확인
            throw new BaseException(NOT_EXISTS_USERID);
        }

        String user_Id = reviewDao.writeReview(postUserReviewReq, userId, orderId);
        return new PostUserReviewRes(user_Id);
    }

    public void modifyUserReview(PatchUserReviewReq patchUserReviewReq) throws BaseException {

        if(userDao.checkOrderId(patchUserReviewReq.getOrderId()) == 0){ //db에 입력받은 orderId가 존재하는지 확인
            throw new BaseException(NOT_EXISTS_ORDERID);
        }
        if(userDao.checkUserId(patchUserReviewReq.getUserId()) == 0){ // db에 입력받은 userId가 존재하는지 확인
            throw new BaseException(NOT_EXISTS_USERID);
        }
        //+ 텍스트의 길이가 너무 길때의
        if(patchUserReviewReq.getReviewText().length() > 100){
            throw new BaseException(POST_USERS_WRITE_TOO_LONG_TEXT);
        }

        int result = reviewDao.modifyUserReview(patchUserReviewReq);
    }

    public void deleteUserReview(PatchUserReviewDeleteReq patchUserReviewDeleteReq) throws BaseException {

        if(userDao.checkUserId(patchUserReviewDeleteReq.getUserId()) == 0){ //동일한 유저 id가 없을경우
            throw new BaseException(NOT_EXISTS_USERID);
        }
        if(userDao.checkOrderId(patchUserReviewDeleteReq.getReviewId()) == 0){ //db에 입력받은 orderId가 존재하는지 확인
            throw new BaseException(NOT_EXISTS_REVIEW_ID);
        }
        if(reviewDao.checkStatusStr(patchUserReviewDeleteReq)){ //입력한 상태가 올바르지 않을 경우
            throw new BaseException(WRONG_STATUS);
        }
    }

    // 별점이 0점 미만 or 5점 초과일 경우를 확인하는 메소드
    public boolean checkReviewScore(int reviewScore){

        boolean con = false;

        switch (reviewScore){
            case (0): case(1): case(2): case(3): case(4): case(5):
                con = true;
                break;
            default:
                con = false;
                break;
        }
        return con;
    }
}
