package com.modive.adminservice.global.error.dto;

import com.modive.adminservice.global.error.code.ErrorCode;
import lombok.Getter;

@Getter
public class ErrorRes {
    public final int status;
    public final String message;

    public ErrorRes(ErrorCode errorCode) {
        this.status = errorCode.getStatus().value();
        this.message = errorCode.getMessage();
    }
}
