package com.modive.adminservice.api.reward.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.modive.adminservice.api.reward.dto.*;
import com.modive.adminservice.external.reward.service.RewardFetchService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {"spring.config.location=classpath:application-test.yml"})
@AutoConfigureMockMvc
public class RewardControllerComponentTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private RewardFetchService rewardFetchService;

    private final String TEST_USER_ID = "test-user-id";

    @Test
    @DisplayName("리워드 요약 정보 조회 성공")
    void getSummarySuccess() throws Exception {
        // given
        RewardsSummaryDto summaryDto = createMockSummaryDto();
        when(rewardFetchService.fetchRewardSummary(anyString())).thenReturn(summaryDto);

        // when & then
        mockMvc.perform(get("/admin/rewards/summary")
                        .header("X-User-Id", TEST_USER_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.totalIssued.value").value(100000))
                .andExpect(jsonPath("$.data.monthlyIssued.value").value(5000))
                .andExpect(jsonPath("$.data.dailyAverageIssued.value").value(200))
                .andExpect(jsonPath("$.data.perUserAverageIssued.value").value(50));
    }

    @Test
    @DisplayName("발급 사유별 총 통계 조회 성공")
    void getRewardByReasonTotalSuccess() throws Exception {
        // given
        RewardByReasonTotalDto totalDto = createMockRewardByReasonTotalDto();
        when(rewardFetchService.fetchRewardByReasonTotal(anyString())).thenReturn(totalDto);

        // when & then
        mockMvc.perform(get("/admin/rewards/by-reason/total")
                        .header("X-User-Id", TEST_USER_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.totalRewardStatistics[0].reason").value("종합점수"))
                .andExpect(jsonPath("$.data.totalRewardStatistics[0].count").value(500))
                .andExpect(jsonPath("$.data.totalRewardStatistics[0].ratio").value(50.0));
    }

    @Test
    @DisplayName("발급 사유별 월별 통계 조회 성공")
    void getRewardByReasonMonthSuccess() throws Exception {
        // given
        RewardByReasonMonthDto monthDto = createMockRewardByReasonMonthDto();
        when(rewardFetchService.fetchRewardByReasonMonth(anyString(), anyInt(), anyInt())).thenReturn(monthDto);

        // when & then
        mockMvc.perform(get("/admin/rewards/by-reason/monthly-stats")
                        .header("X-User-Id", TEST_USER_ID)
                        .param("year", "2023")
                        .param("month", "5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.monthlyRewardStatistics[0].reason").value("종합점수"))
                .andExpect(jsonPath("$.data.monthlyRewardStatistics[0].count").value(300))
                .andExpect(jsonPath("$.data.monthlyRewardStatistics[0].ratio").value(45.0));
    }

    @Test
    @DisplayName("월별 씨앗 지급 통계 조회 성공")
    void getRewardMonthSuccess() throws Exception {
        // given
        RewardMonthDto rewardMonthDto = createMockRewardMonthDto();
        when(rewardFetchService.fetchRewardMonth(anyString())).thenReturn(rewardMonthDto);

        // when & then
        mockMvc.perform(get("/admin/rewards/monthly-stats")
                        .header("X-User-Id", TEST_USER_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.monthlyRewardStatistics[0].year").value(2023))
                .andExpect(jsonPath("$.data.monthlyRewardStatistics[0].month").value(5))
                .andExpect(jsonPath("$.data.monthlyRewardStatistics[0].amount").value(1200));
    }

    @Test
    @DisplayName("최근 씨앗 발급 내역 조회 성공")
    void getRewardHistorySuccess() throws Exception {
        // given
        RewardHistoryDto historyDto = createMockRewardHistoryDto();
        when(rewardFetchService.fetchRewardHistory(anyString(), anyInt(), anyInt())).thenReturn(historyDto);

        // when & then
        mockMvc.perform(get("/admin/rewards/history")
                        .header("X-User-Id", TEST_USER_ID)
                        .param("page", "0")
                        .param("pageSize", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.rewardHistory[0].rewardId").value("reward-id-1"))
                .andExpect(jsonPath("$.data.rewardHistory[0].issuedDate").value("2023-05-01"))
                .andExpect(jsonPath("$.data.rewardHistory[0].reason").value("종합점수"))
                .andExpect(jsonPath("$.data.rewardHistory[0].amount").value(100))
                .andExpect(jsonPath("$.data.pageInfo.currentPage").value(0))
                .andExpect(jsonPath("$.data.pageInfo.pageSize").value(10))
                .andExpect(jsonPath("$.data.pageInfo.totalElements").value(100))
                .andExpect(jsonPath("$.data.pageInfo.totalPages").value(10));
    }

    @Test
    @DisplayName("씨앗 필터링 조회 성공")
    void getRewardFilterSuccess() throws Exception {
        // given
        RewardFilterDto filterDto = createMockRewardFilterDto();
        when(rewardFetchService.fetchRewardFilter(
                anyString(), anyString(), anyString(), any(LocalDate.class), any(LocalDate.class), anyInt(), anyInt()
        )).thenReturn(filterDto);

        // when & then
        mockMvc.perform(get("/admin/rewards")
                        .header("X-User-Id", TEST_USER_ID)
                        .param("email", "user@example.com")
                        .param("reason", "종합점수")  // 'description' 파라미터 이름을 'reason'으로 수정
                        .param("startDate", "2023-01-01")
                        .param("endDate", "2023-12-31")
                        .param("pageSize", "10")
                        .param("page", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.searchResult[0].rewardId").value("reward-id-1"))
                .andExpect(jsonPath("$.data.searchResult[0].userId").value("user-id-1"))
                .andExpect(jsonPath("$.data.searchResult[0].createdAt").value("2023-05-01"))  // LocalDate 형식 확인
                .andExpect(jsonPath("$.data.searchResult[0].description").value("종합점수"))  // 필드명 주의 (Jackson은 기본적으로 camelCase를 사용)
                .andExpect(jsonPath("$.data.searchResult[0].amount").value(100));

    }

    // 테스트에 필요한 Mock 객체 생성 메서드들
    private RewardsSummaryDto createMockSummaryDto() {
        return new RewardsSummaryDto(
                new RewardsSummaryDto.RewardMetric(100000, 5.2),
                new RewardsSummaryDto.RewardMetric(5000, 3.1),
                new RewardsSummaryDto.RewardMetric(200, 1.5),
                new RewardsSummaryDto.RewardMetric(50, 2.0)
        );
    }

    private RewardByReasonTotalDto createMockRewardByReasonTotalDto() {
        List<RewardByReasonTotalDto.TotalRewardStaticsItemDto> items = new ArrayList<>();
        items.add(new RewardByReasonTotalDto.TotalRewardStaticsItemDto("종합점수", 500, 50.0));
        items.add(new RewardByReasonTotalDto.TotalRewardStaticsItemDto("일일 미션", 300, 30.0));
        items.add(new RewardByReasonTotalDto.TotalRewardStaticsItemDto("주간 미션", 200, 20.0));
        return new RewardByReasonTotalDto(items);
    }

    private RewardByReasonMonthDto createMockRewardByReasonMonthDto() {
        List<RewardByReasonMonthDto.MonthRewardStaticsItemDto> items = new ArrayList<>();
        items.add(new RewardByReasonMonthDto.MonthRewardStaticsItemDto("종합점수", 300, 45.0));
        items.add(new RewardByReasonMonthDto.MonthRewardStaticsItemDto("일일 미션", 200, 30.0));
        items.add(new RewardByReasonMonthDto.MonthRewardStaticsItemDto("주간 미션", 150, 25.0));
        return new RewardByReasonMonthDto(items);
    }

    private RewardMonthDto createMockRewardMonthDto() {
        List<RewardMonthDto.MonthlyRewardStatistic> items = new ArrayList<>();
        items.add(new RewardMonthDto.MonthlyRewardStatistic(2023, 5, 1200));
        items.add(new RewardMonthDto.MonthlyRewardStatistic(2023, 4, 1100));
        items.add(new RewardMonthDto.MonthlyRewardStatistic(2023, 3, 1000));
        return new RewardMonthDto(items);
    }

    private RewardHistoryDto createMockRewardHistoryDto() {
        List<RewardHistoryDto.RewardHistoryItem> items = new ArrayList<>();
        items.add(new RewardHistoryDto.RewardHistoryItem("reward-id-1", "2023-05-01", "종합점수", 100));
        items.add(new RewardHistoryDto.RewardHistoryItem("reward-id-2", "2023-05-02", "일일 미션", 50));
        items.add(new RewardHistoryDto.RewardHistoryItem("reward-id-3", "2023-05-03", "주간 미션", 200));

        RewardHistoryDto.PageInfo pageInfo = new RewardHistoryDto.PageInfo(0, 10, 100, 10);
        return new RewardHistoryDto(items, pageInfo);
    }

    private RewardFilterDto createMockRewardFilterDto() {
        List<RewardFilterDto.FilteredRewardResultItem> items = new ArrayList<>();
        items.add(new RewardFilterDto.FilteredRewardResultItem(
                "reward-id-1",
                "user-id-1",
                LocalDate.of(2023, 5, 1),
                "종합점수",  // Description 필드에 맞게 수정
                100
        ));
        items.add(new RewardFilterDto.FilteredRewardResultItem(
                "reward-id-2",
                "user-id-2",
                LocalDate.of(2023, 5, 2),
                "일일 미션",  // Description 필드에 맞게 수정
                50
        ));
        return new RewardFilterDto(items);
    }

}