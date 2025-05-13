package com.modive.adminservice.external.user.dto.res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UCUserMonthlyItem {
    private int year;
    private int month;
    private Long newUsers;
    private Long activeUsers;
    private Long churnedUsers;
}
