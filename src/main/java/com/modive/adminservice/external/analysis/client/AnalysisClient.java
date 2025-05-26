package com.modive.adminservice.external.analysis.client;

import com.modive.adminservice.api.dashboard.dto.res.TotalEventCntByReasonItem;
import com.modive.adminservice.external.analysis.dto.EventsByDriveDTO;
import com.modive.adminservice.external.reward.dto.req.RCRewardByDriveReq;
import com.modive.adminservice.external.reward.dto.req.RCRewardFilterReq;
import com.modive.adminservice.global.dto.res.CommonRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * analysis-service와 통신하는 Feign Client.
 */

@FeignClient(name="analysis-service")
public interface AnalysisClient {

    /**
     * 특정 운전의 이벤트별 횟수 계산
     *
     * @param driveId 운전 ID
     * @return 특정 운전의 이벤트별 횟수
     */
    @GetMapping("/events/{driveId}")
    List<EventsByDriveDTO> getTotalEventCntByType(@PathVariable Long driveId);

    /**
     * 운전별 누적 이벤트 횟수 계산
     *
     * @return
     */
    @GetMapping("/events")
    List<TotalEventCntByReasonItem> getTotalEventCntByType();
}
