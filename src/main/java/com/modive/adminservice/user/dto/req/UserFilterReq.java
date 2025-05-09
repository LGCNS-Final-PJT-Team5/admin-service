package com.modive.adminservice.user.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 관리자 사용자 필터링 API 요청에 사용되는 DTO
 * 최소 운전 경력, 최대 운전 경력 등을 포함
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserFilterReq {
    @Schema(description = "최소 운전 경력 (개월)", example = "6")
    private Integer minExperience;

    @Schema(description = "최대 운전 경력 (개월)", example = "24")
    private Integer maxExperience;

    @Schema(description = "가입 후 경과 개월 수", example = "12")
    private Integer accountAgeInMonths;

    @Schema(description = "계정 활성 여부 (1: 활성, 0: 비활성)", example = "1")
    private Integer active;

    @NotNull
    @Schema(description = "페이지 번호", example = "1", required = true)
    private Integer page;

    @NotNull
    @Schema(description = "페이지당 데이터 수", example = "10", required = true)
    private Integer pageSize;
}
