package com.modive.adminservice.api.dashboard.dto.res;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyDrivesItem {
    private int year;
    private int month;
    private Long count;
}
