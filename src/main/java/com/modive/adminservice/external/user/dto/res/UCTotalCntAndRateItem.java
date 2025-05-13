package com.modive.adminservice.external.user.dto.res;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UCTotalCntAndRateItem {
    private Long value;
    private double changeRate;
}
