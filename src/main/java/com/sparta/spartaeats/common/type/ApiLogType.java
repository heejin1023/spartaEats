package com.sparta.spartaeats.common.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApiLogType {

    LOGIN 					("001_000001", "로그인"),
    LOGOUT 					("001_000002", "로그아웃"),


    ;

    private String code;
    private String nameKo;

    public static ApiLogType find(String name) {
        for(ApiLogType e : ApiLogType.values()) {
            if(e.name().equals(name)) return e;
        }
        return null;
    }

    public static ApiLogType findByCode(String code) {
        for(ApiLogType e : ApiLogType.values()) {
            if(e.code.equals(code)) return e;
        }
        return null;
    }
}
