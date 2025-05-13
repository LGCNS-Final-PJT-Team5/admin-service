package com.modive.adminservice.external.client.reward.dto.req;

import lombok.*;

import java.util.List;

/**
 * 리워드 서비스의 "/reward/by-drive" API 요청의 본문 DTO
 * - 사용처: RewardClient
 */

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RCRewardByDriveReq {
    private List<Long> driveIds;
}
