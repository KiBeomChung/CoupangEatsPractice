package com.example.demo.src.user;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.POST_USERS_EMPTY_NAME;

@RestController
@RequestMapping("/app/users")
public class UserController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final UserProvider userProvider;
    @Autowired
    private final UserService userService;
    @Autowired
    private final JwtService jwtService;




    public UserController(UserProvider userProvider, UserService userService, JwtService jwtService){
        this.userProvider = userProvider;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    /**
     * 회원 조회 API
     * [GET] /users
     * 회원 번호 및 이메일 검색 조회 API
     * [GET] /users? Email=
     * @return BaseResponse<List<GetUserRes>>
     */
    //Query String
    @ResponseBody
    @GetMapping("") // (GET) 127.0.0.1:9000/app/users
    public BaseResponse<List<GetUserRes>> getUsers(@RequestParam(required = false) String name) {
        try{
            if(name == null){
                List<GetUserRes> getUsersRes = userProvider.getUsers(); //유저전체
                return new BaseResponse<>(getUsersRes);
            }
            // Get Users
            List<GetUserRes> getUsersRes = userProvider.getUsersByName(name);
            return new BaseResponse<>(getUsersRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 회원 1명 조회 API
     * [GET] /users/:userIdx
     * @return BaseResponse<GetUserRes>
     */
    // Path-variable
    @ResponseBody
    @GetMapping("/{user_Id}") // (GET) 127.0.0.1:9000/app/users/:userIdx
    public BaseResponse<GetUserRes> getUser(@PathVariable("user_Id") String user_Id) {
        // Get Users
        try{
            GetUserRes getUserRes = userProvider.getUser(user_Id);
            return new BaseResponse<>(getUserRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    /**
     * 회원가입 API
     * [POST] /users
     * @return BaseResponse<PostUserRes>
     */
    // Body
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostUserRes> createUser(@RequestBody PostUserReq postUserReq) {
        // TODO: email 관련한 짧은 validation 예시입니다. 그 외 더 부가적으로 추가해주세요!
        if(postUserReq.getName() == null){ //이름이 안들어왔을때 리턴값
            return new BaseResponse<>(POST_USERS_EMPTY_NAME);
        }
        //이메일 정규표현
//        if(!isRegexEmail(postUserReq.getEmail())){
//            return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
//        }
        try{
            PostUserRes postUserRes = userService.createUser(postUserReq);
            return new BaseResponse<>(postUserRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    /**
     * 로그인 API
     * [POST] /users/logIn
     * @return BaseResponse<PostLoginRes>
     */
    @ResponseBody
    @PostMapping("/logIn")
    public BaseResponse<PostLoginRes> logIn(@RequestBody PostLoginReq postLoginReq){
        try{
            // TODO: 로그인 값들에 대한 형식적인 validatin 처리해주셔야합니다!
            // TODO: 유저의 status ex) 비활성화된 유저, 탈퇴한 유저 등을 관리해주고 있다면 해당 부분에 대한 validation 처리도 해주셔야합니다.
            PostLoginRes postLoginRes = userProvider.logIn(postLoginReq);
            return new BaseResponse<>(postLoginRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 유저정보변경 API
     * [PATCH] /users/:userIdx
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{userId}")
    public BaseResponse<String> modifyUserAddress(@PathVariable("userId") String userId, @RequestBody PatchUserReq patchUserReq){
        try {
            //jwt에서 idx 추출.
            //int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
//            if(userIdx != userIdxByJwt){
//                return new BaseResponse<>(INVALID_USER_JWT);
//            }

            //주소 변경
            PatchUserReq patchUserReq1 = new PatchUserReq(userId, patchUserReq.getAddress());


            userService.modifyUserAddress(patchUserReq1);

            String result = "";
        return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

//    @ResponseBody
//    @GetMapping("/restaurant/{restaurantId}/detailInfo")
//    public BaseResponse<GetRestaurantDetailInfoReq> getRestaurantDetailInfo() throws BaseException{
//
//    }

    /**
     * 이츠에만 있는 가게
     */
    @ResponseBody
    @GetMapping("/EatsOnlyMain")
    public BaseResponse<List<GetEatsOnlyMainRes>> getEatsOnlyMain() throws BaseException{
        List<GetEatsOnlyMainRes> getEatsOnlyMainRes = userProvider.getEatsOnlyMain();
        return new BaseResponse<>(getEatsOnlyMainRes);
    }

    /**
     * 진행중인 이벤트 목록
     */
    @GetMapping("/eventList")
    @ResponseBody
    public BaseResponse<List<GetEventRes>> getEventList() throws BaseException{
        List<GetEventRes> getEventRes = userProvider.getEventRes();
        return new BaseResponse<>(getEventRes);
    }

    /**
     * 가게의 메뉴들
     */
    @GetMapping("restaurant/{restaurant_Id}")
    @ResponseBody
    public BaseResponse<List<GetRestaurantMenuRes>> getRestaurantMenuBase(@PathVariable("restaurant_Id") long restaurant_Id)  throws BaseException{
        List<GetRestaurantMenuRes> getRestaurantMenuRes = userProvider.getRestaurantMenu(restaurant_Id);
        return new BaseResponse<>(getRestaurantMenuRes);
    }

    /**
     * 회원의 즐겨찾기 가게
     */
    @GetMapping("/{user_Id}/favoriteRestaurant")
    @ResponseBody
    public BaseResponse<List<GetFavoriteRestaurantRes>> getFavoriteRestaurantList(@PathVariable("user_Id") String user_Id) throws BaseException{
        List<GetFavoriteRestaurantRes> getFavoriteRestaurantResList = userProvider.getFavoriteRestaurant(user_Id);
        return new BaseResponse<>(getFavoriteRestaurantResList);
    }

    /**
     * 음식 카테고리에 따른 가게 리스트
     */
//    @GetMapping("/{category_Name}/{lowPrice}/{highPrice}")
//    @ResponseBody
//    public BaseResponse<List<GetRestaurantListRes>> getRestaurantList(@RequestParam("category_Name") String category_Name,
//                                                                      @RequestParam("low_Price") int low_Price,
//                                                                      @RequestParam("high_Price") int high_Price){
//        List<GetRestaurantListRes> getRestaurantListRes = userProvider.getRestaurantList(category_Name, low_Price, high_Price);
//        return new BaseResponse<>(getRestaurantListRes);
//    }

//    @GetMapping("/category/{category_Name}")
//    @ResponseBody
//    public BaseResponse<List<GetRestaurantListRes>> getRestaurantList(@RequestParam(value = "category_Name", required = false) String category_Name){
//        List<GetRestaurantListRes> getRestaurantListRes = userProvider.getRestaurantList(category_Name);
//        return new BaseResponse<>(getRestaurantListRes);
//    }

    @GetMapping("/category")
    @ResponseBody
    public BaseResponse<List<GetRestaurantListRes>> getRestaurantList(@RequestParam(value = "category_Name") String category_Name,
                                                                      @RequestParam(value = "lowPrice") int lowPrice,
                                                                      @RequestParam(value = "highPrice") int highPrice){
        System.out.println(category_Name);
        List<GetRestaurantListRes> getRestaurantListRes = userProvider.getRestaurantList(category_Name, lowPrice, highPrice);
        return new BaseResponse<>(getRestaurantListRes);
    }

    /**
     * 식당의 리뷰 목록
     */
    @GetMapping("/{restaurantId}/reviewList")
    @ResponseBody
    public BaseResponse<List<GetRestaurantReviewRes>> getRestaurantReviewRes(@PathVariable("restaurantId") long restaurantId){
        List<GetRestaurantReviewRes> getRestaurantReviewResList = userProvider.getRestaurantReviewList(restaurantId);
        return new BaseResponse<>(getRestaurantReviewResList);
    }

    @GetMapping("{userId}/order/{orderId}")
    @ResponseBody
    public BaseResponse<GetReceiptRes> getReceiptResBaseResponse(@PathVariable("userId") String userId,
                                                                 @PathVariable("orderId") long orderId){
        GetReceiptRes getReceiptRes = userProvider.getReceiptRes(userId, orderId);
        return new BaseResponse<>(getReceiptRes);
    }

    @GetMapping("{userId}/orderList")
    @ResponseBody
    public BaseResponse<List<GetOrderListRes>> getOrderListRes(@PathVariable("userId") String userId){
        List<GetOrderListRes> getOrderListRes = userProvider.getOrderListRes(userId);
        return new BaseResponse<>(getOrderListRes);
    }

    @PostMapping("/review/{userId}/{orderId}")
    @ResponseBody
    public BaseResponse<PostUserReviewRes> writeReview(@RequestBody PostUserReviewReq postUserReviewReq,
                                                       @PathVariable("userId") String userId,
                                                       @PathVariable("orderId") long orderId){

        PostUserReviewRes postUserReviewRes = userService.writeReview(postUserReviewReq, userId, orderId);
        return new BaseResponse<>(postUserReviewRes);
    }

    @GetMapping("/review/{userId}/{orderId}")
    @ResponseBody
    public BaseResponse<GetUserReviewRes> getUserReviewResBaseResponse(@PathVariable("userId") String userId,
                                                                       @PathVariable("orderId") long orderId){
        GetUserReviewRes getUserReviewRes = userProvider.getUserReviewRes(userId, orderId);
            return new BaseResponse<>(getUserReviewRes);
    }

    @PatchMapping("/review/{userId}/{orderId}")
    @ResponseBody
    public BaseResponse<String> modifyUserReview(@PathVariable("userId") String userId,
                                                 @PathVariable("orderId") long orderId,
                                                 @RequestBody PatchUserReviewReq patchUserReviewReq){
        Timestamp dateNow = new Timestamp(System.currentTimeMillis()); //수정시간 받아오기
        patchUserReviewReq.setOrderedAt(dateNow);
        PatchUserReviewReq patchUserReviewReq1 = new PatchUserReviewReq(userId, orderId, patchUserReviewReq.getReviewText(), patchUserReviewReq.getOrderedAt());

        userService.modifyUserReview(patchUserReviewReq1);

        String result = "";
        return new BaseResponse<>(result);
    }

    @PatchMapping("review/delete/{userId}/{reviewId}")
    @ResponseBody
    public BaseResponse<String> deleteUserReview(@PathVariable("userId") String userId,
                                                 @PathVariable("reviewId") long reviewId,
                                                 @RequestBody PatchUserReviewDeleteReq patchUserReviewDeleteReq){

        PatchUserReviewDeleteReq patchUserReviewDeleteReq1 = new PatchUserReviewDeleteReq(userId, reviewId, patchUserReviewDeleteReq.getStatus());
        userService.deleteUserReview(patchUserReviewDeleteReq1);

        String result = "상태변경 완료하였습니다.";
        return new BaseResponse<>(result);

    }
//
//    @GetMapping()
//    @ResponseBody
//    public BaseResponse<List<>>


}

