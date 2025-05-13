package com.modive.adminservice.useradmin.dto.res;

import lombok.*;

import java.util.List;

/**
 * 관리자 특정 사용자 운전 내역 조회 API 응답에 사용되는 DTO.
 * 운전일, 주행 시간, 이벤트, 주행에 대한 리워드 포함
 */

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDriveListItem {
    private String date;
    private int driveDuration;
    private List<UserDriveListEventItem> events;
    private int rewards;
}
