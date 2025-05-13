package com.modive.adminservice.api.dashboard.dto.res;

import lombok.*;

/**
 * 관리자 월별 사용자 추이 조회 API 응답에 사용되는 DTO.
 * 연도, 월, 신규 가입자, 활성 사용자, 이탈 사용자 포함.
 */

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserMonthlyItem {
    private int year;
    private int month;
    private Long newUsers;
    private Long activeUsers;
    private Long churnedUsers;
}
