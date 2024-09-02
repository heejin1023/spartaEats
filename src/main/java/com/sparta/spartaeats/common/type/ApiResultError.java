package com.sparta.spartaeats.common.type;

public enum ApiResultError {

    NO_ERROR				("0000"	, "OK"),

    NO_AUTH_ERROR				("401"	, "권한이 없습니다."),

    LOGIN_ERR_NOT_FOUND_USER 	("1404"	, "사용자 아이디 혹은 비밀번호가 틀렸습니다."),
    LOGIN_ERR_NOT_MATCH_PASSWD	("1401"	, "사용자 아이디 혹은 비밀번호가 틀렸습니다."),
    LOGIN_ERR_NOT_USED_USER 	("1405"	, "사용 할 수 없는 계정 입니다."),
    LOGIN_ERR_REQUIRED			("1500"	, "로그인이 필요합니다"),
    PRODUCT_NO_STORE_ERROR      ("2603", "STORE_ID를 찾을 수 없습니다."),
    STORE_NO_OWNER              ("3603", "음식점 OWNER ID를 찾을 수 없습니다."),

    ERROR_INVALID_LOGIN			("9201" , "로그인정보가 유효하지 않습니다."),

    BAD_REQUEST					("9400", "잘못된 요청입니다."),
    ERROR_ACCESS_DENIED			("9403", "실행 권한이 없습니다."),
    NO_AUTH_PERMISSION_DENIED	("9403", "사용 권한이 없습니다."),

    NO_AUTH_NOT_FOUND_USER		("9404", "유효하지 않은 사용자 정보입니다."),

    ERROR_SERVER_ERROR			("9500", "서버 오류가 발생하였습니다."),
    ERROR_CALL_API				("9801", "API 호출오류."),

    ERROR_INVALID_ARGUMENT      ("9601", "잘못된 인자가 전달되었습니다."),  // IllegalArgumentException에 대한 에러 코드
    ERROR_INVALID_STATE         ("9602", "객체의 상태가 유효하지 않습니다."), // IllegalStateException에 대한 에러 코드
    ERROR_EMPTY_DATA            ("9603", "데이터가 존재하지 않습니다"),

    ERROR_PARAMETERS			("9901", "파라미터 오류."),
    ERROR_NOT_SUPPORTED_METHOD	("9902", "지원하지 않는 Method 입니다."),
    ERROR_INTERNAL_API_PARAMETERS("9903", "내부 API 파라미터 오류."),
    ERROR_TIMEOUT               ("9904", "시간 초과"),
    ERROR_DEFAULT				("9999", "오류가 발생하였습니다.");



    private String code;
    private String message;

    private ApiResultError(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public static boolean isOk(String code) {
        return ApiResultError.NO_ERROR.code.equals(code);
    }

}

