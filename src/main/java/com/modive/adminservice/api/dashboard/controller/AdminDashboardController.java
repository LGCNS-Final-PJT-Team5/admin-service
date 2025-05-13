package com.modive.adminservice.api.dashboard.controller;

import com.modive.adminservice.api.dashboard.dto.res.TotalCntAndRateItem;
import com.modive.adminservice.api.dashboard.dto.res.TotalEventCntByReasonItem;
import com.modive.adminservice.domain.event.service.EventService;
import com.modive.adminservice.global.dto.res.CommonRes;
import com.modive.adminservice.global.error.dto.ErrorRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "대시보드", description = "대시보드와 관련된 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/dashboard")
public class AdminDashboardController {
    private final EventService eventService;

    @GetMapping("/events/by-reason/total")
    @Operation(summary = "이벤트 유형별 추이", description = "이벤트 유형별 발생 횟수를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = {@Content(schema = @Schema(implementation = CommonRes.class))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = {@Content(schema = @Schema(implementation = ErrorRes.class))})
    })
    public ResponseEntity<CommonRes> getEventsByReason() {
        List<TotalEventCntByReasonItem> items = eventService.getTotalEventCntByType();

        Map<String, Object> data = new HashMap<>();
        data.put("eventsStatisticsByReason", items);

        return new ResponseEntity<>(
                CommonRes.success(data, "이벤트 유형별 추이 조회에 성공하였습니다."),
                HttpStatus.OK
        );
    }

    @GetMapping("/summary")
    @Operation(summary = "대시 보드 통계", description = "대시 보드 상단의 통계 데이터를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = {@Content(schema = @Schema(implementation = CommonRes.class))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = {@Content(schema = @Schema(implementation = ErrorRes.class))})
    })
    public ResponseEntity<CommonRes> getMonthly () {
        Map<String, TotalCntAndRateItem> dashboardStatistics = new HashMap<>();

        Map<String, Object> data = new HashMap<>();
        data.put("dashboardStatistics", dashboardStatistics);

        return new ResponseEntity<>(
                CommonRes.success(data, "대시 보드 통계 조회에 성공하였습니다."),
                HttpStatus.OK
        );
    }
}
