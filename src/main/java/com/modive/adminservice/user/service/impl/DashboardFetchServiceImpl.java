package com.modive.adminservice.user.service.impl;

import com.modive.adminservice.external.client.dashboard.DashBoardClient;
import com.modive.adminservice.external.client.dashboard.dto.res.DCDriveCountItem;
import com.modive.adminservice.external.client.dashboard.dto.res.DCDriveCountResData;
import com.modive.adminservice.global.dto.res.CommonRes;
import com.modive.adminservice.global.error.code.ErrorCode;
import com.modive.adminservice.global.error.exception.RestApiException;
import com.modive.adminservice.user.service.DashboardFetchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class DashboardFetchServiceImpl implements DashboardFetchService {
    private final DashBoardClient dashBoardClient;

    /**
     * 대시보드 서비스에서 사용자별 운전 횟수 맵 조회
     *
     * @param userIds 사용자 ID 리스트
     * @return 사용자 ID별 운전 횟수 맵
     */
     @Override
     public Map<Long, Integer> fetchDriveCountMap(List<Long> userIds) {
        Map<String, List<Long>> clientReqBody = new HashMap<>();
        clientReqBody.put("userIds", userIds);

        CommonRes<DCDriveCountResData> dashboardClientRes = dashBoardClient.getDriveCountByUser(clientReqBody);
        if (dashboardClientRes == null || dashboardClientRes.data == null) {
            log.warn("DashboardClient.getDriveCountByUser - response or data is null");
            throw new RestApiException(ErrorCode.FEIGN_DATA_MISSING);
        }

        Map<Long, Integer> driveCountMap = new HashMap<>();
        for (DCDriveCountItem driveCount : dashboardClientRes.data.getDriveCountByUser()) {
            driveCountMap.put(driveCount.getUserId(), driveCount.getDriveCount());
        }

        return driveCountMap;
     }
}
