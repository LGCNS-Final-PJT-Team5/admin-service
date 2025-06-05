package com.modive.adminservice.external.user.dto.res;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UCUserStatisticsSummaryItem {
    private Long lastWeekNewUsers;
    private double monthlyUserGrowthRate;
    private double churnRate;
}
