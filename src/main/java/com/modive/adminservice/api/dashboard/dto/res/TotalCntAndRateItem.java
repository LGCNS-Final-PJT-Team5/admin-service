package com.modive.adminservice.api.dashboard.dto.res;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TotalCntAndRateItem {
    private Long value;
    private double changeRate;
}
