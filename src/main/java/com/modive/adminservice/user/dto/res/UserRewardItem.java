package com.modive.adminservice.user.dto.res;

import lombok.*;

/**
 * 관리자 특정 사용자 씨앗 내역 조회 API 응답에 사용되는 DTO.
 * 발급일, 발급 사유, 발급량 포함
 */

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRewardItem {
    private String issuedDate;
    private String reason;
    private int amount;
}
