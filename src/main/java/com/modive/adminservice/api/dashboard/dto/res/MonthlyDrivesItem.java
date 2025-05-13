package com.modive.adminservice.api.dashboard.dto.res;

import lombok.*;

/**
 * 관리자 월별 운전 횟수 조회 API 응답에 사용되는 DTO.
 * 연도, 월, 운전 횟수 포함
 */

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyDrivesItem {
    private int year;
    private int month;
    private Long count;
}
