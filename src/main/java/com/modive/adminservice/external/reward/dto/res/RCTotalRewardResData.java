package com.modive.adminservice.external.reward.dto.res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RCTotalRewardResData {
    private RCRewardTotalCntAndRateItem totalIssuedRewards;
}
