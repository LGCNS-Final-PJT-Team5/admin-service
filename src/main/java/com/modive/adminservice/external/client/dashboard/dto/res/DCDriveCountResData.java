package com.modive.adminservice.external.client.dashboard.dto.res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * 대시보드 서비스의 "/dashboard/drives/by-user" API 응답의 data 필드  DTO.
 * - 실제 응답 구조: { "status": ..., "message": ..., "data": { "driveCountByUser": [...] } }
 * - 사용처: DashBoardClient
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DCDriveCountResData {
    private List<DCDriveCountItem> driveCountByUser;
}
