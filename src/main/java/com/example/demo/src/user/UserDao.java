package com.example.demo.src.user;


import com.example.demo.src.user.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class UserDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetUserRes> getUsers() {
        String getUsersQuery = "select * from User";
        return this.jdbcTemplate.query(getUsersQuery,
                (rs, rowNum) -> new GetUserRes(
                        rs.getString("user_Id"),
                        rs.getString("name"),
                        rs.getString("password"),
                        rs.getString("phone_Num"),
                        rs.getBoolean("status"),
                        rs.getTimestamp("createdAt"),
                        rs.getTimestamp("updatedAt"),
                        rs.getString("address"))
        );
    }

    public List<GetUserRes> getUsersByName(String name) {
        String getUsersByNameQuery = "select * from User where name =?";
        String getUsersByNameParams = name;
        return this.jdbcTemplate.query(getUsersByNameQuery,
                (rs, rowNum) -> new GetUserRes(
                        rs.getString("user_Id"),
                        rs.getString("name"),
                        rs.getString("password"),
                        rs.getString("phone_Num"),
                        rs.getBoolean("status"),
                        rs.getTimestamp("createdAt"),
                        rs.getTimestamp("updatedAt"),
                        rs.getString("address")),
                getUsersByNameParams);
    }

    public GetUserRes getUser(String user_Id) {
        String getUserQuery = "select * from User where user_Id = ?";
        String getUserParams = user_Id;
        return this.jdbcTemplate.queryForObject(getUserQuery,
                (rs, rowNum) -> new GetUserRes(
                        rs.getString("user_Id"),
                        rs.getString("name"),
                        rs.getString("password"),
                        rs.getString("phone_Num"),
                        rs.getBoolean("status"),
                        rs.getTimestamp("createdAt"),
                        rs.getTimestamp("updatedAt"),
                        rs.getString("address")),
                getUserParams);
    }


    public String createUser(PostUserReq postUserReq) {
        String createUserQuery = "insert into User (userId, name, password, phone_Num, address) VALUES (?,?,?,?,?)";
        Object[] createUserParams = new Object[]{postUserReq.getUserId(), postUserReq.getName(), postUserReq.getPassword(), postUserReq.getPhone_Num(),
                postUserReq.getAddress()};
        this.jdbcTemplate.update(createUserQuery, createUserParams); // 데이터베이스에 저장

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, String.class);
    }

    public String writeReview(PostUserReviewReq postUserReviewReq, String userId, long orderId){
        String getUserId = userId;
        String writeReviewQuery = "insert into Review (userId, reviewText, reviewScore, orderId) VALUES (?,?,?,?)";
        Object[] writeReviewParams = new Object[]{postUserReviewReq.getUserId(), postUserReviewReq.getReviewText(),
        postUserReviewReq.getReviewScore(), postUserReviewReq.getOrderId()};
        this.jdbcTemplate.update(writeReviewQuery, writeReviewParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, String.class);
    }

    public int checkEmail(String email) {
        String checkEmailQuery = "select exists(select email from UserInfo where email = ?)";
        String checkEmailParams = email;
        return this.jdbcTemplate.queryForObject(checkEmailQuery,
                int.class,
                checkEmailParams);

    }

    public int modifyUserAddress(PatchUserReq patchUserReq) {

        String modifyUserAddressQuery = "update User set address = ? where userId = ? ";
        Object[] modifyUserAddressParams = new Object[]{patchUserReq.getAddress(), patchUserReq.getUserId()};

        return this.jdbcTemplate.update(modifyUserAddressQuery, modifyUserAddressParams);
    }

    public User getPwd(PostLoginReq postLoginReq) {
        String getPwdQuery = "select userIdx, password,email,userName,ID from UserInfo where ID = ?";
        String getPwdParams = postLoginReq.getId();

        return this.jdbcTemplate.queryForObject(getPwdQuery,
                (rs, rowNum) -> new User(
                        rs.getInt("userIdx"),
                        rs.getString("ID"),
                        rs.getString("userName"),
                        rs.getString("password"),
                        rs.getString("email")
                ),
                getPwdParams
        );

    }

    public List getEatsOnlyMain() {
        String getEatsOnlyMain = "SELECT Restaurant.eatsOnly AS eatsOnly,\n" +
                "       Image.file AS file,\n" +
                "       Restaurant.name AS name,\n" +
                "       Restaurant.reviewScore AS reviewScore,\n" +
                "       Restaurant.deliveryFee AS deliveryFee,\n" +
                "       Restaurant.packaging AS packaging\n" +
                "FROM Restaurant INNER JOIN Restaurant_Main_Img ON Restaurant.restaurantId = Restaurant_Main_Img.restaurantId\n" +
                "INNER JOIN Image ON Image.imgId = Restaurant_Main_Img.imgId\n" +
                "WHERE Restaurant.eatsOnly = 'active'";
        return this.jdbcTemplate.query(getEatsOnlyMain,
                (rs, rowNum) -> new GetEatsOnlyMainRes(
                        rs.getString("eatsOnly"),
                        rs.getString("file"),
                        rs.getString("name"),
                        rs.getDouble("reviewScore"),
                        rs.getInt("deliveryFee"),
                        rs.getString("packaging"))
        );
    }

    public List<GetEventRes> getEventList() {
        String getEventList = "SELECT Event.eventName, Image.file, Event.endDate, Event.status\n" +
                "FROM Event INNER JOIN Image ON Event.imgId = Image.imgId";
        return this.jdbcTemplate.query(getEventList,
                (rs, rowNum) -> new GetEventRes(
                        rs.getString("eventName"),
                        rs.getString("file"),
                        rs.getString("endDate"),
                        rs.getString("status"))
        );
    }

    public List getRestaurantMenuById(long restaurant_Id) {
        String getRestaurantMenuListById = "SELECT Restaurant.name, In_Restaurant_Category.categoryName, Food.foodName, Food.mostReviewed, Food.mostOrdered, Food.price, Food.foodIntroduction,\n" +
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
                        rs.getString("name"),
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

    public List getFavoriteRestaurant(String user_Id) {
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
        String GetUserIdParam = user_Id;
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

    public List<GetRestaurantListRes> getRestaurantList(String category_Name, int lowPrice, int highPrice) {
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
        String GetCategoryNameParam = category_Name;
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

    public GetReceiptRes getReceipt(String userId, long orderId) {
        String getReceiptStr = "SELECT Restaurant.name,\n" +
                "       Order_Food.orderId,\n" +
                "       Order_Food.createdAt,\n" +
                "       Food.price,\n" +
                "       Restaurant.deliveryFee,\n" +
                "       User_Coupay.changes,\n" +
                "       SUM(Food.price + Restaurant.deliveryFee - User_Coupay.changes) AS sum,\n" +
                "       SUM(CASE WHEN Order_Food.status = 'active' THEN Food.price + Restaurant.deliveryFee - User_Coupay.changes ELSE 0 END)\n" +
                "       AS paySum,\n" +
                "       User_Credit_Card.cardName,\n" +
                "       address.realAddress,\n" +
                "       User_Credit_Card.cardNum\n" +
                "FROM Order_Food INNER JOIN Order_Food_List on Order_Food.orderId = Order_Food_List.orderId\n" +
                "INNER JOIN Restaurant ON Order_Food.orderRestaurantId = Restaurant.restaurantId\n" +
                "INNER JOIN Food ON Food.foodId = Order_Food_List.foodId\n" +
                "INNER JOIN User ON Order_Food.userId = User.userId\n" +
                "    AND User.userId = ? AND Order_Food.orderId = ?\n" +
                "INNER JOIN User_Coupay ON User.userId = User_Coupay.userId\n" +
                "INNER JOIN User_Credit_Card ON User.userId = User_Credit_Card.userId AND User_Credit_Card.status = 'active'\n" +
                "INNER JOIN address on User.userId = address.userId AND address.status = 'active'";
        String getUserIdParam = userId;
        long getOrderIdParam = orderId;
        return this.jdbcTemplate.queryForObject(getReceiptStr,
                (rs, rowNum) -> new GetReceiptRes(
                        rs.getString("name"),
                        rs.getLong("orderId"),
                        rs.getTimestamp("createdAt"),
                        rs.getInt("price"),
                        rs.getInt("deliveryFee"),
                        rs.getInt("changes"),
                        rs.getInt("sum"),
                        rs.getInt("paySum"),
                        rs.getString("cardName"),
                        rs.getString("realAddress"),
                        rs.getString("cardNum")
                ),
                getUserIdParam, getOrderIdParam);
    }

    public List<GetOrderListRes> getOrderList(String userId){
        String getOrderListQuery = "SELECT Restaurant.name,\n" +
                "       Order_Food.createdAt,\n" +
                "       Order_Food.status,\n" +
                "       Food.foodName,\n" +
                "       Image.file,\n" +
                "       Order_Food.totalPrice\n" +
                "FROM Order_Food INNER JOIN User on Order_Food.userId = User.userId\n" +
                "INNER JOIN Order_Food_List ON Order_Food.orderId = Order_Food_List.orderId\n" +
                "INNER JOIN Restaurant ON Order_Food.orderRestaurantId = Restaurant.restaurantId\n" +
                "INNER JOIN Food ON Order_Food_List.foodId = Food.foodId\n" +
                "INNER JOIN Restaurant_Main_Img ON Restaurant.restaurantId = Restaurant_Main_Img.restaurantId\n" +
                "INNER JOIN Image ON Restaurant_Main_Img.imgId = Image.imgId AND Restaurant_Main_Img.priority = 1\n" +
                "WHERE User.userId =?";
        String getUserId = userId;
        return this.jdbcTemplate.query(getOrderListQuery,
                (rs, rowNum) -> new GetOrderListRes(
                        rs.getString("name"),
                        rs.getTimestamp("createdAt"),
                        rs.getString("status"),
                        rs.getString("foodName"),
                        rs.getString("file"),
                        rs.getInt("totalPrice")
                ),
                getUserId);
    }

    public GetUserReviewRes getUserReview(String userId, long orderId){
        String getUserReviewQuery = "SELECT Restaurant.name,\n" +
                "       Review.reviewScore,\n" +
                "       Review.reviewText,\n" +
                "       Review.createdAt,\n" +
                "       Food.foodName\n" +
                "FROM Review INNER JOIN Order_Food_List ON Review.orderId = Order_Food_List.orderId\n" +
                "INNER JOIN Food ON Food.foodId = Order_Food_List.foodId\n" +
                "INNER JOIN Order_Food ON Order_Food.orderId = Order_Food_List.orderId\n" +
                "AND Order_Food.orderId = ?\n" +
                    "INNER JOIN Restaurant ON Order_Food.orderRestaurantId = Restaurant.restaurantId\n" +
                "WHERE Review.userId = ?";
        String getUserId = userId;
        long getOrderId = orderId;
        return this.jdbcTemplate.queryForObject(getUserReviewQuery,
                (rs, rowNum) -> new GetUserReviewRes(
                        rs.getString("name"),
                        rs.getInt("reviewScore"),
                        rs.getString("reviewText"),
                        rs.getTimestamp("createdAt"),
                        rs.getString("foodName")
                ),
                getOrderId, getUserId);
    }

    public int modifyUserReview(PatchUserReviewReq patchUserReviewReq){

        String modifyUserReviewQuery = "update Review set reviewText = ?, updatedAt = ? where orderId = ?";
        Object[] modifyUserReviewParams = new Object[]{patchUserReviewReq.getReviewText(), patchUserReviewReq.getOrderedAt(), patchUserReviewReq.getOrderId()};

        return this.jdbcTemplate.update(modifyUserReviewQuery, modifyUserReviewParams);
    }

    public int deleteUserReview(PatchUserReviewDeleteReq patchUserReviewDeleteReq){

        String deleteUserReviewQuery = "update Review set status = ? where reviewId = ? AND UserId = ?";
        Object[] deleteUserReviewParam = new Object[]{patchUserReviewDeleteReq.getStatus(), patchUserReviewDeleteReq.getReviewId(), patchUserReviewDeleteReq.getUserId()};

        return this.jdbcTemplate.update(deleteUserReviewQuery, deleteUserReviewParam);
    }

    public List<GetFamousFranchiseRes> getFamousFranchiseList(){
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

    public List<GetUserAddressRes> getUserAddressList(String userId){
        String getUserAddressQuery = "SELECT address.addressName, address.realAddress, address.status\n" +
                "FROM address WHERE userId = ?";
        String getUserId = userId;
        return this.jdbcTemplate.query(getUserAddressQuery,
                (rs, rowNum) -> new GetUserAddressRes(
                        rs.getString("addressName"),
                        rs.getString("realAddress"),
                        rs.getString("status")
                ),
                getUserId);
    }
}
