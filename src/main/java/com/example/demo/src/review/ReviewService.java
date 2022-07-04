package com.example.demo.src.review;

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

@Service
public class ReviewService {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ReviewDAO reviewDao;
    private final ReviewProvider reviewProvider;
    private final JwtService jwtService;

    @Autowired
    public ReviewService(ReviewDAO reviewDao, com.example.demo.src.review.ReviewProvider reviewProvider, JwtService jwtService) {
        this.reviewDao = reviewDao;
        this.reviewProvider = reviewProvider;
        this.jwtService = jwtService;
    }

    public PostUserReviewRes writeReview(PostUserReviewReq postUserReviewReq, String userId, long orderId){
        String user_Id = reviewDao.writeReview(postUserReviewReq, userId, orderId);
        return new PostUserReviewRes(user_Id);
    }

    public void modifyUserReview(PatchUserReviewReq patchUserReviewReq){
        int result = reviewDao.modifyUserReview(patchUserReviewReq);
    }

    public void deleteUserReview(PatchUserReviewDeleteReq patchUserReviewDeleteReq){
        int result = reviewDao.deleteUserReview(patchUserReviewDeleteReq);
    }
}
