package com.example.demo.src.restaurant;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.restaurant.model.*;
import com.example.demo.src.user.UserService;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/app/restaurant")
public class RestaurantController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final RestaurantProvider restaurantProvider;
    @Autowired
    private final UserService restaurantService;
    @Autowired
    private final JwtService jwtService;

    public RestaurantController(RestaurantProvider restaurantProvider, UserService restaurantService, JwtService jwtService) {
        this.restaurantProvider = restaurantProvider;
        this.restaurantService = restaurantService;
        this.jwtService = jwtService;
    }

    /**
     * 이츠에만 있는 가게
     * @param status
     * @return BaseResponse<List<GetEatsOnlyMainRes>>
     * @throws BaseException
     */
    @ResponseBody
    @GetMapping("/EatsOnlyMain/{status}")
    public BaseResponse<List<GetEatsOnlyMainRes>> getEatsOnlyMain(@PathVariable("status") String status)
            throws BaseException {

        List<GetEatsOnlyMainRes> getEatsOnlyMainRes = restaurantProvider.getEatsOnlyMain(status);
        return new BaseResponse<>(getEatsOnlyMainRes);
    }

    /**
     * 진행중인 이벤트 목록
     * validation은 따로 필요없을듯?,,
     * @return aseResponse<List<GetEventRes>>
     * @throws BaseException
     */
    @GetMapping("/eventList")
    @ResponseBody
    public BaseResponse<List<GetEventRes>> getEventList() throws BaseException{

        //List<GetEventRes> checkEventEndDate = restaurantProvider.checkEventEndDate();

        List<GetEventRes> getEventRes = restaurantProvider.getEventRes();
        return new BaseResponse<>(getEventRes);
    }

    /**
     * 가게의 메뉴들
     * @param restaurant_Id
     * @return BaseResponse<List<GetRestaurantMenuRes>>
     * @throws BaseException
     */
    @GetMapping("/{restaurant_Id}")
    @ResponseBody
    public BaseResponse<GetRestaurantMenuRes2> getRestaurantMenuBase(@PathVariable("restaurant_Id") long restaurant_Id)  throws BaseException{

        try {
            GetRestaurantMenuRes2 getRestaurantMenuRes = restaurantProvider.getRestaurantMenu(restaurant_Id);
            return new BaseResponse<>(getRestaurantMenuRes);
        }     catch(BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 회원의 즐겨찾기 가게
     * @param userId
     * @return BaseResponse<List<GetFavoriteRestaurantRes>>
     * @throws BaseException
     */
    @GetMapping("/{userId}/favoriteRestaurant")
    @ResponseBody
    public BaseResponse<List<GetFavoriteRestaurantRes>> getFavoriteRestaurantList(@PathVariable("userId") String userId) throws BaseException{

        try {
            String userIdByJwt = jwtService.getUserId();
            if(!userId.equals(userIdByJwt)){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            List<GetFavoriteRestaurantRes> getFavoriteRestaurantResList = restaurantProvider.getFavoriteRestaurant(userId);
            return new BaseResponse<>(getFavoriteRestaurantResList);
        } catch(BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 음식 카테고리와 배송비에 따른 가게 리스트
     * @param categoryName
     * @param lowPrice
     * @param highPrice
     * @return BaseResponse<List<GetRestaurantListRes>>
     */
    @GetMapping("/category")
    @ResponseBody
    public BaseResponse<List<GetRestaurantListRes>> getRestaurantList(@RequestParam(value = "categoryName") String categoryName,
                                                                      @RequestParam(value = "lowPrice") int lowPrice,
                                                                      @RequestParam(value = "highPrice") int highPrice) throws BaseException {
        // categoryName ~ highPrice 까지 아예 값이 들어오지 않았을 경우 예외처리 해야함
        System.out.println(categoryName);

        try {
            List<GetRestaurantListRes> getRestaurantListRes = restaurantProvider.getRestaurantList(categoryName, lowPrice, highPrice);
            return new BaseResponse<>(getRestaurantListRes);
        } catch(BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 가게의 전체 리뷰 목록 조회
     * @param restaurantId
     * @return BaseResponse<List<GetRestaurantReviewRes>>
     */
    @GetMapping("/{restaurantId}/reviewList")
    @ResponseBody
    public BaseResponse<List<GetRestaurantReviewRes>> getRestaurantReviewRes(@PathVariable("restaurantId") long restaurantId){

        //restaurantId 값이 아예 없는 경우 예외 추가

        try {
            List<GetRestaurantReviewRes> getRestaurantReviewResList = restaurantProvider.getRestaurantReviewList(restaurantId);
            return new BaseResponse<>(getRestaurantReviewResList);
        } catch(BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 인기있는 프랜차이즈 목록
     * @return BaseResponse<List<GetFamousFranchiseRes>>
     */
    @GetMapping("/franchise")
    @ResponseBody
    public BaseResponse<List<GetFamousFranchiseRes>> getFamousFranchiseRes(){
        List<GetFamousFranchiseRes> getFamousFranchiseResList = restaurantProvider.getFamousFranchiseResList();
        return new BaseResponse<>(getFamousFranchiseResList);
    }
}
