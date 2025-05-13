package com.modive.adminservice.api.user.dto.res;

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
