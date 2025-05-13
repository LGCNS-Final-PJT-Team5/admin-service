package com.modive.adminservice.api.dashboard.dto.res;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserMonthlyItem {
    private int year;
    private int month;
    private Long newUsers;
    private Long activeUsers;
    private Long churnedUsers;
}
