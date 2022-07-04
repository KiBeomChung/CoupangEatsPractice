package com.example.demo.src.review;

import com.example.demo.config.BaseResponse;
import com.example.demo.src.review.model.*;
import com.example.demo.src.user.UserProvider;
import com.example.demo.src.user.UserService;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;

@RestController
@RequestMapping("/app/review")
public class ReviewController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final ReviewProvider reviewProvider;
    @Autowired
    private final ReviewService reviewService;
    @Autowired
    private final JwtService jwtService;

    public ReviewController(ReviewProvider reviewProvider, ReviewService reviewService, JwtService jwtService) {
        this.reviewProvider = reviewProvider;
        this.reviewService = reviewService;
        this.jwtService = jwtService;
    }

    @PostMapping("/write/{userId}/{orderId}")
    @ResponseBody
    public BaseResponse<PostUserReviewRes> writeReview(@RequestBody PostUserReviewReq postUserReviewReq,
                                                       @PathVariable("userId") String userId,
                                                       @PathVariable("orderId") long orderId){

        PostUserReviewRes postUserReviewRes = reviewService.writeReview(postUserReviewReq, userId, orderId);
        return new BaseResponse<>(postUserReviewRes);
    }

    @GetMapping("/{userId}/{orderId}")
    @ResponseBody
    public BaseResponse<GetUserReviewRes> getUserReviewResBaseResponse(@PathVariable("userId") String userId,
                                                                       @PathVariable("orderId") long orderId){
        GetUserReviewRes getUserReviewRes = reviewProvider.getUserReviewRes(userId, orderId);
        return new BaseResponse<>(getUserReviewRes);
    }

    @PatchMapping("/{userId}/{orderId}")
    @ResponseBody
    public BaseResponse<String> modifyUserReview(@PathVariable("userId") String userId,
                                                 @PathVariable("orderId") long orderId,
                                                 @RequestBody PatchUserReviewReq patchUserReviewReq){
        Timestamp dateNow = new Timestamp(System.currentTimeMillis()); //수정시간 받아오기
        patchUserReviewReq.setOrderedAt(dateNow);
        PatchUserReviewReq patchUserReviewReq1 = new PatchUserReviewReq(userId, orderId, patchUserReviewReq.getReviewText(), patchUserReviewReq.getOrderedAt());

        reviewService.modifyUserReview(patchUserReviewReq1);

        String result = "";
        return new BaseResponse<>(result);
    }

    @PatchMapping("/delete/{userId}/{reviewId}")
    @ResponseBody
    public BaseResponse<String> deleteUserReview(@PathVariable("userId") String userId,
                                                 @PathVariable("reviewId") long reviewId,
                                                 @RequestBody PatchUserReviewDeleteReq patchUserReviewDeleteReq){

        PatchUserReviewDeleteReq patchUserReviewDeleteReq1 = new PatchUserReviewDeleteReq(userId, reviewId, patchUserReviewDeleteReq.getStatus());
        reviewService.deleteUserReview(patchUserReviewDeleteReq1);

        String result = "상태변경 완료하였습니다.";
        return new BaseResponse<>(result);

    }
}
