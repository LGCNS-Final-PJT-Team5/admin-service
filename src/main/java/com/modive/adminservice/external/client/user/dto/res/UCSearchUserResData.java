package com.modive.adminservice.external.client.user.dto.res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * 유저 서비스의 "/user?searchKeyword=test@modive.com" API 응답의 data 필드를 감싸는 DTO.
 * - 실제 응답 구조: { "status": ..., "message": ..., "data": { "searchResult": [...] } }
 * - 사용처: UserClient
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UCSearchUserResData {
    private List<UCUserListItem> searchResult;
}
