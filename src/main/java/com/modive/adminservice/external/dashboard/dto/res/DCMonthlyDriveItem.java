package com.modive.adminservice.external.dashboard.dto.res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DCMonthlyDriveItem {
    private int year;
    private int month;
    private Long count;
}
