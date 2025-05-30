package com.modive.adminservice.external.dashboard.client;

import com.modive.adminservice.external.dashboard.dto.res.DCDriveCountResData;
import com.modive.adminservice.external.dashboard.dto.res.DCDriveListResData;
import com.modive.adminservice.external.dashboard.dto.res.DCMontlyDriveResData;
import com.modive.adminservice.external.dashboard.dto.res.DCTotalDriveResData;
import com.modive.adminservice.global.dto.res.CommonRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * dashboard-service와 통신하는 Feign Client.
 */
@FeignClient(name = "dashboard-service")
public interface DashBoardClient {

    /**
     * 사용자 ID 리스트를 전달하여, 각 사용자별 주행 횟수 조회.
     *
     * @param body { "userIds": [1, 2, 3, ...] }
     * @return CommonRes 형태로 사용자별 주행 횟수 정보가 담긴 응답 반환
     */
    @PostMapping("/dashboard/drives/by-user")
    CommonRes<DCDriveCountResData>  getDriveCountByUser(@RequestBody Map<String, List<String>> body);

    /**
     * 사용자 ID를 전달하여, 해당 사용자의 운전 내역 조회.
     *
     * @param userId 운전 내역 조회 대상 유저 ID
     * @return CommonRes 형태로 사용자의 운전 내역 응답 반환
     */
    @GetMapping("/dashboard/drives/{userId}")
    CommonRes<DCDriveListResData> getDrivesByUserId(
            @PathVariable("userId") String userId,
            @RequestParam(name = "pageSize" , required = false) int pageSize,
            @RequestParam(name = "startTime", required = false) String startTime,
            @RequestParam(name = "driveId", required = false) String driveId
    );
    @GetMapping("/dashboard/drives/total")
    CommonRes<DCTotalDriveResData> getTotalDriveCount();

    @GetMapping("/dashboard/drives/monthly-stats")
    CommonRes<DCMontlyDriveResData> getMonthlyStats();
}

