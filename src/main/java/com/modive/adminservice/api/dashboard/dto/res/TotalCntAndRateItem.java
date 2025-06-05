package com.modive.adminservice.api.dashboard.dto.res;

import lombok.*;

/**
 * 관리자 대시보드 통계 조회 API 응답에 사용되는 DTO.
 * (누적 사용자, 누적 자동차 수, 누적 운전 수, 누적 씨앗 발행 수)
 * 누적 수, 증감률 포함
 */

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TotalCntAndRateItem {
    private Long value;
    private double changeRate;
}
