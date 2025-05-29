package com.modive.adminservice.api.reward.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RewardMonthDto {

    @Schema(description = "월별 통계")
    private List<MonthlyRewardStatistic> monthlyRewardStatistics;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MonthlyRewardStatistic {
        private int year;
        private int month;
        private int amount;
    }
}
