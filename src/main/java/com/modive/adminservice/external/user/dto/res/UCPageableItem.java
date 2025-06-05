package com.modive.adminservice.external.user.dto.res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UCPageableItem {
    private int pageNumber;
    private int pageSize;
    private List<Object> sort; // 정렬이 복잡하면 정렬 DTO로 대체 가능
    private long offset;
    private boolean unpaged;
    private boolean paged;
}
