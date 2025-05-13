package com.modive.adminservice.external.reward.service.impl;

import com.modive.adminservice.external.reward.client.RewardClient;
import com.modive.adminservice.external.reward.dto.req.RCRewardByDriveReq;
import com.modive.adminservice.external.reward.dto.req.RCRewardFilterReq;
import com.modive.adminservice.external.reward.dto.res.*;
import com.modive.adminservice.global.dto.res.CommonRes;
import com.modive.adminservice.global.error.code.ErrorCode;
import com.modive.adminservice.global.error.exception.RestApiException;
import com.modive.adminservice.external.reward.service.RewardFetchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class RewardFetchServiceImpl implements RewardFetchService {

    private RewardClient rewardClient;

    /**
     * 리워드 서비스에서 씨앗 발급 내역 필터링
     *
     * @param req 유저 ID, 발급 사유 등의 필터링 요소 및 페이지네이션에 사용될 데이터
     * @return 필터링 결과
     */
    @Override
    public List<RCRewardFilterItem> fetchRewardFilter(RCRewardFilterReq req) {
        CommonRes<RCRewardFilterResData> res = rewardClient.filterReward(req);
        if (res == null || res.getData() == null) {
            log.warn("RewardClient.filterReward - response or data is null");
            throw new RestApiException(ErrorCode.FEIGN_DATA_MISSING);
        }
        return res.getData().getSearchResult();
    }

    /**
     * 리워드 서비스에서 Drive ID별 리워드 적립 내용 조회
     *
     * @param req drive ID 리스트
     * @return drive ID별 리워드 조회 결과
     */
    @Override
    public Map<Long, Integer> fetchRewardMapByDrive(RCRewardByDriveReq req) {
        CommonRes<RCRewardByDriveResData> res = rewardClient.getRewardByDrive(req);
        if (res == null || res.getData() == null) {
            log.warn("RewardClient.getRewardByDrive(req = {}) - response or data is null", req.toString());
            throw new RestApiException(ErrorCode.FEIGN_DATA_MISSING);
        }

        Map<Long, Integer> rewardMap = new HashMap<>();
        for (RCRewardByDriveItem item : res.data.getRewardsByDrive()) {
            rewardMap.put(item.getDriveId(), item.getReward());
        }

        return rewardMap;
    }

    /**
     * 리워드 서비스에서 발급된 리워드 합계 및 증감률 조회
     */
    @Override
    public RCRewardTotalCntAndRateItem fetchTotalIssuedRewards() {
        CommonRes<RCTotalRewardResData> res = rewardClient.getTotalIssuedRewards();
        if (res == null || res.getData() == null) {
            log.warn("RewardClient.getTotalIssuedRewards() - response or data is null");
            throw new RestApiException(ErrorCode.FEIGN_DATA_MISSING);
        }

        return res.getData().getTotalIssuedRewards();
    }
}
