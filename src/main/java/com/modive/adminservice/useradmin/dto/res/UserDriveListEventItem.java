package com.modive.adminservice.useradmin.dto.res;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDriveListEventItem {
    private String type;
    private Long count;
}
