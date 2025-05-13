package com.modive.adminservice.external.reward.dto.req;

import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * 리워드 서비스의 "/reward/filter" API 요청의 파라미터 항목 DTO
 * - 사용처: RewardClient
 */

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RCRewardFilterReq {
    private Long userId;
    private String reason;
    private String date;

    @NotNull
    private Integer page;

    @NotNull
    private Integer pageSize;
}
