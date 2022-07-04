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
     */
    @ResponseBody
    @GetMapping("/EatsOnlyMain")
    public BaseResponse<List<GetEatsOnlyMainRes>> getEatsOnlyMain() throws BaseException {
        List<GetEatsOnlyMainRes> getEatsOnlyMainRes = restaurantProvider.getEatsOnlyMain();
        return new BaseResponse<>(getEatsOnlyMainRes);
    }

    /**
     * 진행중인 이벤트 목록
     */
    @GetMapping("/eventList")
    @ResponseBody
    public BaseResponse<List<GetEventRes>> getEventList() throws BaseException{
        List<GetEventRes> getEventRes = restaurantProvider.getEventRes();
        return new BaseResponse<>(getEventRes);
    }

    /**
     * 가게의 메뉴들
     */
    @GetMapping("/{restaurant_Id}")
    @ResponseBody
    public BaseResponse<List<GetRestaurantMenuRes>> getRestaurantMenuBase(@PathVariable("restaurant_Id") long restaurant_Id)  throws BaseException{
        List<GetRestaurantMenuRes> getRestaurantMenuRes = restaurantProvider.getRestaurantMenu(restaurant_Id);
        return new BaseResponse<>(getRestaurantMenuRes);
    }

    /**
     * 회원의 즐겨찾기 가게
     */
    @GetMapping("/{user_Id}/favoriteRestaurant")
    @ResponseBody
    public BaseResponse<List<GetFavoriteRestaurantRes>> getFavoriteRestaurantList(@PathVariable("user_Id") String user_Id) throws BaseException{
        List<GetFavoriteRestaurantRes> getFavoriteRestaurantResList = restaurantProvider.getFavoriteRestaurant(user_Id);
        return new BaseResponse<>(getFavoriteRestaurantResList);
    }

    /**
     * 음식 카테고리에 따른 가게 리스트
     */
    @GetMapping("/category")
    @ResponseBody
    public BaseResponse<List<GetRestaurantListRes>> getRestaurantList(@RequestParam(value = "category_Name") String category_Name,
                                                                      @RequestParam(value = "lowPrice") int lowPrice,
                                                                      @RequestParam(value = "highPrice") int highPrice){
        System.out.println(category_Name);
        List<GetRestaurantListRes> getRestaurantListRes = restaurantProvider.getRestaurantList(category_Name, lowPrice, highPrice);
        return new BaseResponse<>(getRestaurantListRes);
    }

    /**
     * 식당의 리뷰 목록
     */
    @GetMapping("/{restaurantId}/reviewList")
    @ResponseBody
    public BaseResponse<List<GetRestaurantReviewRes>> getRestaurantReviewRes(@PathVariable("restaurantId") long restaurantId){
        List<GetRestaurantReviewRes> getRestaurantReviewResList = restaurantProvider.getRestaurantReviewList(restaurantId);
        return new BaseResponse<>(getRestaurantReviewResList);
    }

    /**
     * 인기있는 프랜차이즈 목록
     * @return
     */
    @GetMapping("/franchise")
    @ResponseBody
    public BaseResponse<List<GetFamousFranchiseRes>> getFamousFranchiseRes(){
        List<GetFamousFranchiseRes> getFamousFranchiseResList = restaurantProvider.getFamousFranchiseResList();
        return new BaseResponse<>(getFamousFranchiseResList);
    }
}
