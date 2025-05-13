package com.modive.adminservice.external.client.dashboard.dto.res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * 대시보드 서비스의 "/dashboard/{userId}/drives" API의 응답에 사용될 DTO
 * - 사용처: DashBoardClient
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DCDriveListItem {
    private Long driveId;
    private String date;
    private int driveDuration;
}
