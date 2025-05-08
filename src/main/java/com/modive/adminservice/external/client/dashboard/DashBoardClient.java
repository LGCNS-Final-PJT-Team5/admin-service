package com.modive.adminservice.external.client.dashboard;

import com.modive.adminservice.global.dto.res.CommonRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * dashboard-service와 통신하는 Feign Client.
 */
@FeignClient(name="dashboard-service")
public interface DashBoardClient {

    /**
     * 사용자 ID 리스트를 전달하여, 각 사용자별 주행 횟수 조회.
     *
     * @param body { "userIds": [1, 2, 3, ...] }
     * @return CommonRes 형태로 사용자별 주행 횟수 정보가 담긴 응답 반환
     */
    @PostMapping("/dashboard/drives/by-user")
    CommonRes getDriveCountByUser(@RequestBody Map<String, List<Long>> body);
}
