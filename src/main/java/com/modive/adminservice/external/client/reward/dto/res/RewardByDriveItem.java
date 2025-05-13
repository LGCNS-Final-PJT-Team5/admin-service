package com.modive.adminservice.external.client.reward.dto.res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 리워드 서비스의 "/reward/by-drive" API 응답에 사용되는 DTO
 * - 사용처: RewardClient
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RewardByDriveItem {
    private Long driveId;
    private int reward;
}
