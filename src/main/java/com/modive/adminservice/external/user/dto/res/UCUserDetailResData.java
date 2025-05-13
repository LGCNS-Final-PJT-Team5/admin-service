package com.modive.adminservice.external.user.dto.res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 유저 서비스의 "/user/{userId}" API 응답의 data 필드를 감싸는 DTO.
 * - 실제 응답 구조: { "status": ..., "message": ..., "data": { "searchResult": [...] } }
 * - 사용처: UserClient
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UCUserDetailResData {
    private int reward;
    private String nickname;
    private String email;
    private String birthdate;
    private String licenseDate;
    private boolean alarm;
    private String gender;
    private String phone;
}
