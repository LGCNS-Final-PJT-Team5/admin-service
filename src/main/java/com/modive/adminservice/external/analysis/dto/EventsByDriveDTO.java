package com.modive.adminservice.external.analysis.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventsByDriveDTO {
    private String type;
    private Long count;
}
