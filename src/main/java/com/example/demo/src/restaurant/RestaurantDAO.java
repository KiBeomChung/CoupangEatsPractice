package com.example.demo.src.restaurant;

import com.example.demo.src.restaurant.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class RestaurantDAO {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

//    public List<GetRestaurantMenuRes> getRestaurantMenuById(long restaurant_Id) {
//        String getRestaurantMenuListById = "SELECT Restaurant.name, In_Restaurant_Category.categoryName, Food.foodName, Food.mostReviewed, Food.mostOrdered, Food.price, Food.foodIntroduction,\n" +
//                "Image.file,\n" +
//                "Favorite_Restraunt.status\n" +
//                "FROM Food INNER JOIN In_Restaurant_Category_With_Food ON Food.foodId = In_Restaurant_Category_With_Food.foodId\n" +
//                "INNER JOIN In_Restaurant_Category ON In_Restaurant_Category.categoryId = In_Restaurant_Category_With_Food.categoryId\n" +
//                "INNER JOIN Image ON Food.foodId = Image.foodId\n" +
//                "INNER JOIN Restaurant ON Restaurant.restaurantId = Food.restaurantId\n" +
//                "INNER JOIN Favorite_Restraunt ON Favorite_Restraunt.restaurantId = Restaurant.restaurantId\n" +
//                "WHERE Food.restaurantId = ?";
//        long GetRestaurantParam = restaurant_Id;
//        return this.jdbcTemplate.query(getRestaurantMenuListById,
//                (rs, rowNum) -> new GetRestaurantMenuRes(
//                        rs.getString("name"),
//                        rs.getString("categoryName"),
//                        rs.getString("foodName"),
//                        rs.getString("mostReviewed"),
//                        rs.getString("mostOrdered"),
//                        rs.getInt("price"),
//                        rs.getString("foodIntroduction"),
//                        rs.getString("file"),
//                        rs.getString("status")
//                ),
//                GetRestaurantParam
//        );
//    }

    public List<GetRestaurantMenuRes> getRestaurantMenuById(long restaurant_Id) {
        String getRestaurantMenuListById = "SELECT In_Restaurant_Category.categoryName, Food.foodName, Food.mostReviewed, Food.mostOrdered, Food.price, Food.foodIntroduction,\n" +
                "Image.file,\n" +
                "Favorite_Restraunt.status\n" +
                "FROM Food INNER JOIN In_Restaurant_Category_With_Food ON Food.foodId = In_Restaurant_Category_With_Food.foodId\n" +
                "INNER JOIN In_Restaurant_Category ON In_Restaurant_Category.categoryId = In_Restaurant_Category_With_Food.categoryId\n" +
                "INNER JOIN Image ON Food.foodId = Image.foodId\n" +
                "INNER JOIN Restaurant ON Restaurant.restaurantId = Food.restaurantId\n" +
                "INNER JOIN Favorite_Restraunt ON Favorite_Restraunt.restaurantId = Restaurant.restaurantId\n" +
                "WHERE Food.restaurantId = ?";
        long GetRestaurantParam = restaurant_Id;
        return this.jdbcTemplate.query(getRestaurantMenuListById,
                (rs, rowNum) -> new GetRestaurantMenuRes(
                        rs.getString("categoryName"),
                        rs.getString("foodName"),
                        rs.getString("mostReviewed"),
                        rs.getString("mostOrdered"),
                        rs.getInt("price"),
                        rs.getString("foodIntroduction"),
                        rs.getString("file"),
                        rs.getString("status")
                ),
                GetRestaurantParam
        );
    }

    public List<GetFavoriteRestaurantRes> getFavoriteRestaurant(String userId) {
        String getFavoriteRestaurantStr = "SELECT Restaurant.name,\n" +
                "       Restaurant.fastDelivery,\n" +
                "       Restaurant.reviewScore,\n" +
                "       Restaurant.deliveryFee,\n" +
                "       Restaurant.packaging,\n" +
                "       Image.file\n" +
                "FROM Restaurant_Main_Img\n" +
                "INNER JOIN Restaurant ON Restaurant_Main_Img.restaurantId = Restaurant.restaurantId\n" +
                "INNER JOIN Image ON Image.imgId = Restaurant_Main_Img.imgId\n" +
                "WHERE Restaurant_Main_Img.restaurantId IN\n" +
                "      (SELECT restaurantId FROM Favorite_Restraunt WHERE userId = ?\n" +
                "          ORDER BY deliveryNum DESC)";
        String GetUserIdParam = userId;
        return this.jdbcTemplate.query(getFavoriteRestaurantStr,
                (rs, rowNum) -> new GetFavoriteRestaurantRes(
                        rs.getString("name"),
                        rs.getString("fastDelivery"),
                        rs.getInt("reviewScore"),
                        rs.getInt("deliveryFee"),
                        rs.getString("packaging"),
                        rs.getString("file")
                ),
                GetUserIdParam
        );
    }

    public List<GetRestaurantListRes> getRestaurantList(String categoryName, int lowPrice, int highPrice) {
        String getRestaurantListStr = "SELECT Restaurant_Category.name as categoryName,\n" +
                "       Image.file as file,\n" +
                "       Restaurant.name as name,\n" +
                "       Restaurant.reviewScore as reviewScore,\n" +
                "       Restaurant.deliveryFee as deliveryFee,\n" +
                "       Restaurant.fastDelivery as fastDelivery,\n" +
                "       Restaurant.packaging as packaging\n" +
                "FROM Restaurant\n" +
                "INNER JOIN Restaurant_With_Category\n" +
                "    ON Restaurant.restaurantId = Restaurant_With_Category.restaurantId\n" +
                "INNER JOIN Restaurant_Main_Img\n" +
                "    ON Restaurant.restaurantId = Restaurant_Main_Img.restaurantId\n" +
                "INNER JOIN Image\n" +
                "ON Restaurant_Main_Img.imgId = Image.imgId\n" +
                "INNER JOIN Restaurant_Category\n" +
                "ON Restaurant_Category.categoryId =\n" +
                "   (SELECT Restaurant_Category.categoryId FROM Restaurant_Category WHERE Restaurant_Category.name =?)\n" +
                "WHERE Restaurant.deliveryFee BETWEEN ? AND ?\n" +
                "AND Restaurant_With_Category.categoryId =\n" +
                "           (SELECT Restaurant_Category.categoryId FROM Restaurant_Category WHERE Restaurant_Category.name =?)";
        String GetCategoryNameParam = categoryName;
        int GetLowPriceParam = lowPrice;
        int GetHighPriceParam = highPrice;
        return this.jdbcTemplate.query(getRestaurantListStr,
                (rs, rowNum) -> new GetRestaurantListRes(
                        rs.getString("categoryName"),
                        rs.getString("file"),
                        rs.getString("name"),
                        rs.getInt("reviewScore"),
                        rs.getInt("deliveryFee"),
                        rs.getString("fastDelivery"),
                        rs.getString("packaging")
                ),
                GetCategoryNameParam, GetLowPriceParam, GetHighPriceParam, GetCategoryNameParam
        );
    }

    public List<GetRestaurantReviewRes> getRestaurantReview(long restaurantId) {
        String getRestaurantReviewList = "SELECT Restaurant.name,\n" +
                "       Image.file,\n" +
                "       User.name as userName,\n" + //여기서 이름과
                "       Review.reviewScore,\n" +
                "       Review.reviewText,\n" +
                "       Food.foodName,\n" +
                "       Review.createdAt\n" +
                "FROM Review INNER JOIN User ON Review.userId = User.userId\n" +
                "INNER JOIN Order_Food ON Review.orderId = Order_Food.orderId\n" +
                "INNER JOIN Restaurant ON Order_Food.orderRestaurantId = Restaurant.restaurantId\n" +
                "AND Restaurant.restaurantId = ?\n" +
                "LEFT JOIN Review_Img ON Review.reviewId = Review_Img.reviewId\n" +
                "LEFT JOIN Image ON Review_Img.imgId = Image.imgId\n" +
                "INNER JOIN Order_Food_List ON Order_Food.orderId = Order_Food_List.orderId\n" +
                "INNER JOIN Food ON Order_Food_List.foodId = Food.foodId";
        long getRestaurantIdParam = restaurantId;
        return this.jdbcTemplate.query(getRestaurantReviewList,
                (rs, rowNum) -> new GetRestaurantReviewRes(
                        rs.getString("name"),
                        rs.getString("file"),
                        rs.getString("userName"), //여기서의 이름과 동일해야함함
                        rs.getInt("reviewScore"),
                        rs.getString("reviewText"),
                        rs.getString("foodName"),
                        rs.getTimestamp("createdAt")
                ),
                getRestaurantIdParam
        );
    }

    public List<GetFamousFranchiseRes> getFamousFranchiseList() {
        String getFamousFranchiseListQuery = "SELECT Image.file,\n" +
                "       Restaurant.name,\n" +
                "       Restaurant.fastDelivery,\n" +
                "       Restaurant.reviewScore,\n" +
                "       Restaurant.deliveryFee\n" +
                "FROM Restaurant\n" +
                "INNER JOIN Restaurant_Main_Img on Restaurant.restaurantId = Restaurant_Main_Img.restaurantId\n" +
                "INNER JOIN Image on Restaurant_Main_Img.imgId = Image.imgId\n" +
                "INNER JOIN Franchise ON Franchise.franchiseId = Restaurant.franchiseId";
        return this.jdbcTemplate.query(getFamousFranchiseListQuery,
                (rs, rowNum) -> new GetFamousFranchiseRes(
                        rs.getString("file"),
                        rs.getString("name"),
                        rs.getString("fastDelivery"),
                        rs.getInt("reviewScore"),
                        rs.getInt("deliveryFee"))
        );
    }

    public List<GetEatsOnlyMainRes> getEatsOnlyMain(String status) {
        String getEatsOnlyMainQuery = "SELECT Restaurant.eatsOnly AS eatsOnly,\n" +
                "       Image.file AS file,\n" +
                "       Restaurant.name AS name,\n" +
                "       Restaurant.reviewScore AS reviewScore,\n" +
                "       Restaurant.deliveryFee AS deliveryFee,\n" +
                "       Restaurant.packaging AS packaging\n" +
                "FROM Restaurant INNER JOIN Restaurant_Main_Img ON Restaurant.restaurantId = Restaurant_Main_Img.restaurantId\n" +
                "INNER JOIN Image ON Image.imgId = Restaurant_Main_Img.imgId\n" +
                "WHERE Restaurant.eatsOnly = ?";
        String getEatsOnlyMainParam = status;
        return this.jdbcTemplate.query(getEatsOnlyMainQuery,
                (rs, rowNum) -> new GetEatsOnlyMainRes(
                        rs.getString("eatsOnly"),
                        rs.getString("file"),
                        rs.getString("name"),
                        rs.getDouble("reviewScore"),
                        rs.getInt("deliveryFee"),
                        rs.getString("packaging")),
                        getEatsOnlyMainParam);
    }

    //event 기간이 지났는지 안지났는지 확인하고 status를 변경하는 메소드
    public void checkEventEndDate(){
        String checkEventEndDateQuery = "update Event set status  = 'deregister' where  endDate < ?";
        Timestamp dateNow = new Timestamp(System.currentTimeMillis());
        Object[] checkEventEndDateParam = new Object[]{dateNow};

        this.jdbcTemplate.update(checkEventEndDateQuery, checkEventEndDateParam);
    }

    public List<GetEventRes> getEventList() {

        checkEventEndDate();

        String getEventList = "SELECT Event.eventName, Image.file, Event.endDate, Event.status\n" +
                "FROM Event INNER JOIN Image ON Event.imgId = Image.imgId";
        return this.jdbcTemplate.query(getEventList,
                (rs, rowNum) -> new GetEventRes(
                        rs.getString("eventName"),
                        rs.getString("file"),
                        rs.getTimestamp("endDate"),
                        rs.getString("status"))
        );
    }

    public String GetRestaurantName(long restaurantId){
        String getRestaurantNameQuery = "SELECT name FROM Restaurant where restaurantId = ?";

        long getRestaurantNameParam = restaurantId;

        return this.jdbcTemplate.queryForObject(getRestaurantNameQuery, String.class, getRestaurantNameParam);
    }

    // 입력받은 status의 스펠링을 확인하는 메소드
    public boolean checkStatus(String status){
        if(status.equals("active") || status.equals("inactive")){
            return true;
        }

        return false;
    }

    //가게id 확인하는 메소드
    public int checkRestaurantId(long restaurantId){
        String checkRestaurantIdQuery = "select exists(select orderId from Order_Food where orderId = ?)";
        long checkRestaurantIdParam = restaurantId;
        return this.jdbcTemplate.queryForObject(checkRestaurantIdQuery, int.class, checkRestaurantIdParam);
    }

    public int checkCategoryName(String categoryName){
        String checkCategoryNameQuery = "select exists(select name from Restaurant_Category where name = ?";
        String checkCategoryParam = categoryName;
        return this.jdbcTemplate.queryForObject(checkCategoryNameQuery, int.class, checkCategoryParam);
    }

    public boolean checkDeliveryFee(int lowPrice, int highPrice){
        if(lowPrice < 0 || highPrice > 10000){
            return true;
        }
        else {
            return false;
        }
    }
}
