package com.modive.adminservice.unit;

import com.modive.adminservice.api.reward.dto.*;
import com.modive.adminservice.external.reward.client.RewardClient;
import com.modive.adminservice.external.reward.service.RewardFetchService;
import com.modive.adminservice.external.reward.dto.req.RCRewardByDriveReq;
import com.modive.adminservice.external.reward.dto.req.RCRewardFilterReq;
import com.modive.adminservice.external.reward.dto.res.*;
import com.modive.adminservice.external.reward.service.impl.RewardFetchServiceImpl;
import com.modive.adminservice.global.dto.res.CommonRes;
import com.modive.adminservice.global.error.code.ErrorCode;
import com.modive.adminservice.global.error.exception.RestApiException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RewardFetchServiceTest {

    @Mock
    private RewardClient rewardClient;

    @InjectMocks
    private RewardFetchServiceImpl rewardFetchService;

    private final String userId = "testUser";

    /**
     * fetchRewardFilter 테스트 - 정상 케이스
     * 입력: 정상적인 RCRewardFilterReq
     * 기대: 응답의 searchResult 리스트를 그대로 반환
     * 검증: 반환된 리스트 크기와 내용이 맞는지
     */
    @Test
    void fetchRewardFilter_whenValidResponse_shouldReturnFilterItems() {
        // given
        RCRewardFilterReq req = new RCRewardFilterReq();
        List<RCRewardFilterItem> expectedItems = Arrays.asList(
                new RCRewardFilterItem(),
                new RCRewardFilterItem()
        );

        RCRewardFilterResData resData = new RCRewardFilterResData();
        resData.setSearchResult(expectedItems);

        CommonRes<RCRewardFilterResData> response = new CommonRes<>();
        response.setData(resData);

        when(rewardClient.filterReward(userId, req)).thenReturn(response);

        // when
        List<RCRewardFilterItem> result = rewardFetchService.fetchRewardFilter(userId, req);

        // then
        assertEquals(expectedItems, result);
        assertEquals(2, result.size());
        verify(rewardClient).filterReward(userId, req);
    }

    /**
     * fetchRewardFilter 테스트 - 예외 상황 1
     * 입력: RewardClient가 null 반환
     * 기대: RestApiException 발생
     * 검증: 예외 타입과 ErrorCode가 FEIGN_DATA_MISSING인지
     */
    @Test
    void fetchRewardFilter_whenResponseIsNull_shouldThrowException() {
        // given
        RCRewardFilterReq req = new RCRewardFilterReq();
        when(rewardClient.filterReward(userId, req)).thenReturn(null);

        // when & then
        RestApiException exception = assertThrows(RestApiException.class,
                () -> rewardFetchService.fetchRewardFilter(userId, req));

        assertEquals(ErrorCode.FEIGN_DATA_MISSING, exception.getErrorCode());
    }

    /**
     * fetchRewardFilter 테스트 - 예외 상황 2
     * 입력: CommonRes 객체는 있지만 data가 null인 경우
     * 기대: RestApiException 발생
     * 검증: 예외 타입과 ErrorCode가 FEIGN_DATA_MISSING인지
     */
    @Test
    void fetchRewardFilter_whenDataIsNull_shouldThrowException() {
        // given
        RCRewardFilterReq req = new RCRewardFilterReq();
        CommonRes<RCRewardFilterResData> response = new CommonRes<>();
        response.setData(null);

        when(rewardClient.filterReward(userId, req)).thenReturn(response);

        // when & then
        RestApiException exception = assertThrows(RestApiException.class,
                () -> rewardFetchService.fetchRewardFilter(userId, req));

        assertEquals(ErrorCode.FEIGN_DATA_MISSING, exception.getErrorCode());
    }

    /**
     * fetchRewardMapByDrive 테스트 - 데이터 변환 로직
     * 비즈니스 로직: List<RCRewardByDriveItem>을 Map<String, Integer>로 변환
     * 검증: 변환된 맵의 크기와 각 key-value 값이 정확한지
     */
    @Test
    void fetchRewardMapByDrive_whenValidResponse_shouldReturnMap() {
        // given
        RCRewardByDriveReq req = new RCRewardByDriveReq();

        List<RCRewardByDriveItem> items = Arrays.asList(
                createRewardByDriveItem("drive1", 100),
                createRewardByDriveItem("drive2", 200)
        );

        RCRewardByDriveResData resData = new RCRewardByDriveResData();
        resData.setRewardsByDrive(items);

        CommonRes<RCRewardByDriveResData> response = new CommonRes<>();
        response.setData(resData);

        when(rewardClient.getRewardByDrive(userId, req)).thenReturn(response);

        // when
        Map<String, Integer> result = rewardFetchService.fetchRewardMapByDrive(userId, req);

        // then
        assertEquals(2, result.size());
        assertEquals(100, result.get("drive1"));
        assertEquals(200, result.get("drive2"));
        verify(rewardClient).getRewardByDrive(userId, req);
    }

    /**
     * fetchRewardMapByDrive 테스트 - 경계값 테스트
     * 입력: 빈 리스트
     * 기대: 빈 맵 반환
     * 검증: 맵이 비어있는지 확인
     */
    @Test
    void fetchRewardMapByDrive_whenEmptyList_shouldReturnEmptyMap() {
        // given
        RCRewardByDriveReq req = new RCRewardByDriveReq();

        RCRewardByDriveResData resData = new RCRewardByDriveResData();
        resData.setRewardsByDrive(Arrays.asList());

        CommonRes<RCRewardByDriveResData> response = new CommonRes<>();
        response.setData(resData);

        when(rewardClient.getRewardByDrive(userId, req)).thenReturn(response);

        // when
        Map<String, Integer> result = rewardFetchService.fetchRewardMapByDrive(userId, req);

        // then
        assertTrue(result.isEmpty());
    }

    /**
     * fetchTotalIssuedRewards 테스트 - 단순 위임
     * 비즈니스 로직: 단순히 res.getData().getTotalIssued() 반환
     * 검증: 반환값이 예상된 객체와 일치하는지
     */
    @Test
    void fetchTotalIssuedRewards_whenValidResponse_shouldReturnItem() {
        // given
        RCRewardTotalCntAndRateItem expectedItem = new RCRewardTotalCntAndRateItem();

        RCTotalRewardResData resData = new RCTotalRewardResData();
        resData.setTotalIssued(expectedItem);

        CommonRes<RCTotalRewardResData> response = new CommonRes<>();
        response.setData(resData);

        when(rewardClient.getTotalIssuedRewards(userId)).thenReturn(response);

        // when
        RCRewardTotalCntAndRateItem result = rewardFetchService.fetchTotalIssuedRewards(userId);

        // then
        assertEquals(expectedItem, result);
        verify(rewardClient).getTotalIssuedRewards(userId);
    }

    /**
     * fetchRewardSummary 테스트 - 복잡한 병합 로직
     * 비즈니스 로직: 4개의 다른 API 호출 결과를 하나의 DTO로 병합
     * 검증: 각 필드가 올바른 값으로 설정되었는지
     */
    @Test
    void fetchRewardSummary_whenAllResponsesValid_shouldMergeCorrectly() {
        // given
        RewardsSummaryDto.RewardMetric totalMetric = new RewardsSummaryDto.RewardMetric();
        // totalMetric에 필요한 값들 설정 (실제 RewardMetric 구조에 맞게)

        RewardsSummaryDto.RewardMetric monthlyMetric = new RewardsSummaryDto.RewardMetric();
        RewardsSummaryDto.RewardMetric dailyMetric = new RewardsSummaryDto.RewardMetric();
        RewardsSummaryDto.RewardMetric perUserMetric = new RewardsSummaryDto.RewardMetric();

        RewardsSummaryDto totalDto = new RewardsSummaryDto();
        totalDto.setTotalIssued(totalMetric);

        RewardsSummaryDto monthlyDto = new RewardsSummaryDto();
        monthlyDto.setMonthlyIssued(monthlyMetric);

        RewardsSummaryDto dailyDto = new RewardsSummaryDto();
        dailyDto.setDailyAverageIssued(dailyMetric);

        RewardsSummaryDto perUserDto = new RewardsSummaryDto();
        perUserDto.setPerUserAverageIssued(perUserMetric);

        when(rewardClient.fetchRewardSummaryTotal(userId))
                .thenReturn(createCommonRes(totalDto));
        when(rewardClient.fetchRewardSummaryMonthly(userId))
                .thenReturn(createCommonRes(monthlyDto));
        when(rewardClient.fetchRewardSummaryDaily(userId))
                .thenReturn(createCommonRes(dailyDto));
        when(rewardClient.fetchRewardSummaryPerUser(userId))
                .thenReturn(createCommonRes(perUserDto));

        // when
        RewardsSummaryDto result = rewardFetchService.fetchRewardSummary(userId);

        // then
        assertEquals(totalMetric, result.getTotalIssued());
        assertEquals(monthlyMetric, result.getMonthlyIssued());
        assertEquals(dailyMetric, result.getDailyAverageIssued());
        assertEquals(perUserMetric, result.getPerUserAverageIssued());

        verify(rewardClient).fetchRewardSummaryTotal(userId);
        verify(rewardClient).fetchRewardSummaryMonthly(userId);
        verify(rewardClient).fetchRewardSummaryDaily(userId);
        verify(rewardClient).fetchRewardSummaryPerUser(userId);
    }

    /**
     * fetchRewardSummary 테스트 - null 안전성
     * 시나리오: monthly 응답만 null인 경우
     * 기대: totalIssued는 설정되고, monthlyIssued는 null 유지
     * 검증: 일부 응답이 null이어도 다른 값들은 제대로 설정되는지
     */
    @Test
    void fetchRewardSummary_whenSomeResponsesNull_shouldHandleGracefully() {
        // given
        RewardsSummaryDto.RewardMetric totalMetric = new RewardsSummaryDto.RewardMetric();

        RewardsSummaryDto totalDto = new RewardsSummaryDto();
        totalDto.setTotalIssued(totalMetric);

        when(rewardClient.fetchRewardSummaryTotal(userId))
                .thenReturn(createCommonRes(totalDto));
        when(rewardClient.fetchRewardSummaryMonthly(userId))
                .thenReturn(createCommonRes(null)); // null data
        when(rewardClient.fetchRewardSummaryDaily(userId))
                .thenReturn(createCommonRes(new RewardsSummaryDto()));
        when(rewardClient.fetchRewardSummaryPerUser(userId))
                .thenReturn(createCommonRes(new RewardsSummaryDto()));

        // when
        RewardsSummaryDto result = rewardFetchService.fetchRewardSummary(userId);

        // then
        assertEquals(totalMetric, result.getTotalIssued());
        assertNull(result.getMonthlyIssued()); // null인 경우 처리
    }

    /**
     *  fetchRewardByReasonMonth 테스트
     *  비즈니스 로직: int year, int month → "YYYY-MM" 문자열 변환
     *  검증 포인트: String.format("%d-%02d", year, month) 로직이 올바른지
     **/
    @Test
    void fetchRewardByReasonMonth_shouldFormatDateCorrectly() {
        // given
        int year = 2025;
        int month = 6;
        String expectedDateFormat = "2025-06";

        RewardByReasonMonthDto expectedDto = new RewardByReasonMonthDto();
        CommonRes<RewardByReasonMonthDto> response = createCommonRes(expectedDto);

        when(rewardClient.fetchRewardByReasonMonth(userId, expectedDateFormat))
                .thenReturn(response);

        // when
        RewardByReasonMonthDto result = rewardFetchService.fetchRewardByReasonMonth(userId, year, month);

        // then
        assertEquals(expectedDto, result);
        verify(rewardClient).fetchRewardByReasonMonth(userId, expectedDateFormat);
    }

    /**
     * 날짜 포맷 변환 로직 테스트 - 한 자리 월 (경계값 테스트)
     * 비즈니스 로직: 한 자리 월에 대해 0 패딩이 올바르게 적용되는지 검증
     * 검증 포인트: month=1이 "01"로 변환되는지 (not "1")
     */
    @Test
    void fetchRewardByReasonMonth_withSingleDigitMonth_shouldPadZero() {
        // given
        int year = 2025;
        int month = 1; // 한 자리 월
        String expectedDateFormat = "2025-01"; // 0 패딩

        RewardByReasonMonthDto expectedDto = new RewardByReasonMonthDto();
        CommonRes<RewardByReasonMonthDto> response = createCommonRes(expectedDto);

        when(rewardClient.fetchRewardByReasonMonth(userId, expectedDateFormat))
                .thenReturn(response);

        // when
        RewardByReasonMonthDto result = rewardFetchService.fetchRewardByReasonMonth(userId, year, month);

        // then
        assertEquals(expectedDto, result);
        verify(rewardClient).fetchRewardByReasonMonth(userId, expectedDateFormat);
    }

    /**
     * 매개변수 전달 테스트 - 모든 파라미터 포함
     * 비즈니스 로직: 여러 매개변수를 RewardClient에 올바른 순서로 전달
     * 검증 포인트: 매개변수 순서와 값이 정확히 전달되는지
     */
    @Test
    void fetchRewardFilter_withAllParams_shouldPassCorrectly() {
        // given
        String email = "test@example.com";
        String description = "test description";
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 1, 31);
        int page = 1;
        int size = 10;

        RewardFilterDto expectedDto = new RewardFilterDto();
        CommonRes<RewardFilterDto> response = createCommonRes(expectedDto);

        when(rewardClient.fetchRewardFilter(userId, email, description, startDate, endDate, page, size))
                .thenReturn(response);

        // when
        RewardFilterDto result = rewardFetchService.fetchRewardFilter(
                userId, email, description, startDate, endDate, page, size);

        // then
        assertEquals(expectedDto, result);
        verify(rewardClient).fetchRewardFilter(userId, email, description, startDate, endDate, page, size);
    }

    // ========== Helper Methods ==========

    /**
     * 테스트용 RCRewardByDriveItem 객체 생성 헬퍼
     * @param driveId 드라이브 ID
     * @param reward 리워드 값
     * @return 설정된 RCRewardByDriveItem 객체
     */
    private RCRewardByDriveItem createRewardByDriveItem(String driveId, Integer reward) {
        RCRewardByDriveItem item = new RCRewardByDriveItem();
        item.setDriveId(driveId);
        item.setReward(reward);
        return item;
    }

    /**
     * 테스트용 CommonRes 래퍼 객체 생성 헬퍼
     * @param data 래핑할 데이터
     * @param <T> 데이터 타입
     * @return data가 설정된 CommonRes 객체
     */
    private <T> CommonRes<T> createCommonRes(T data) {
        CommonRes<T> response = new CommonRes<>();
        response.setData(data);
        return response;
    }
}