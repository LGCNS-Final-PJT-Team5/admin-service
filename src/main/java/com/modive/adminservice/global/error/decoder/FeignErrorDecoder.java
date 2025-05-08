package com.modive.adminservice.global.error.decoder;

import com.modive.adminservice.global.error.code.ErrorCode;
import com.modive.adminservice.global.error.exception.RestApiException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FeignErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        int status = response.status();

        log.warn("[FeignError] methodKey={}, status={}", methodKey, status);

        for (ErrorCode errorCode : ErrorCode.values()) {
            if (errorCode.getStatus().value() == status) {
                return new RestApiException(errorCode);
            }
        }

        return new RestApiException(ErrorCode.INTERNAL_SERVER_ERROR);
    }
}
