package com.example.demo.src.review;

import com.example.demo.src.review.model.GetUserReviewRes;
import com.example.demo.src.review.model.PatchUserReviewDeleteReq;
import com.example.demo.src.review.model.PatchUserReviewReq;
import com.example.demo.src.review.model.PostUserReviewReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class ReviewDAO {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public String writeReview(PostUserReviewReq postUserReviewReq, String userId, long orderId) {
        String getUserId = userId;
        String writeReviewQuery = "insert into Review (userId, reviewText, reviewScore, orderId) VALUES (?,?,?,?)";
        Object[] writeReviewParams = new Object[]{postUserReviewReq.getUserId(), postUserReviewReq.getReviewText(),
                postUserReviewReq.getReviewScore(), postUserReviewReq.getOrderId()};
        this.jdbcTemplate.update(writeReviewQuery, writeReviewParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, String.class);
    }

    public GetUserReviewRes getUserReview(String userId, long orderId) {
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

    public int modifyUserReview(PatchUserReviewReq patchUserReviewReq) {

        String modifyUserReviewQuery = "update Review set reviewText = ?, updatedAt = ? where orderId = ?";
        Object[] modifyUserReviewParams = new Object[]{patchUserReviewReq.getReviewText(), patchUserReviewReq.getOrderedAt(), patchUserReviewReq.getOrderId()};

        return this.jdbcTemplate.update(modifyUserReviewQuery, modifyUserReviewParams);
    }

    public int deleteUserReview(PatchUserReviewDeleteReq patchUserReviewDeleteReq) {

        String deleteUserReviewQuery = "update Review set status = ? where reviewId = ? AND UserId = ?";
        Object[] deleteUserReviewParam = new Object[]{patchUserReviewDeleteReq.getStatus(), patchUserReviewDeleteReq.getReviewId(), patchUserReviewDeleteReq.getUserId()};

        return this.jdbcTemplate.update(deleteUserReviewQuery, deleteUserReviewParam);
    }

}
