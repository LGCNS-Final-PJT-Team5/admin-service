package com.modive.adminservice.global.error.handler;

import com.modive.adminservice.global.error.dto.ErrorRes;
import com.modive.adminservice.global.error.code.ErrorCode;
import com.modive.adminservice.global.error.exception.RestApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(RestApiException.class)
    protected ResponseEntity<ErrorRes> handleRestApiException(RestApiException e) {
        ErrorCode errorCode = e.getErrorCode();
        return handleExceptionInternal(errorCode);
    }

    private ResponseEntity<ErrorRes> handleExceptionInternal(ErrorCode errorCode) {
        return ResponseEntity
                .status(errorCode.getStatus().value())
                .body(new ErrorRes(errorCode));
    }
}
