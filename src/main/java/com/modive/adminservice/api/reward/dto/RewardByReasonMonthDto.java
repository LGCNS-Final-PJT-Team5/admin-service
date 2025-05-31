package com.modive.adminservice.api.reward.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RewardByReasonMonthDto {

    @Schema(description = "리워드 통계 항목 리스트")
    private List<MonthRewardStaticsItemDto> monthlyRewardStatistics;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MonthRewardStaticsItemDto {

        @Schema(description = "리워드 사유", example = "종합점수")
        private String reason;

        @Schema(description = "지급 횟수", example = "1200")
        private int count;

        @Schema(description = "비율 (%)", example = "51.6")
        private double ratio;
    }
}
