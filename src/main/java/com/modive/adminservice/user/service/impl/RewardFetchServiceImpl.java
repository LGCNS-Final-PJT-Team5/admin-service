package com.modive.adminservice.user.service.impl;

import com.modive.adminservice.external.client.reward.RewardClient;
import com.modive.adminservice.external.client.reward.dto.req.RewardFilterReq;
import com.modive.adminservice.external.client.reward.dto.res.RewardFilterItem;
import com.modive.adminservice.external.client.reward.dto.res.RewardFilterResData;
import com.modive.adminservice.global.dto.res.CommonRes;
import com.modive.adminservice.global.error.code.ErrorCode;
import com.modive.adminservice.global.error.exception.RestApiException;
import com.modive.adminservice.user.service.RewardFetchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public List<RewardFilterItem> fetchRewardFilter(RewardFilterReq req) {
        CommonRes<RewardFilterResData> res = rewardClient.filterReward(req);
        if (res == null || res.getData() == null) {
            log.warn("RewardClient.filterReward - response or data is null");
            throw new RestApiException(ErrorCode.FEIGN_DATA_MISSING);
        }
        return res.getData().getSearchResult();
    }
}
