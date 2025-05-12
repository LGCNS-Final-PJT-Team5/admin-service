package com.modive.adminservice.external.client.reward.dto.res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * 리워드 서비스의 "/reward/filter" API 응답의 data 필드  DTO.
 * - 실제 응답 구조: { "status": ..., "message": ..., "data": { "searchResult": [...] } } - TODO "searchResult" ->  "filterResult"
 * - 사용처: RewardClient
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RewardFilterResData {
    private List<RewardFilterItem> searchResult;
}
