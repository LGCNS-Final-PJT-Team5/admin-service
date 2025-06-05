package com.modive.adminservice.api.reward.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RewardHistoryDto {

    private List<RewardHistoryItem> rewardHistory;
    private PageInfo pageInfo;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RewardHistoryItem {
        private String rewardId;
        //private String email;
        private String issuedDate;
        private String reason;
        private int amount;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PageInfo {
        private int currentPage;
        private int pageSize;
        private int totalElements;
        private int totalPages;
    }
}
