package com.example.demo.src.review;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.review.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;

import static com.example.demo.config.BaseResponseStatus.*;

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

    //아예 안넣는것과 빈칸을 넣는것의 차이점?? -> 빈칸을 넣는건 null값이 저장되는것이 아니라 공백이 저장되는것.

    /**
     * 리뷰 작성
     * @param postUserReviewReq
     * @param userId
     * @param orderId
     * @return BaseResponse<PostUserReviewRes>
     */
    @PostMapping("/write/{userId}/{orderId}")
    @ResponseBody
    public BaseResponse<PostUserReviewRes> writeReview(@RequestBody PostUserReviewReq postUserReviewReq,
                                                       @PathVariable("userId") String userId,
                                                       @PathVariable("orderId") long orderId) throws BaseException {

        if(userId == null){ //userId의 값이 없을떄
            return new BaseResponse<>(USERS_EMPTY_USER_ID);
        }
        // + orderId, + reviewScore 의 값이 null값 일때 예외처리 메소드

        String userIdByJwt = jwtService.getUserId();
        if(!userId.equals(userIdByJwt)){
            return new BaseResponse<>(INVALID_USER_JWT);
        }

        if(postUserReviewReq.getReviewText() == null) { // 리뷰 부분이 nulll 일때 예외처리 (null과 공백은 다름, 공백일때는 문제 x)
            return new BaseResponse<>(POST_USERS_EMPTY_TEXT);
        }
        if(reviewService.checkReviewScore(postUserReviewReq.getReviewScore()) == false){ //별점을 0미만, 5이상으로 주었을 경우
            return new BaseResponse<>(INVALID_REVIEW_SCORE);
        }

        PostUserReviewRes postUserReviewRes = reviewService.writeReview(postUserReviewReq, userId, orderId);
        return new BaseResponse<>(postUserReviewRes);
    }

    /**
     * 리뷰 조회
     * @param userId
     * @param orderId
     * @return BaseResponse<GetUserReviewRes>
     */
    @GetMapping("/{userId}/{orderId}")
    @ResponseBody
    public BaseResponse<GetUserReviewRes> getUserReviewResBaseResponse(@PathVariable("userId") String userId,
                                                                       @PathVariable("orderId") long orderId) throws BaseException {

        if(userId == null){ //userId의 값이 없을떄
            return new BaseResponse<>(USERS_EMPTY_USER_ID);
        }

        String userIdByJwt = jwtService.getUserId();
        if(!userId.equals(userIdByJwt)){
            return new BaseResponse<>(INVALID_USER_JWT);
        }
        // + orderId의 값이 null값 일때 예외처리 메소드
        try {
            GetUserReviewRes getUserReviewRes = reviewProvider.getUserReviewRes(userId, orderId);
            return new BaseResponse<>(getUserReviewRes);
        } catch(BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 리뷰 수정
     * @param userId
     * @param orderId
     * @param patchUserReviewReq
     * @return BaseResponse<String>
     */
    @PatchMapping("/{userId}/{orderId}")
    @ResponseBody
    public BaseResponse<String> modifyUserReview(@PathVariable("userId") String userId,
                                                 @PathVariable("orderId") long orderId,
                                                 @RequestBody PatchUserReviewReq patchUserReviewReq) throws BaseException {

        String userIdByJwt = jwtService.getUserId();
        if(!userId.equals(userIdByJwt)){
            return new BaseResponse<>(INVALID_USER_JWT);
        }

        if(userId == null){ //userId의 값이 없을떄
            return new BaseResponse<>(USERS_EMPTY_USER_ID);
        }
        // + orderId의 값이 null값 일때 예외처리 메소드
        if(patchUserReviewReq.getReviewText() == null){ //text부분이 없을때
            return new BaseResponse<>(POST_USERS_EMPTY_TEXT);
        }

        Timestamp dateNow = new Timestamp(System.currentTimeMillis()); //수정시간 받아오기
        patchUserReviewReq.setOrderedAt(dateNow); //수정된 시간 저장

        try {
            PatchUserReviewReq patchUserReviewReq1 = new PatchUserReviewReq(userId, orderId, patchUserReviewReq.getReviewText(), patchUserReviewReq.getOrderedAt());

            reviewService.modifyUserReview(patchUserReviewReq1);
            String result = "";
            return new BaseResponse<>(result);

        }   catch(BaseException exception) {
                return new BaseResponse<>(exception.getStatus());
            }
    }

    /**
     * 리뷰 삭제
     * @param userId
     * @param reviewId
     * @param
     * @return BaseResponse<String>
     */
    @DeleteMapping("/delete/{userId}/{reviewId}")
    public BaseResponse<String> deleteUserReview(@PathVariable("userId") String userId,
                                                 @PathVariable("reviewId") long reviewId
                                                 ) throws BaseException {

        String userIdByJwt = jwtService.getUserId();
        if(!userId.equals(userIdByJwt)){
            return new BaseResponse<>(INVALID_USER_JWT);
        }

        try {
            DeleteUserReviewDeleteReq deleteUserReviewDeleteReq1 = new DeleteUserReviewDeleteReq(userId, reviewId);
            reviewService.deleteUserReview(deleteUserReviewDeleteReq1);

            String result = "삭제 완료하였습니다.";
            return new BaseResponse<>(result);
        } catch(BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
