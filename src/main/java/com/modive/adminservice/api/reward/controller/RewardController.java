package com.modive.adminservice.api.reward.controller;

import com.modive.adminservice.api.reward.dto.*;
import com.modive.adminservice.external.reward.dto.req.RCRewardFilterReq;
import com.modive.adminservice.external.reward.service.RewardFetchService;
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
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Tag(name = "리워드", description = "리워드와 관련된 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/rewards")
public class RewardController {

    private final RewardFetchService rewardFetchService;

    @GetMapping("/summary")
    @Operation(summary = "씨앗 내역 통계 (상단 부분)", description = "씨앗 내역 통계를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = {@Content(schema = @Schema(implementation = CommonRes.class))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = {@Content(schema = @Schema(implementation = ErrorRes.class))})
    })
    public ResponseEntity<CommonRes<RewardsSummaryDto>> getSummary(@RequestHeader("X-User-Id") String userId) {

        RewardsSummaryDto data = rewardFetchService.fetchRewardSummary(userId);

        return new ResponseEntity<>(
                CommonRes.success(data, "발급 사유별 월별 통계에 성공했습니다."),
                HttpStatus.OK
        );
    }

    @GetMapping("/by-reason/total")
    @Operation(summary = "발급 사유별 총 통계 (전체 누적 그래프)", description = "발급 사유별 총 통계를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = {@Content(schema = @Schema(implementation = CommonRes.class))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = {@Content(schema = @Schema(implementation = ErrorRes.class))})
    })
    public ResponseEntity<CommonRes<RewardByReasonTotalDto>> getRewardByReasonTotal(@RequestHeader("X-User-Id") String userId) {

        RewardByReasonTotalDto data = rewardFetchService.fetchRewardByReasonTotal(userId);

        return new ResponseEntity<>(
                CommonRes.success(data, "발급 사유별 총 통계에 성공했습니다."),
                HttpStatus.OK
        );
    }

    @GetMapping("/by-reason/monthly-stats")
    @Operation(summary = "발급 사유별 월별 통계", description = "발급 사유별 월별 통계를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = {@Content(schema = @Schema(implementation = CommonRes.class))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = {@Content(schema = @Schema(implementation = ErrorRes.class))})
    })
    public ResponseEntity<CommonRes<RewardByReasonMonthDto>> getRewardByReasonMonth(
            @RequestHeader("X-User-Id") String userId,
            @RequestParam int year,
            @RequestParam int month
    ) {

        RewardByReasonMonthDto data = rewardFetchService.fetchRewardByReasonMonth(userId, year, month);

        return new ResponseEntity<>(
                CommonRes.success(data, "발급 사유별 총 통계에 성공했습니다."),
                HttpStatus.OK
        );
    }

    @GetMapping("/monthly-stats")
    @Operation(summary = "월별 씨앗 지급 통계", description = "월별 씨앗 지급 통계를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = {@Content(schema = @Schema(implementation = CommonRes.class))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = {@Content(schema = @Schema(implementation = ErrorRes.class))})
    })
    public ResponseEntity<CommonRes<RewardMonthDto>> getRewardMonth(@RequestHeader("X-User-Id") String userId) {

        RewardMonthDto data = rewardFetchService.fetchRewardMonth(userId);

        return new ResponseEntity<>(
                CommonRes.success(data, "월별 씨앗 지급 통계에 성공했습니다."),
                HttpStatus.OK
        );
    }

    @GetMapping("/history")
    @Operation(summary = "최근 씨앗 발급 내역", description = "최근 씨앗 발급 내역을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = {@Content(schema = @Schema(implementation = CommonRes.class))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = {@Content(schema = @Schema(implementation = ErrorRes.class))})
    })
    public ResponseEntity<CommonRes<RewardHistoryDto>> getRewardHistory(
            @RequestHeader("X-User-Id") String userId,
            @RequestParam int page,
            @RequestParam int pageSize
    ) {

        RewardHistoryDto data = rewardFetchService.fetchRewardHistory(userId, page, pageSize);

        return new ResponseEntity<>(
                CommonRes.success(data, "최근 씨앗 발급 내역 조회에 성공했습니다."),
                HttpStatus.OK
        );
    }

    @GetMapping()
    @Operation(summary = "씨앗 필터링", description = "씨앗 필터링을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = {@Content(schema = @Schema(implementation = CommonRes.class))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = {@Content(schema = @Schema(implementation = ErrorRes.class))})
    })
    public ResponseEntity<CommonRes<RewardFilterDto>> getRewardFilter(
            @RequestHeader("X-User-Id") String userId,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String reason,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam int page,
            @RequestParam int pageSize
    ) {

        RewardFilterDto data = rewardFetchService.fetchRewardFilter(userId, email, reason, startDate, endDate, page, pageSize);

        return new ResponseEntity<>(
                CommonRes.success(data, "씨앗 필터링 조회에 성공하였습니다."),
                HttpStatus.OK
        );
    }

}
