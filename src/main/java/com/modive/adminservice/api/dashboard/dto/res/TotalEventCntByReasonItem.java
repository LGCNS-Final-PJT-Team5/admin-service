package com.modive.adminservice.api.dashboard.dto.res;

import lombok.*;

/**
 * 관리자 타입별 이벤트 발생 횟수 조회 API 응답에 사용되는 DTO.
 * 이유, 횟수 포함
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
