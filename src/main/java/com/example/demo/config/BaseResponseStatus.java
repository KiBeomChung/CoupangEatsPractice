package com.example.demo.config;

import lombok.Getter;

/**
 * 에러 코드 관리
 */
@Getter
public enum BaseResponseStatus {
    /**
     * 1000 : 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),


    /**
     * 2000 : Request 오류
     */
    // Common
    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),
    EMPTY_JWT(false, 2001, "JWT를 입력해주세요."),
    INVALID_JWT(false, 2002, "유효하지 않은 JWT입니다."),
    INVALID_USER_JWT(false,2003,"권한이 없는 유저의 접근입니다."),
    INVALID_REVIEW_SCORE(false, 2009, "별점이 유효한 값이 아닙니다."),
    INVALID_DELIVERY_FEE(false, 2060, "배달비 설정값이 유효하지 않습니다."),
    NOT_EXISTS_USERID(false,2004,"해당하는 id가 없습니다."),
    NOT_EXISTS_ORDERID(false,2005, "해당하는 주문 번호가 없습니다."),
    NOT_EXISTS_ADDRESID(false, 2006, "해당하는 주소 id가 없습니다."),
    NOT_EXISTS_REVIEW_ID(false, 2007,"해당하는 리뷰 id가 없습니다."),
    NOT_EXISTS_RESTAURANT_ID(false, 2050,"해당하는 가게 id가 없습니다."),
    NOT_EXISTS_RESTAURANT_CATEGORY(false, 2051, "해당하는 카테고리가 없습니다."),
    WRONG_STATUS(false, 2008, "status를 다시 한번 확인하세요."),

    // users
    USERS_EMPTY_USER_ID(false, 2010, "유저 아이디 값을 확인해주세요."),

    // [POST] /users
    POST_USERS_EMPTY_EMAIL(false, 2015, "이메일을 입력해주세요."),
    POST_USERS_INVALID_EMAIL(false, 2016, "이메일 형식을 확인해주세요."),
    POST_USERS_EXISTS_EMAIL(false,2017,"중복된 이메일입니다."),

    POST_USERS_EMPTY_NAME(false, 2020, "이름을 입력해주세요." ),
    POST_USERS_EMPTY_ADDRESS(false, 2021, "주소를 입력해주세요."),
    POST_USERS_EMPTY_PHONENUM(false, 2022, "휴대폰 번호를 입력해주세요"),
    POST_USERS_EMPTY_PASSWORD(false, 2023, "비밀번호를 설정하세요."),
    POST_USERS_EMPTY_STATUS(false,2024,"상태를 등록하세요."),
    POST_USERS_EMPTY_TEXT(false, 2025, "텍스트를 입력해주세요."),
    POST_USERS_WRITE_TOO_LONG_TEXT(false, 2030, "글자수는 100 글자를 넘길 수 없습니다."),
    POST_RESTAURANT_EMPTY_RESTAURANT_ID(false, 2040, "가게 id를 입력해주세요."),


    /**
     * 3000 : Response 오류
     */
    // Common
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),

    // [POST] /users
    DUPLICATED_EMAIL(false, 3013, "중복된 이메일입니다."),
    DUPLICATED_ADDRESS(false,3014, "중복된 주소가 존재합니다."),
    FAILED_TO_LOGIN(false,3014,"없는 아이디거나 비밀번호가 틀렸습니다."),



    /**
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다."),

    //[PATCH] /users/{userIdx}
    MODIFY_FAIL_USER_ADDRESS(false,4014,"유저 주소 수정 실패"),

    PASSWORD_ENCRYPTION_ERROR(false, 4011, "비밀번호 암호화에 실패하였습니다."),
    PASSWORD_DECRYPTION_ERROR(false, 4012, "비밀번호 복호화에 실패하였습니다.");


    // 5000 : 필요시 만들어서 쓰세요
    // 6000 : 필요시 만들어서 쓰세요


    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
