package com.modive.adminservice.global.error.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.hc.client5.http.ssl.HttpsSupport;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다."),
    FEIGN_DATA_PARSE_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "요청 응답에서 필드를 파싱하지 못했습니다."),
    FEIGN_DATA_MISSING(HttpStatus.INTERNAL_SERVER_ERROR, "요청 응답 데이터가 없습니다.");

    private final HttpStatus status;
    private final String message;
}
