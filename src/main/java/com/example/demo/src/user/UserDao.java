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
                        rs.getString("userId"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("phone_Num"),
                        rs.getString("status"),
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
                        rs.getString("status"),
                        rs.getTimestamp("createdAt"),
                        rs.getTimestamp("updatedAt"),
                        rs.getString("address")),
                getUsersByNameParams);
    }

    public GetUserRes getUser(String userId) {
        String getUserQuery = "select * from User where userId = ?";
        String getUserParams = userId;
        return this.jdbcTemplate.queryForObject(getUserQuery,
                (rs, rowNum) -> new GetUserRes(
                        rs.getString("userId"),
                        rs.getString("name"),
                        rs.getString("password"),
                        rs.getString("phone_Num"),
                        rs.getString("status"),
                        rs.getTimestamp("createdAt"),
                        rs.getTimestamp("updatedAt"),
                        rs.getString("address")),
                getUserParams);
    }


    public String createUser(PostUserReq postUserReq) {
        String createUserQuery = "insert into User (userId, name, password, phone_Num, address, userIdx) VALUES (?,?,?,?,?,?)";
        Object[] createUserParams = new Object[]{postUserReq.getUserId(), postUserReq.getName(), postUserReq.getPassword(), postUserReq.getPhone_Num(),
                postUserReq.getAddress(), postUserReq.getUserIdx()};
        this.jdbcTemplate.update(createUserQuery, createUserParams); // 데이터베이스에 저장

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

    //유저 id가 존재하는지 확인하는 메소드
    public int checkUserId(String userId){
        String checkUserIdQuery ="select exists(select userId from User where userId = ?)";
        String checkUserParam = userId;
        return this.jdbcTemplate.queryForObject(checkUserIdQuery, int.class, checkUserParam);
    }

    //주문번호 확인하는 메소드
    public int checkOrderId(long orderId){
        String checkOrderIdQuery = "select exists(select orderId from Order_Food where orderId = ?)";
        long checkOrderIdParam = orderId;
        return this.jdbcTemplate.queryForObject(checkOrderIdQuery, int.class, checkOrderIdParam);
    }

    // 중복된 주소가 있는지 확인하는 메소드
    public int isMultipleAddress(String address, String userId){
        String isMultipleAddressQuery = "select exists(select userId from address where realAddress = ? and userId = ?)";
        String checkAddressParam = address;
        String checkUserIdParam = userId;
        return this.jdbcTemplate.queryForObject(isMultipleAddressQuery, int.class, checkAddressParam, checkUserIdParam);
    }

    // 해당 addrssId가 있는지 확인하는 메소드
    public int checkAddressId(long addressId){
        String checkAddressIdQuery = "select exists(select addressId from address where addressId = ?)";
        long checkAddressIdParam = addressId;
        return this.jdbcTemplate.queryForObject(checkAddressIdQuery, int.class, checkAddressIdParam);
    }

    // 주소의 닉네임을 변경하는 메소드
    public int modifyUserAddress(PatchUserReq patchUserReq) {

        String modifyUserAddressQuery = "update User set address = ? where userId = ? ";
        Object[] modifyUserAddressParams = new Object[]{patchUserReq.getAddress(), patchUserReq.getUserId()};

        return this.jdbcTemplate.update(modifyUserAddressQuery, modifyUserAddressParams);
    }

//    public User getPwd(PostLoginReq postLoginReq) {
//        String getPwdQuery = "select userIdx, password,email,userName,ID from UserInfo where ID = ?";
//        String getPwdParams = postLoginReq.getId();
//
//        return this.jdbcTemplate.queryForObject(getPwdQuery,
//                (rs, rowNum) -> new User(
//                        rs.getInt("userIdx"),
//                        rs.getString("ID"),
//                        rs.getString("userName"),
//                        rs.getString("password"),
//                        rs.getString("email")
//                ),
//                getPwdParams
//        );
//
//    }

    public User getPwd(PostLoginReq postLoginReq) {
        String getPwdQuery = "select userId, password, name, phone_Num, userIdx from User where userId = ?";
        String getPwdParams = postLoginReq.getId();

        System.out.println(getPwdParams);

        return this.jdbcTemplate.queryForObject(getPwdQuery,
                (rs, rowNum) -> new User(
                        rs.getString("userId"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("phone_Num"),
                        rs.getInt("userIdx")
                ),
                getPwdParams
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

    public List<GetOrderListRes> getOrderList(String userId) {
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

    public List<GetUserAddressRes> getUserAddressList(String userId) {
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

    public String addUserAddress(PostUserAddressReq postUserAddressReq, String userId) {
        String addUserAddressQuery = "insert into address (addressName, userId, status, realAddress) VALUES(?,?,?,?)";
        Object[] addUserAddressParam = new Object[]{postUserAddressReq.getAddressName(), postUserAddressReq.getUserId(),
                postUserAddressReq.getStatus(), postUserAddressReq.getRealAddress()};
        this.jdbcTemplate.update(addUserAddressQuery, addUserAddressParam);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, String.class);
    }

    public int modifyAddressNickname(PatchUserAddressNicknameReq patchUserAddressNicknameReq){

        String modifyAddressNicknameQuery = "update address set addressName = ? where userId = ? and addressId = ?";
        Object[] modifyAddressNicknameParam = new Object[]{patchUserAddressNicknameReq.getAddressName(), patchUserAddressNicknameReq.getUserId(),
        patchUserAddressNicknameReq.getAddressId()};
        return this.jdbcTemplate.update(modifyAddressNicknameQuery, modifyAddressNicknameParam);
    }
}

