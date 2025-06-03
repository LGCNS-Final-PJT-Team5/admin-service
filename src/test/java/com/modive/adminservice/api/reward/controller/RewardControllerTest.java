package com.modive.adminservice.api.reward.controller;

import com.modive.adminservice.api.reward.dto.RewardHistoryDto;
import com.modive.adminservice.api.reward.dto.RewardMonthDto;
import com.modive.adminservice.api.reward.dto.RewardsSummaryDto;
import com.modive.adminservice.external.reward.service.RewardFetchService;
import com.modive.adminservice.global.dto.res.CommonRes;
import com.modive.adminservice.global.error.code.ErrorCode;
import com.modive.adminservice.global.error.exception.RestApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RewardControllerTest {

    private RewardFetchService rewardFetchService;
    private RewardController rewardController;
    private final String userId = "testUser123";

    @BeforeEach
    void setUp() {
        rewardFetchService = mock(RewardFetchService.class);
        rewardController = new RewardController(rewardFetchService);
    }

    /****
     * Given: RewardFetchService가 정상적으로 RewardsSummaryDto를 반환할 때
     * When: getSummary 메서드를 호출하면
     * Then: 응답 메시지와 DTO가 포함된 CommonRes가 반환된다.
     */
    @Test
    void getSummary_shouldReturnValidResponse() {
        RewardsSummaryDto dto = mock(RewardsSummaryDto.class);
        when(rewardFetchService.fetchRewardSummary(userId)).thenReturn(dto);

        ResponseEntity<CommonRes<RewardsSummaryDto>> response = rewardController.getSummary(userId);

        assertEquals("발급 사유별 월별 통계에 성공했습니다.", response.getBody().getMessage());
        assertEquals(dto, response.getBody().getData());
        verify(rewardFetchService).fetchRewardSummary(userId);
    }

    /****
     * Given: RewardFetchService가 RestApiException을 던질 때
     * When: getSummary 메서드를 호출하면
     * Then: 동일한 RestApiException이 다시 던져진다.
     */
    @Test
    void getSummary_whenServiceThrows_shouldPropagateException() {
        when(rewardFetchService.fetchRewardSummary(userId)).thenThrow(new RestApiException(ErrorCode.INTERNAL_SERVER_ERROR));

        RestApiException exception = assertThrows(RestApiException.class, () -> rewardController.getSummary(userId));
        assertEquals(ErrorCode.INTERNAL_SERVER_ERROR, exception.getErrorCode());
        verify(rewardFetchService).fetchRewardSummary(userId);
    }

    /****
     * Given: RewardFetchService가 정상적으로 RewardByReasonTotalDto를 반환할 때
     * When: getRewardByReasonTotal 메서드를 호출하면
     * Then: 응답 메시지와 DTO가 포함된 CommonRes가 반환된다.
     */
    @Test
    void getRewardByReasonTotal_shouldReturnValidResponse() {
        com.modive.adminservice.api.reward.dto.RewardByReasonTotalDto dto = mock(com.modive.adminservice.api.reward.dto.RewardByReasonTotalDto.class);
        when(rewardFetchService.fetchRewardByReasonTotal(userId)).thenReturn(dto);

        ResponseEntity<CommonRes<com.modive.adminservice.api.reward.dto.RewardByReasonTotalDto>> response = rewardController.getRewardByReasonTotal(userId);

        assertEquals("발급 사유별 총 통계에 성공했습니다.", response.getBody().getMessage());
        assertEquals(dto, response.getBody().getData());
        verify(rewardFetchService).fetchRewardByReasonTotal(userId);
    }

    /****
     * Given: RewardFetchService가 RestApiException을 던질 때
     * When: getRewardByReasonTotal 메서드를 호출하면
     * Then: 동일한 RestApiException이 다시 던져진다.
     */
    @Test
    void getRewardByReasonTotal_whenServiceThrows_shouldPropagateException() {
        when(rewardFetchService.fetchRewardByReasonTotal(userId)).thenThrow(new RestApiException(ErrorCode.INTERNAL_SERVER_ERROR));

        RestApiException exception = assertThrows(RestApiException.class, () -> rewardController.getRewardByReasonTotal(userId));
        assertEquals(ErrorCode.INTERNAL_SERVER_ERROR, exception.getErrorCode());
        verify(rewardFetchService).fetchRewardByReasonTotal(userId);
    }

    /****
     * Given: RewardFetchService가 정상적으로 RewardByReasonMonthDto를 반환할 때
     * When: getRewardByReasonMonth 메서드를 호출하면
     * Then: 응답 메시지와 DTO가 포함된 CommonRes가 반환된다.
     */
    @Test
    void getRewardByReasonMonth_shouldReturnValidResponse() {
        com.modive.adminservice.api.reward.dto.RewardByReasonMonthDto dto = mock(com.modive.adminservice.api.reward.dto.RewardByReasonMonthDto.class);
        int year = 2024, month = 6;
        when(rewardFetchService.fetchRewardByReasonMonth(userId, year, month)).thenReturn(dto);

        ResponseEntity<CommonRes<com.modive.adminservice.api.reward.dto.RewardByReasonMonthDto>> response = rewardController.getRewardByReasonMonth(userId, year, month);

        assertEquals("발급 사유별 총 통계에 성공했습니다.", response.getBody().getMessage());
        assertEquals(dto, response.getBody().getData());
        verify(rewardFetchService).fetchRewardByReasonMonth(userId, year, month);
    }

    /****
     * Given: RewardFetchService가 RestApiException을 던질 때
     * When: getRewardByReasonMonth 메서드를 호출하면
     * Then: 동일한 RestApiException이 다시 던져진다.
     */
    @Test
    void getRewardByReasonMonth_whenServiceThrows_shouldPropagateException() {
        int year = 2024, month = 6;
        when(rewardFetchService.fetchRewardByReasonMonth(userId, year, month)).thenThrow(new RestApiException(ErrorCode.INTERNAL_SERVER_ERROR));

        RestApiException exception = assertThrows(RestApiException.class, () -> rewardController.getRewardByReasonMonth(userId, year, month));
        assertEquals(ErrorCode.INTERNAL_SERVER_ERROR, exception.getErrorCode());
        verify(rewardFetchService).fetchRewardByReasonMonth(userId, year, month);
    }

    /****
     * Given: RewardFetchService가 정상적으로 RewardFilterDto를 반환할 때
     * When: getRewardFilter 메서드를 호출하면
     * Then: 응답 메시지와 DTO가 포함된 CommonRes가 반환된다.
     */
    @Test
    void getRewardFilter_shouldReturnValidResponse() {
        com.modive.adminservice.api.reward.dto.RewardFilterDto dto = mock(com.modive.adminservice.api.reward.dto.RewardFilterDto.class);
        String email = "test@test.com";
        String reason = "보상";
        java.time.LocalDate startDate = java.time.LocalDate.of(2024, 1, 1);
        java.time.LocalDate endDate = java.time.LocalDate.of(2024, 1, 31);
        int page = 1, pageSize = 10;

        when(rewardFetchService.fetchRewardFilter(userId, email, reason, startDate, endDate, page, pageSize)).thenReturn(dto);

        ResponseEntity<CommonRes<com.modive.adminservice.api.reward.dto.RewardFilterDto>> response =
                rewardController.getRewardFilter(userId, email, reason, startDate, endDate, page, pageSize);

        assertEquals("씨앗 필터링 조회에 성공하였습니다.", response.getBody().getMessage());
        assertEquals(dto, response.getBody().getData());
        verify(rewardFetchService).fetchRewardFilter(userId, email, reason, startDate, endDate, page, pageSize);
    }

    /****
     * Given: RewardFetchService가 RestApiException을 던질 때
     * When: getRewardFilter 메서드를 호출하면
     * Then: 동일한 RestApiException이 다시 던져진다.
     */
    @Test
    void getRewardFilter_whenServiceThrows_shouldPropagateException() {
        String email = "test@test.com";
        String reason = "보상";
        java.time.LocalDate startDate = java.time.LocalDate.of(2024, 1, 1);
        java.time.LocalDate endDate = java.time.LocalDate.of(2024, 1, 31);
        int page = 1, pageSize = 10;

        when(rewardFetchService.fetchRewardFilter(userId, email, reason, startDate, endDate, page, pageSize))
                .thenThrow(new RestApiException(ErrorCode.INTERNAL_SERVER_ERROR));

        RestApiException exception = assertThrows(RestApiException.class,
                () -> rewardController.getRewardFilter(userId, email, reason, startDate, endDate, page, pageSize));
        assertEquals(ErrorCode.INTERNAL_SERVER_ERROR, exception.getErrorCode());
        verify(rewardFetchService).fetchRewardFilter(userId, email, reason, startDate, endDate, page, pageSize);
    }

    /****
     * Given: RewardFetchService가 정상적으로 RewardMonthDto를 반환할 때
     * When: getRewardMonth 메서드를 호출하면
     * Then: 응답 메시지와 DTO가 포함된 CommonRes가 반환된다.
     */
    @Test
    void getRewardMonth_shouldReturnValidResponse() {
        RewardMonthDto dto = mock(RewardMonthDto.class);
        when(rewardFetchService.fetchRewardMonth(userId)).thenReturn(dto);

        ResponseEntity<CommonRes<RewardMonthDto>> response = rewardController.getRewardMonth(userId);

        assertEquals("월별 씨앗 지급 통계에 성공했습니다.", response.getBody().getMessage());
        assertEquals(dto, response.getBody().getData());
        verify(rewardFetchService).fetchRewardMonth(userId);
    }

    /****
     * Given: RewardFetchService가 RestApiException을 던질 때
     * When: getRewardMonth 메서드를 호출하면
     * Then: 동일한 RestApiException이 다시 던져진다.
     */
    @Test
    void getRewardMonth_whenServiceThrows_shouldPropagateException() {
        when(rewardFetchService.fetchRewardMonth(userId)).thenThrow(new RestApiException(ErrorCode.INTERNAL_SERVER_ERROR));

        RestApiException exception = assertThrows(RestApiException.class, () -> rewardController.getRewardMonth(userId));
        assertEquals(ErrorCode.INTERNAL_SERVER_ERROR, exception.getErrorCode());
        verify(rewardFetchService).fetchRewardMonth(userId);
    }

    /****
     * Given: RewardFetchService가 정상적으로 RewardHistoryDto를 반환할 때
     * When: getRewardHistory 메서드를 호출하면
     * Then: 응답 메시지와 DTO가 포함된 CommonRes가 반환된다.
     */
    @Test
    void getRewardHistory_shouldReturnValidResponse() {
        RewardHistoryDto dto = mock(RewardHistoryDto.class);
        int page = 0, pageSize = 5;
        when(rewardFetchService.fetchRewardHistory(userId, page, pageSize)).thenReturn(dto);

        ResponseEntity<CommonRes<RewardHistoryDto>> response =
                rewardController.getRewardHistory(userId, page, pageSize);

        assertEquals("최근 씨앗 발급 내역 조회에 성공했습니다.", response.getBody().getMessage());
        assertEquals(dto, response.getBody().getData());
        verify(rewardFetchService).fetchRewardHistory(userId, page, pageSize);
    }

    /****
     * Given: RewardFetchService가 RestApiException을 던질 때
     * When: getRewardHistory 메서드를 호출하면
     * Then: 동일한 RestApiException이 다시 던져진다.
     */
    @Test
    void getRewardHistory_whenServiceThrows_shouldPropagateException() {
        int page = 0, pageSize = 5;
        when(rewardFetchService.fetchRewardHistory(userId, page, pageSize)).thenThrow(new RestApiException(ErrorCode.INTERNAL_SERVER_ERROR));

        RestApiException exception = assertThrows(RestApiException.class,
                () -> rewardController.getRewardHistory(userId, page, pageSize));
        assertEquals(ErrorCode.INTERNAL_SERVER_ERROR, exception.getErrorCode());
        verify(rewardFetchService).fetchRewardHistory(userId, page, pageSize);
    }
}