package com.modive.adminservice.api.reward.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RewardFilterDto {

    private List<FilteredRewardResultItem> searchResult;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FilteredRewardResultItem {
        private String rewardId;
        private String userId;
        private LocalDate createdAt;
        private String Description;
        private int amount;
    }
}
