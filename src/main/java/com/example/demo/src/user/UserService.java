package com.example.demo.src.user;



import com.example.demo.config.BaseException;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.AES128;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;
import static org.apache.tomcat.util.net.openssl.ciphers.Encryption.AES128;

// Service Create, Update, Delete 의 로직 처리
@Service
public class UserService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserDao userDao;
    private final UserProvider userProvider;
    private final JwtService jwtService;


    @Autowired
    public UserService(UserDao userDao, UserProvider userProvider, JwtService jwtService) {
        this.userDao = userDao;
        this.userProvider = userProvider;
        this.jwtService = jwtService;

    }

    //POST
    public PostUserRes createUser(PostUserReq postUserReq) throws BaseException {
        //중복
//        if(userProvider.checkEmail(postUserReq.getEmail()) ==1){
//            throw new BaseException(POST_USERS_EXISTS_EMAIL);
//        }

        String pwd;
        try{
            //암호화
            pwd = new AES128(Secret.USER_INFO_PASSWORD_KEY).encrypt(postUserReq.getPassword());
            postUserReq.setPassword(pwd);
        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }

        try{
            String user_Id = userDao.createUser(postUserReq);
            //jwt 발급.
           // String jwt = jwtService.createJwt(userIdx);
            return new PostUserRes(user_Id);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyUserAddress(PatchUserReq patchUserReq) throws BaseException {
       // try{
            int result = userDao.modifyUserAddress(patchUserReq);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_USER_ADDRESS);
            }
//        } catch(Exception exception){
//            throw new BaseException(DATABASE_ERROR);
//        }
    }

    public PostUserAddressRes addUserAddress(PostUserAddressReq postUserAddressReq, String userId) throws BaseException {

        // 중복된 주소가 있는지 확인하는 validation
        if(userDao.isMultipleAddress(postUserAddressReq.getRealAddress(), userId) != 0){
            throw new BaseException(DUPLICATED_ADDRESS);
        }
        if(userDao.checkUserId(userId) == 0){ // db에 입력받은 userId가 존재하는지 확인
            throw new BaseException(NOT_EXISTS_USERID);
        }

        String user_Id = userDao.addUserAddress(postUserAddressReq, userId);
        return new PostUserAddressRes(user_Id);
    }

    public void modifyAddressNickname(PatchUserAddressNicknameReq patchUserAddressNicknameReq) throws BaseException {

        if(userDao.checkUserId(patchUserAddressNicknameReq.getUserId()) == 0){ // db에 입력받은 userId가 존재하는지 확인
            throw new BaseException(NOT_EXISTS_USERID);
        }

        userDao.modifyAddressNickname(patchUserAddressNicknameReq);
    }


}
