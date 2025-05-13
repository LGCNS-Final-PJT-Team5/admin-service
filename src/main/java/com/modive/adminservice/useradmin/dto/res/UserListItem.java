package com.modive.adminservice.useradmin.dto.res;

import lombok.*;

/**
 * 관리자 사용자 목록 API 응답에 사용되는 DTO.
 * 사용자 기본 정보와 주행 횟수, 씨앗 잔액, 활성 여부 등 포함.
 */

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserListItem {
    private Long userId;
    private String nickname;
    private String email;
    private int experience;
    private String joinedAt;
    private int driveCount;
    private int seedBalance;
    private int isActive;
}
