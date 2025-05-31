package com.modive.adminservice.external.user.dto.res;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UCFilterUserResData {

    private List<UCUserListItem> content;
    private UCPageableItem pageable;

    private boolean last;
    private int totalPages;
    private long totalElements;
    private boolean first;
    private int size;
    private int number;
    private List<Object> sort;
    private int numberOfElements;
    private boolean empty;
}
