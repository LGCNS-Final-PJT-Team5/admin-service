package com.modive.adminservice.api.dashboard.dto.res;

import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserStatisticsSummaryItem {
    private Long lastWeekNewUsers;
    private double monthlyUserGrowthRate;
    private double churnRate;
}
