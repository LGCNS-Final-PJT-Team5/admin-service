package com.modive.adminservice.external.client.reward;

import com.modive.adminservice.external.client.reward.dto.req.RCRewardByDriveReq;
import com.modive.adminservice.external.client.reward.dto.req.RCRewardFilterReq;
import com.modive.adminservice.global.dto.res.CommonRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * reward-service와 통신하는 Feign Client.
 */

@FeignClient(name="reward-service")
public interface RewardClient {

    /**
     * 발급 사유, 유저 ID, 발급 날짜를 기준으로 필터링
     *
     * @param params 필터링 데이터, 페이지네이션에 필요한 데이터
     * @return CommonRes 형태의 필터링 결과 응답
     */
    @GetMapping("/reward/filter")
    CommonRes filterReward(@SpringQueryMap RCRewardFilterReq params);

    /**
     * drive ID를 기준으로 리워드 적립 내용 조회
     *
     * @param req drive ID 리스트
     * @return drive ID별 리워드 적립 데이터
     */
    @PostMapping("/reward/by-drive")
    CommonRes getRewardByDrive(@RequestBody RCRewardByDriveReq req);
}
