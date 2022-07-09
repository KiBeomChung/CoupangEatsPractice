package com.example.demo.utils;


import com.example.demo.config.BaseException;
import com.example.demo.config.secret.Secret;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class JwtService {

    /*
    JWT 생성
    @param userIdx
    @return String
     */
//    public String createJwt(int userIdx){
//        Date now = new Date();
//        return Jwts.builder()
//                .setHeaderParam("type","jwt")
//                .claim("userIdx",userIdx)
//                .setIssuedAt(now)
//                .setExpiration(new Date(System.currentTimeMillis()+1*(1000*60*60*24*365)))
//                .signWith(SignatureAlgorithm.HS256, Secret.JWT_SECRET_KEY)
//                .compact();
//    }

    public String createJwt(String userId){
        Date now = new Date();
        return Jwts.builder()
                .setHeaderParam("type","jwt")
                .claim("userId",userId)
                .setIssuedAt(now)
                .setExpiration(new Date(System.currentTimeMillis()+1*(1000*60*60*24*365)))
                .signWith(SignatureAlgorithm.HS256, Secret.JWT_SECRET_KEY)
                .compact();
    }

    /*
    Header에서 X-ACCESS-TOKEN 으로 JWT 추출
    @return String
     */
    public String getJwt(){
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
        // 쿠키, 헤더 등의 정보를 request 변수에 저장하여 받아올 수 있다.
        return request.getHeader("X-ACCESS-TOKEN");
        // request에 저장되 값중에 Header에 있는 값만 가져온다. 그리고 그 헤더의 값을 X-ACCESS_TOKEN 이라고 부르기로함.
    }

    /*
    JWT에서 userId 추출
    @return String
    @throws BaseException
     */
    public  String getUserId() throws BaseException{
        //1. JWT 추출
        String accessToken = getJwt(); // 헤더의 값이 accessToken 안에 저장
        System.out.println("accessToken: " + accessToken);
        if(accessToken == null || accessToken.length() == 0){ // 헤더에 아무것도 안들어있을 경우 예외처리
            throw new BaseException(EMPTY_JWT);
        } //일단 jwt 추출까지는 정상적으로 이루어짐

        // 2. JWT parsing
        Jws<Claims> claims; //클레임 선언
        try{
            claims = Jwts.parser()
                    .setSigningKey(Secret.JWT_SECRET_KEY) //비밀키 이용해서 복호화
                    .parseClaimsJws(accessToken);
        } catch (Exception ignored) {
            throw new BaseException(INVALID_JWT);
        }
        System.out.println("claims: " + claims);
        // 3. userId 추출
        System.out.println(claims.getBody());
        System.out.println(claims.getBody().get("userId", String.class));

        return claims.getBody().get("userId", String.class);
    }


//    public int getUserIdx() throws BaseException{
//        //1. JWT 추출
//        String accessToken = getJwt();
//        if(accessToken == null || accessToken.length() == 0){
//            throw new BaseException(EMPTY_JWT);
//        }
//
//        // 2. JWT parsing
//        Jws<Claims> claims;
//        try{
//            claims = Jwts.parser()
//                    .setSigningKey(Secret.JWT_SECRET_KEY)
//                    .parseClaimsJws(accessToken);
//        } catch (Exception ignored) {
//            throw new BaseException(INVALID_JWT);
//        }
//
//        // 3. userIdx 추출
//        return claims.getBody().get("userIdx",Integer.class);
//    }

}
