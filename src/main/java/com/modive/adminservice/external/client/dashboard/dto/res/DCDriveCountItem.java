package com.modive.adminservice.external.client.dashboard.dto.res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 대시보드 서비스의 "/dashboard/drives/by-user" API에서 각 사용자의 주행 정보 횟수 DTO.
 * - 사용처: DashBoardClient
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DCDriveCountItem {
    private Long userId;
    private int driveCount;
}
