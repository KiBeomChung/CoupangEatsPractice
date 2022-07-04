package com.example.demo.src.review;

import com.example.demo.src.review.model.GetUserReviewRes;
import com.example.demo.src.user.UserDao;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewProvider {

    private final ReviewDAO reviewDao;
    private final JwtService jwtService;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public ReviewProvider(ReviewDAO reviewDao, JwtService jwtService) {
        this.reviewDao = reviewDao;
        this.jwtService = jwtService;
    }

    public GetUserReviewRes getUserReviewRes(String userId, long orderId){
        GetUserReviewRes getUserReview = reviewDao.getUserReview(userId, orderId);
        return getUserReview;
    }
}
