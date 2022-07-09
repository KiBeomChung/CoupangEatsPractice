package com.example.demo.src.user;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

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
        if(postUserReq.getUserId() == null){ //userId가 안들어왔을때 리턴값
            return new BaseResponse<>(USERS_EMPTY_USER_ID);
        }
        if(postUserReq.getPhone_Num() == null){ //휴대폰 번호가 안들어왔을떄 리턴값
            return new BaseResponse<>(POST_USERS_EMPTY_PHONENUM);
        }
        if(postUserReq.getAddress() == null){ //주소가 안들어왔을때 리턴값
            return new BaseResponse<>(POST_USERS_EMPTY_ADDRESS);
        }
        if(postUserReq.getPassword() == null){ //비밀번호를 입력안했을 경우 리턴값
            return new BaseResponse<>(POST_USERS_EMPTY_PASSWORD);
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
            //jwt에서 id 추출.
            String userIdByJwt = jwtService.getUserId();
            System.out.println("userIdByJwt: " + userIdByJwt);
            //userId와 접근한 유저가 같은지 확인
            if(!userId.equals(userIdByJwt)){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            //주소 변경
            PatchUserReq patchUserReq1 = new PatchUserReq(userId, patchUserReq.getAddress());
            userService.modifyUserAddress(patchUserReq1);

            String result = "";
        return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }


//    /**
//     * 유저정보변경 API
//     * [PATCH] /users/:userIdx
//     * @return BaseResponse<String>
//     */
//    @ResponseBody
//    @PatchMapping("/{userId}")
//    public BaseResponse<String> modifyUserAddress(@PathVariable("userId") String userId, @RequestBody PatchUserReq patchUserReq){
//        try {
//            //jwt에서 idx 추출.
//            //int userIdxByJwt = jwtService.getUserIdx();
//            //userIdx와 접근한 유저가 같은지 확인
////            if(userIdx != userIdxByJwt){
////                return new BaseResponse<>(INVALID_USER_JWT);
////            }
//
//            //주소 변경
//            PatchUserReq patchUserReq1 = new PatchUserReq(userId, patchUserReq.getAddress());
//
//
//            userService.modifyUserAddress(patchUserReq1);
//
//            String result = "";
//            return new BaseResponse<>(result);
//        } catch (BaseException exception) {
//            return new BaseResponse<>((exception.getStatus()));
//        }
//    }

    /**
     * 회원이 주문한 주문 내역 영수증
     * @return
     */
    @GetMapping("{userId}/order/{orderId}")
    @ResponseBody
    public BaseResponse<GetReceiptRes> getReceiptResBaseResponse(@PathVariable("userId") String userId,
                                                                 @PathVariable("orderId") long orderId) throws Exception{
        if(userId == null){ //userId의 값이 없을떄
            return new BaseResponse<>(USERS_EMPTY_USER_ID);
        }

        //orderId의 값이 없을때는 어떻게 해야할까? long 형식은 null 값이 없는데,,

        try{
            GetReceiptRes getReceiptRes = userProvider.getReceiptRes(userId, orderId);
            return new BaseResponse<>(getReceiptRes);

        } catch(BaseException exception) {
            System.out.println("여기로 들어옴!!!!!!!");
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     *
     * 회원의 주문 내역 목록
     * @return
     */
    @GetMapping("{userId}/orderList")
    @ResponseBody
    public BaseResponse<List<GetOrderListRes>> getOrderListRes(@PathVariable("userId") String userId) throws BaseException {

        if(userId == null){ //userId의 값이 없을떄
            return new BaseResponse<>(USERS_EMPTY_USER_ID);
        }

        try {
            List<GetOrderListRes> getOrderListRes = userProvider.getOrderListRes(userId);
            return new BaseResponse<>(getOrderListRes);

        } catch(BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 회원의 배송 주소 목록 조회
     * @param userId
     * @return BaseResponse<List<GetUserAddressRes>>
     * @throws BaseException
     */
    @GetMapping("/{userId}/myAddressList")
    @ResponseBody
    public BaseResponse<List<GetUserAddressRes>> getUserAddressRes(@PathVariable("userId") String userId) throws BaseException {

        if (userId == null) { //userId의 값이 없을떄
            return new BaseResponse<>(USERS_EMPTY_USER_ID);
        }

        try {
            List<GetUserAddressRes> getUserAddressResList = userProvider.getUserAddressList(userId);
            return new BaseResponse<>(getUserAddressResList);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 회원의 주소 추가
     * @param userId
     * @param postUserAddressReq
     * @return BaseResponse<PostUserAddressRes>
     */
    @PostMapping("/{userId}/myAddressList/add")
    @ResponseBody
    public BaseResponse<PostUserAddressRes> addUserAddress(@PathVariable("userId") String userId,
                                               @RequestBody PostUserAddressReq postUserAddressReq) throws BaseException {

        if (userId == null) { //userId의 값이 없을떄
            return new BaseResponse<>(USERS_EMPTY_USER_ID);
        }
        if(postUserAddressReq.getRealAddress() == null || postUserAddressReq.getAddressName() == null){ //주소 입력이 안되었을때
            return new BaseResponse<>(POST_USERS_EMPTY_ADDRESS);
        }
        if(postUserAddressReq.getStatus() == null){ //status가 등록안되었을 경우
            return new BaseResponse<>(POST_USERS_EMPTY_STATUS);
        }

        try {
            PostUserAddressRes postUserAddressRes = userService.addUserAddress(postUserAddressReq, userId);
            return new BaseResponse<>(postUserAddressRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 주소 변경
     * @param userId
     * @param userId, addressId, PatchUserAddressNickNameReq
     * @return BaseResponse<String>
     */
    @PatchMapping("/{userId}/myAddressList/add/{addressId}")
    @ResponseBody
    public BaseResponse<String> modifyAddressNickname(@PathVariable("userId") String userId,
                                                      @PathVariable("addressId") long addressId,
                                                      @RequestBody PatchUserAddressNicknameReq patchUserAddressNicknameReq) throws BaseException {

        if (userId == null) { //userId의 값이 없을떄
            return new BaseResponse<>(USERS_EMPTY_USER_ID);
        }
        if(patchUserAddressNicknameReq.getAddressName() == null){
            return new BaseResponse<>(POST_USERS_EMPTY_ADDRESS);
        }

        //addressId 값이 없을때 예외 처리?
        try {
            PatchUserAddressNicknameReq patchUserAddressNicknameReq1 = new PatchUserAddressNicknameReq(userId, addressId, patchUserAddressNicknameReq.getAddressName());
            userService.modifyAddressNickname(patchUserAddressNicknameReq1);

            String result = "변경 완료하였습니다.";
            return new BaseResponse<>(result);

        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}

