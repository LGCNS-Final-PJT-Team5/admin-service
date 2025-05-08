package com.modive.adminservice.external.client.user.dto.res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 유저 서비스의 "/user/list" API 응답에서 각 사용자 항목을 나타내는 DTO.
 * - 사용처: UserClient
 */

@Getter
@Setter
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
