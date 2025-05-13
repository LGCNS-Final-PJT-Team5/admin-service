package com.modive.adminservice.external.reward.dto.res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 리워드 서비스의 "/reward/filter" API 응답에 사용되는 DTO
 * - 사용처: RewardClient
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RCRewardFilterItem {
    private Long rewardId;

//    TODO 응답 DTO에 이메일 포함 여부 확인
//    private String email;

    private String issuedDate;
    private String reason;
    private int amount;
}
