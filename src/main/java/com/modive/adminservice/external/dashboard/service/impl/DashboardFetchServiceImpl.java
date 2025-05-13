package com.modive.adminservice.external.dashboard.service.impl;

import com.modive.adminservice.external.dashboard.client.DashBoardClient;
import com.modive.adminservice.external.dashboard.dto.res.*;
import com.modive.adminservice.global.dto.res.CommonRes;
import com.modive.adminservice.global.error.code.ErrorCode;
import com.modive.adminservice.global.error.exception.RestApiException;
import com.modive.adminservice.external.dashboard.service.DashboardFetchService;
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

    /**
     * 대시보드 서비스에서 특정 사용자의 운전 내역 리스트 조회
     *
     * @param userId 사용자 ID
     * @return 운전 내역 리스트
     */
    @Override
    public List<DCDriveListItem> fetchDriveListByUserId(Long userId) {
        CommonRes<DCDriveListResData> res = dashBoardClient.getDrivesByUserId(userId);
        if (res == null || res.data == null) {
            log.warn("DashboardClient.getDrivesByUserId(userId = {}) - response or data is null", userId);
            throw new RestApiException(ErrorCode.FEIGN_DATA_MISSING);
        }

        return res.getData().getDriveHistory();
    }

    /**
     *  대시보드 서비스에서 누적 주행 횟수와 증감률 조회
     */
    @Override
    public DCTotalCntAndRateItem fetchDriveTotalCntAndRate() {
        CommonRes<DCTotalDriveResData> res = dashBoardClient.getTotalDriveCount();
        if (res == null || res.data == null) {
            log.warn("DashboardClient.getTotalDriveCount() - response or data is null");
            throw new RestApiException(ErrorCode.FEIGN_DATA_MISSING);
        }

        return res.getData().getTotalDrives();
    }
}
