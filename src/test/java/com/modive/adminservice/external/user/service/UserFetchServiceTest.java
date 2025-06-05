package com.modive.adminservice.external.user.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.modive.adminservice.api.user.dto.req.UserFilterReq;
import com.modive.adminservice.external.user.client.UserClient;
import com.modive.adminservice.external.user.dto.res.*;
import com.modive.adminservice.external.user.service.impl.UserFetchServiceImpl;
import com.modive.adminservice.global.dto.res.CommonRes;
import com.modive.adminservice.global.error.code.ErrorCode;
import com.modive.adminservice.global.error.exception.RestApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class UserFetchServiceTest {

    @Mock
    private UserClient userClient;

    @InjectMocks
    private UserFetchServiceImpl userFetchService;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * fetchUsers 테스트 - 정상 케이스
     * 입력: page=1, pageSize=10
     * 기대: userClient에서 받은 사용자 목록 반환
     * 검증: 반환된 리스트 크기 및 내용 일치
     */
    @Test
    void fetchUsers_shouldReturnUserList_whenResponseIsValid() {
        // given
        int page = 1;
        int pageSize = 10;

        UCUserListItem user1 = new UCUserListItem(/* 사용자 정보 세팅 */);
        UCUserListItem user2 = new UCUserListItem(/* 사용자 정보 세팅 */);

        UCUserListResData resData = new UCUserListResData();
        resData.setUserInfos(Arrays.asList(user1, user2));

        CommonRes<UCUserListResData> mockResponse = new CommonRes<>();
        mockResponse.setData(resData);

        when(userClient.getUserList(page, pageSize)).thenReturn(mockResponse);

        // when
        List<UCUserListItem> result = userFetchService.fetchUsers(page, pageSize);

        // then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(user1, result.get(0));
        assertEquals(user2, result.get(1));
    }

    /**
     * fetchUsers 테스트 - 응답이 null인 경우
     * 입력: 유효한 page, pageSize
     * 기대: RestApiException 발생
     * 검증: 예외 타입 및 ErrorCode 확인
     */
    @Test
    void fetchUsers_shouldThrowException_whenResponseIsNull() {
        // given
        when(userClient.getUserList(anyInt(), anyInt())).thenReturn(null);

        // when & then
        assertThrows(RestApiException.class, () -> userFetchService.fetchUsers(1, 10));
    }

    /**
     * fetchUsers 테스트 - 응답의 data가 null인 경우
     * 입력: 유효한 page, pageSize
     * 기대: RestApiException 발생
     * 검증: 예외 타입 및 ErrorCode 확인
     */
    @Test
    void fetchUsers_shouldThrowException_whenResponseDataIsNull() {
        // given
        CommonRes<UCUserListResData> mockResponse = new CommonRes<>();
        mockResponse.setData(null);

        when(userClient.getUserList(anyInt(), anyInt())).thenReturn(mockResponse);

        // when & then
        assertThrows(RestApiException.class, () -> userFetchService.fetchUsers(1, 10));
    }

    /**
     * fetchSearchUsers 테스트 - 정상 케이스
     * 입력: 검색어 keyword
     * 기대: 검색된 사용자 목록 반환
     * 검증: 리스트 크기 및 내용 확인
     */
    @Test
    void fetchSearchUsers_returnsUserList_whenResponseIsValid() {
        // given
        String keyword = "testUser";

        UCUserListItem user1 = new UCUserListItem();
        UCUserListItem user2 = new UCUserListItem();

        UCSearchUserResData resData = new UCSearchUserResData();
        resData.setSearchResult(Arrays.asList(user1, user2));

        CommonRes<UCSearchUserResData> mockRes = new CommonRes<>();
        mockRes.setData(resData);

        when(userClient.searchUser(keyword)).thenReturn(mockRes);

        // when
        List<UCUserListItem> result = userFetchService.fetchSearchUsers(keyword);

        // then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(user1, result.get(0));
        assertEquals(user2, result.get(1));
    }

    /**
     * fetchSearchUsers 테스트 - 응답이 null인 경우
     * 입력: 검색어 keyword
     * 기대: RestApiException 발생
     * 검증: 예외 코드 확인
     */
    @Test
    void fetchSearchUsers_throwsException_whenResponseIsNull() {
        // given
        when(userClient.searchUser(anyString())).thenReturn(null);

        // when & then
        RestApiException thrown = assertThrows(
                RestApiException.class,
                () -> userFetchService.fetchSearchUsers("someKeyword")
        );
        assertEquals(ErrorCode.FEIGN_DATA_MISSING, thrown.getErrorCode());
    }

    /**
     * fetchSearchUsers 테스트 - 응답의 data가 null인 경우
     * 입력: 검색어 keyword
     * 기대: RestApiException 발생
     * 검증: 예외 코드 확인
     */
    @Test
    void fetchSearchUsers_throwsException_whenResponseDataIsNull() {
        // given
        CommonRes<UCSearchUserResData> mockRes = new CommonRes<>();
        mockRes.setData(null);
        when(userClient.searchUser(anyString())).thenReturn(mockRes);

        // when & then
        RestApiException thrown = assertThrows(
                RestApiException.class,
                () -> userFetchService.fetchSearchUsers("someKeyword")
        );
        assertEquals(ErrorCode.FEIGN_DATA_MISSING, thrown.getErrorCode());
    }

    /**
     * fetchUserDetail 테스트 - 정상 케이스
     * 입력: userId
     * 기대: 상세 정보 리스트 반환 (size == 1)
     * 검증: 반환된 객체와 mock 객체 동일 여부 확인
     */
    @Test
    void fetchUserDetail_returnsUserDetailList_whenResponseIsValid() {
        // given
        String userId = "user123";

        UCUserListItem userItem = new UCUserListItem(); // 필요한 필드 세팅
        CommonRes<UCUserListItem> mockResponse = new CommonRes<>();
        mockResponse.setData(userItem);

        when(userClient.getUserDetail(userId)).thenReturn(mockResponse);

        // when
        List<UCUserListItem> result = userFetchService.fetchUserDetail(userId);

        // then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(userItem, result.get(0));
    }

    /**
     * fetchUserDetail 테스트 - 응답이 null인 경우
     * 입력: userId
     * 기대: 예외 발생
     * 검증: ErrorCode가 FEIGN_DATA_MISSING인지 확인
     */
    @Test
    void fetchUserDetail_throwsException_whenResponseIsNull() {
        // given
        when(userClient.getUserDetail(anyString())).thenReturn(null);

        // when & then
        RestApiException ex = assertThrows(
                RestApiException.class,
                () -> userFetchService.fetchUserDetail("user123")
        );
        assertEquals(ErrorCode.FEIGN_DATA_MISSING, ex.getErrorCode());
    }

    /**
     * fetchUserDetail 테스트 - 응답 data가 null인 경우
     * 입력: userId
     * 기대: 예외 발생
     * 검증: 예외 발생 여부 및 ErrorCode 확인
     */
    @Test
    void fetchUserDetail_throwsException_whenResponseDataIsNull() {
        // given
        CommonRes<UCUserListItem> mockResponse = new CommonRes<>();
        mockResponse.setData(null);
        when(userClient.getUserDetail(anyString())).thenReturn(mockResponse);

        // when & then
        RestApiException ex = assertThrows(
                RestApiException.class,
                () -> userFetchService.fetchUserDetail("user123")
        );
        assertEquals(ErrorCode.FEIGN_DATA_MISSING, ex.getErrorCode());
    }

    /**
     * fetchFilteredUser 테스트 - 정상 케이스
     * 입력: 필터링 요청 객체
     * 기대: 필터링된 사용자 결과 반환
     * 검증: 응답 데이터 내용과 동일한지
     */
    @Test
    void fetchFilteredUser_returnsFilteredData_whenResponseIsValid() {
        // given
        UserFilterReq filterParams = new UserFilterReq(); // 필드 설정 필요 시 setXXX 사용
        UCFilterUserResData expectedData = new UCFilterUserResData(); // 결과 객체도 적절히 세팅

        CommonRes<UCFilterUserResData> mockResponse = new CommonRes<>();
        mockResponse.setData(expectedData);

        when(userClient.getFilteredUser(filterParams)).thenReturn(mockResponse);

        // when
        UCFilterUserResData result = userFetchService.fetchFilteredUser(filterParams);

        // then
        assertNotNull(result);
        assertEquals(expectedData, result);
    }

    /**
     * fetchFilteredUser 테스트 - 응답이 null
     * 입력: 유효한 요청 객체
     * 기대: 예외 발생
     * 검증: 예외 코드 확인
     */
    @Test
    void fetchFilteredUser_throwsException_whenResponseIsNull() {
        // given
        UserFilterReq filterParams = new UserFilterReq();
        when(userClient.getFilteredUser(any())).thenReturn(null);

        // when & then
        RestApiException ex = assertThrows(
                RestApiException.class,
                () -> userFetchService.fetchFilteredUser(filterParams)
        );
        assertEquals(ErrorCode.FEIGN_DATA_MISSING, ex.getErrorCode());
    }

    /**
     * fetchFilteredUser 테스트 - 응답 data가 null
     * 입력: 유효한 요청 객체
     * 기대: 예외 발생
     * 검증: 예외 코드 확인
     */
    @Test
    void fetchFilteredUser_throwsException_whenResponseDataIsNull() {
        // given
        UserFilterReq filterParams = new UserFilterReq();
        CommonRes<UCFilterUserResData> mockResponse = new CommonRes<>();
        mockResponse.setData(null);

        when(userClient.getFilteredUser(any())).thenReturn(mockResponse);

        // when & then
        RestApiException ex = assertThrows(
                RestApiException.class,
                () -> userFetchService.fetchFilteredUser(filterParams)
        );
        assertEquals(ErrorCode.FEIGN_DATA_MISSING, ex.getErrorCode());
    }

    /**
     * inactiveUser 테스트 - 정상 케이스
     * 입력: userId
     * 기대: 예외 없이 메서드 완료
     * 검증: 예외 발생하지 않음
     */
    @Test
    void inactiveUser_shouldSucceed_whenResponseIsValid() {
        String userId = "user123";
        CommonRes mockRes = new CommonRes();
        mockRes.setData(new Object()); // 비어 있지 않게

        when(userClient.deleteUser(userId)).thenReturn(mockRes);

        assertDoesNotThrow(() -> userFetchService.inactiveUser(userId));
    }

    /**
     * inactiveUser 테스트 - 응답이 null
     * 입력: userId
     * 기대: 예외 발생
     * 검증: 예외 타입 및 코드 확인
     */
    @Test
    void inactiveUser_shouldThrow_whenResponseIsNull() {
        when(userClient.deleteUser(anyString())).thenReturn(null);
        assertThrows(RestApiException.class, () -> userFetchService.inactiveUser("user123"));
    }

    /**
     * inactiveUser 테스트 - data가 null
     * 입력: userId
     * 기대: 예외 발생
     * 검증: FEIGN_DATA_MISSING 코드 반환 여부
     */
    @Test
    void inactiveUser_shouldThrow_whenResponseDataIsNull() {
        CommonRes res = new CommonRes();
        res.setData(null);
        when(userClient.deleteUser(anyString())).thenReturn(res);
        assertThrows(RestApiException.class, () -> userFetchService.inactiveUser("user123"));
    }

    /**
     * fetchUserTotalCountAndChangeRate 테스트 - 정상 케이스
     * 입력: 없음
     * 기대: 총합 및 증감률 정보 반환
     * 검증: 반환 값과 mock 값 일치
     */
    @Test
    void fetchUserTotalCountAndChangeRate_shouldReturnData_whenValid() {
        UCTotalCntAndRateItem expected = new UCTotalCntAndRateItem();
        UCTotalUserResData data = new UCTotalUserResData();
        data.setTotalUserCount(expected);

        CommonRes<UCTotalUserResData> res = new CommonRes<>();
        res.setData(data);

        when(userClient.getTotalUser()).thenReturn(res);

        UCTotalCntAndRateItem result = userFetchService.fetchUserTotalCountAndChangeRate();
        assertEquals(expected, result);
    }

    /**
     * fetchUserTotalCountAndChangeRate 테스트 - data null
     * 입력: 없음
     * 기대: 예외 발생
     * 검증: FEIGN_DATA_MISSING 코드 확인
     */
    @Test
    void fetchUserTotalCountAndChangeRate_shouldThrow_whenDataIsNull() {
        CommonRes<UCTotalUserResData> res = new CommonRes<>();
        res.setData(null);
        when(userClient.getTotalUser()).thenReturn(res);
        assertThrows(RestApiException.class, () -> userFetchService.fetchUserTotalCountAndChangeRate());
    }

    /**
     * fetchDevicesTotalCountAndChangeRate 테스트 - 정상 케이스
     * 입력: 없음
     * 기대: 디바이스 총합 및 증감률 반환
     * 검증: 응답 값과 예상 값 일치
     */
    @Test
    void fetchDevicesTotalCountAndChangeRate_shouldReturnData_whenValid() {
        UCTotalCntAndRateItem expected = new UCTotalCntAndRateItem();
        UCTotalDeviceResData data = new UCTotalDeviceResData();
        data.setTotalCarCount(expected);

        CommonRes<UCTotalDeviceResData> res = new CommonRes<>();
        res.setData(data);

        when(userClient.getTotalUserCars()).thenReturn(res);

        UCTotalCntAndRateItem result = userFetchService.fetchDevicesTotalCountAndChangeRate();
        assertEquals(expected, result);
    }

    /**
     * fetchDevicesTotalCountAndChangeRate 테스트 - data null
     * 입력: 없음
     * 기대: 예외 발생
     * 검증: FEIGN_DATA_MISSING 코드 확인
     */
    @Test
    void fetchDevicesTotalCountAndChangeRate_shouldThrow_whenDataIsNull() {
        CommonRes<UCTotalDeviceResData> res = new CommonRes<>();
        res.setData(null);
        when(userClient.getTotalUserCars()).thenReturn(res);
        assertThrows(RestApiException.class, () -> userFetchService.fetchDevicesTotalCountAndChangeRate());
    }

    /**
     * fetchUserStatistics 테스트 - 정상 케이스
     * 입력: 없음
     * 기대: 사용자 통계 Map 반환 (summary, userTrend 포함)
     * 검증: 반환된 Map의 값이 mock 객체와 동일
     */
    @Test
    void fetchUserStatistics_shouldReturnMap_whenValid() {
        // 실제 타입으로 초기화
        UCUserStatisticsSummaryItem summary = new UCUserStatisticsSummaryItem(); // 혹은 builder로 생성
        UCUserMonthlyItem trendItem = new UCUserMonthlyItem(); // 필요 시 필드 세팅
        List<UCUserMonthlyItem> trend = new ArrayList<>();
        trend.add(trendItem);

        UCUserStatisticsResData stats = new UCUserStatisticsResData();
        stats.setSummary(summary);
        stats.setUserTrend(trend);

        UCUserStatisticsWrapper wrapper = new UCUserStatisticsWrapper();
        wrapper.setUserStatistics(stats);

        CommonRes<UCUserStatisticsWrapper> res = new CommonRes<>();
        res.setData(wrapper);

        when(userClient.getMonthlyStats()).thenReturn(res);

        Map<String, Object> result = userFetchService.fetchUserStatistics();
        assertEquals(summary, result.get("summary"));
        assertEquals(trend, result.get("userTrend"));
    }

    /**
     * fetchUserStatistics 테스트 - data null
     * 입력: 없음
     * 기대: 예외 발생
     * 검증: 예외 타입 및 코드 확인
     */
    @Test
    void fetchUserStatistics_shouldThrow_whenDataIsNull() {
        CommonRes<UCUserStatisticsWrapper> res = new CommonRes<>();
        res.setData(null);
        when(userClient.getMonthlyStats()).thenReturn(res);
        assertThrows(RestApiException.class, () -> userFetchService.fetchUserStatistics());
    }
}
