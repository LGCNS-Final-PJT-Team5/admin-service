package com.modive.adminservice.external.dashboard.dto.res;

import lombok.*;

import java.util.List;

/**
 * 대시보드 서비스의 "/dashboard/drives/"{userId}" API 응답의 data 필드  DTO.
 * - 실제 응답 구조: { "status": ..., "message": ..., "data": { "driveHistory": [...] } }
 * - 사용처: DashBoardClient
 */

@Data
@Builder
public class DCDriveListResData {
    private DriveHistoryWrapper driveHistory;

    @Data
    public static class DriveHistoryWrapper {
        private List<DCDriveListItem> list;
        private String driveId;
        private String startTime;
    }
}

