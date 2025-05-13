package com.modive.adminservice.api.dashboard.dto.res;

import lombok.*;

/**
 * 관리자 특정 사용자 운전 내역 조회 API 응답에 사용되는 DTO.
 * 운전일, 주행 시간, 이벤트, 주행에 대한 리워드 포함
 */

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TotalEventCntByReasonItem {
    private String reason;
    private Long count;
}
