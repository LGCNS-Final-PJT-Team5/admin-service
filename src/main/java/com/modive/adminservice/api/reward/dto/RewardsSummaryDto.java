package com.modive.adminservice.api.reward.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RewardsSummaryDto {

    @Schema(description = "총 발행량")
    private RewardMetric totalIssued;

    @Schema(description = "월간 발행량")
    private RewardMetric monthlyIssued;

    @Schema(description = "일 평균 발행량")
    private RewardMetric dailyAverageIssued;

    @Schema(description = "유저 1인당 평균 발행량")
    private RewardMetric perUserAverageIssued;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RewardMetric {

        @Schema(description = "값", example = "1247890")
        private long value;

        @Schema(description = "변화율 (%)", example = "3.2")
        private double changeRate;
    }
}