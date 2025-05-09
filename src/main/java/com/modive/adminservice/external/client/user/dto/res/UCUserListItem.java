package com.modive.adminservice.external.client.user.dto.res;

import lombok.*;

/**
 * 유저 서비스의
 * "/user/list" API, "/user?searchKeyword=test@modive.com"
 * API 응답에서 각 사용자 항목을 나타내는 DTO.
 * - 사용처: UserClient
 */

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UCUserListItem {
    private Long userId;
    private String nickname;
    private String email;
    private int experience;
    private String joinedAt;
    private int seedBalance;
    private int isActive;
}
