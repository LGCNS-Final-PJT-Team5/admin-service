package com.modive.adminservice.global.dto.res;

import lombok.*;

import java.util.Map;

/**
 * 공통 API 응답 포맷 정의.
 */

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommonRes<T> {
    public int status;
    public String message;
    public T data;
}
