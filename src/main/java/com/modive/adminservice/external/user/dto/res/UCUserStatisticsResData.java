package com.modive.adminservice.external.user.dto.res;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UCUserStatisticsResData {
    private UCUserStatisticsSummaryItem summary;
    private List<UCUserMonthlyItem> userTrend;
}
