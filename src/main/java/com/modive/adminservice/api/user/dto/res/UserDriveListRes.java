package com.modive.adminservice.api.user.dto.res;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDriveListRes {
    private List<UserDriveListItem> driveHistory;  // 주행 이력
    private String startTime;                      // 커서
    private String driveId;                        // 커서
}
