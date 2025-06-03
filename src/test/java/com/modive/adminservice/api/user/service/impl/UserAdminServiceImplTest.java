package com.modive.adminservice.api.user.service.impl;

import com.modive.adminservice.api.user.dto.req.UserFilterReq;
import com.modive.adminservice.api.user.dto.res.UserDriveListRes;
import com.modive.adminservice.api.user.dto.res.UserListItem;
import com.modive.adminservice.api.user.dto.res.UserRewardItem;
import com.modive.adminservice.external.analysis.dto.EventsByDriveDTO;
import com.modive.adminservice.external.analysis.service.AnalysisFetchService;
import com.modive.adminservice.external.dashboard.dto.res.DCDriveListItem;
import com.modive.adminservice.external.dashboard.dto.res.DCDriveListResData;
import com.modive.adminservice.external.dashboard.service.DashboardFetchService;
import com.modive.adminservice.external.reward.dto.req.RCRewardFilterReq;
import com.modive.adminservice.external.reward.dto.res.RCRewardFilterItem;
import com.modive.adminservice.external.reward.service.RewardFetchService;
import com.modive.adminservice.external.user.dto.res.UCFilterUserResData;
import com.modive.adminservice.external.user.dto.res.UCUserListItem;
import com.modive.adminservice.external.user.service.UserFetchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserAdminServiceImplTest {

    @Mock
    private UserFetchService userFetchService;
    @Mock
    private DashboardFetchService dashboardFetchService;
    @Mock
    private AnalysisFetchService analysisFetchService;
    @Mock
    private RewardFetchService rewardFetchService;
    @InjectMocks
    private UserAdminServiceImpl userAdminServiceImpl;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    /**
     * adminSearchUser 테스트
     * 시나리오: 검색 키워드에 해당하는 사용자가 없는 경우
     * 기대: 빈 리스트 반환, 관련 서비스 메소드 정상 호출 확인
     */
    @Test
    void testAdminSearchUser_emptyResult() {
        String searchKeyword = "unmatched";

        when(userFetchService.fetchSearchUsers(searchKeyword)).thenReturn(Collections.emptyList());

        List<UserListItem> result = userAdminServiceImpl.adminSearchUser(searchKeyword);

        assertEquals(0, result.size());
        verify(userFetchService, times(1)).fetchSearchUsers(searchKeyword);
        verify(dashboardFetchService, times(1)).fetchDriveCountMap(eq(Collections.emptyList()));
    }

    /**
     * adminSearchUser 테스트 - 정상 사용자 검색
     * 시나리오: 검색 키워드에 해당하는 사용자가 존재하는 경우
     * 기대: 사용자 정보가 변환되어 반환되고, 각 필드가 올바르게 매핑되는지 검증
     */
    @Test
    void testAdminSearchUser_successfullyFindUsers() {
        String searchKeyword = "test";

        UCUserListItem mockUser1 = UCUserListItem.builder()
                .userId("user1")
                .nickname("Test User")
                .email("testuser@example.com")
                .experience(150)
                .joinedAt("2023-03-01")
                .isActive(1)
                .seedBalance(700)
                .build();

        List<UCUserListItem> mockUsers = Collections.singletonList(mockUser1);
        when(userFetchService.fetchSearchUsers(searchKeyword)).thenReturn(mockUsers);

        Map<String, Integer> driveCountMap = Map.of("user1", 8);
        when(dashboardFetchService.fetchDriveCountMap(Collections.singletonList("user1"))).thenReturn(driveCountMap);

        List<UserListItem> result = userAdminServiceImpl.adminSearchUser(searchKeyword);

        assertEquals(1, result.size());
        UserListItem user = result.get(0);
        assertEquals("user1", user.getUserId());
        assertEquals("Test User", user.getNickname());
        assertEquals("testuser@example.com", user.getEmail());
        assertEquals(150, user.getExperience());
        assertEquals("2023-03-01", user.getJoinedAt());
        assertEquals(1, user.getIsActive());
        assertEquals(700, user.getSeedBalance());
        assertEquals(8, user.getDriveCount());

        verify(userFetchService, times(1)).fetchSearchUsers(searchKeyword);
        verify(dashboardFetchService, times(1)).fetchDriveCountMap(Collections.singletonList("user1"));
    }

    /**
     * adminFilterUser 테스트 - 필터 조건 만족 사용자 조회
     * 시나리오: 필터 조건으로 사용자 조회 성공
     * 기대: 반환된 데이터가 요청과 일치하며 내부 서비스 호출 정상 수행됨
     */
    @Test
    void testAdminFilterUser_successfulFiltering() {
        UserFilterReq filterReq = new UserFilterReq();
        filterReq.setMinExperience(6);
        filterReq.setMaxExperience(24);
        filterReq.setAccountAgeInMonths(12);
        filterReq.setActive(1);
        filterReq.setPage(1);
        filterReq.setPageSize(10);

        UCFilterUserResData mockFilteredData = UCFilterUserResData.builder().build();

        when(userFetchService.fetchFilteredUser(filterReq)).thenReturn(mockFilteredData);

        UCFilterUserResData result = userAdminServiceImpl.adminFilterUser(filterReq);

        assertEquals(mockFilteredData, result);
        verify(userFetchService, times(1)).fetchFilteredUser(filterReq);
    }

    /**
     * adminFilterUser 테스트 - userFetchService 예외 발생
     * 시나리오: 내부 서비스에서 RuntimeException 발생
     * 기대: 예외가 호출자에게 전파되고, 호출 횟수 검증
     */
    @Test
    void testAdminFilterUser_userFetchServiceException() {
        UserFilterReq filterReq = new UserFilterReq();
        filterReq.setMinExperience(6);
        filterReq.setMaxExperience(24);
        filterReq.setAccountAgeInMonths(12);
        filterReq.setActive(1);
        filterReq.setPage(1);
        filterReq.setPageSize(10);

        when(userFetchService.fetchFilteredUser(filterReq))
                .thenThrow(new RuntimeException("User service error"));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userAdminServiceImpl.adminFilterUser(filterReq));

        assertEquals("User service error", exception.getMessage());
        verify(userFetchService, times(1)).fetchFilteredUser(filterReq);
    }

    /**
     * adminSearchUser 테스트 - dashboardFetchService 예외 발생
     * 시나리오: user는 조회되었지만 dashboard 서비스 실패
     * 기대: 예외가 전파되고 호출 순서 검증
     */
    @Test
    void testAdminSearchUser_dashboardFetchServiceException() {
        String searchKeyword = "test";

        UCUserListItem mockUser = UCUserListItem.builder()
                .userId("user1")
                .nickname("Test User")
                .email("testuser@example.com")
                .experience(150)
                .joinedAt("2023-03-01")
                .isActive(1)
                .seedBalance(700)
                .build();

        List<UCUserListItem> mockUsers = Collections.singletonList(mockUser);
        when(userFetchService.fetchSearchUsers(searchKeyword)).thenReturn(mockUsers);
        when(dashboardFetchService.fetchDriveCountMap(Collections.singletonList("user1")))
                .thenThrow(new RuntimeException("Dashboard service error"));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userAdminServiceImpl.adminSearchUser(searchKeyword));

        assertEquals("Dashboard service error", exception.getMessage());
        verify(userFetchService, times(1)).fetchSearchUsers(searchKeyword);
        verify(dashboardFetchService, times(1)).fetchDriveCountMap(Collections.singletonList("user1"));
    }


    /**
     * adminSearchUser 테스트 - userFetchService 예외 발생
     * 시나리오: 사용자 조회 자체에 실패한 경우
     * 기대: 예외 발생, 후속 호출이 생략됨
     */
    @Test
    void testAdminSearchUser_userFetchServiceException() {
        String searchKeyword = "test";

        when(userFetchService.fetchSearchUsers(searchKeyword)).thenThrow(new RuntimeException("User service error"));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userAdminServiceImpl.adminSearchUser(searchKeyword));

        assertEquals("User service error", exception.getMessage());
        verify(userFetchService, times(1)).fetchSearchUsers(searchKeyword);
        verify(dashboardFetchService, never()).fetchDriveCountMap(anyList());
    }

    /**
     * adminInactiveUser 테스트 - 정상 처리
     * 시나리오: 사용자 비활성화가 성공적으로 처리됨
     * 기대: 서비스 호출 확인, 예외 없음
     */
    @Test
    void testAdminInactiveUser_successfulInactivation() {
        String userId = "user1";

        doNothing().when(userFetchService).inactiveUser(userId);

        userAdminServiceImpl.adminInactiveUser(userId);

        verify(userFetchService, times(1)).inactiveUser(userId);
    }

    /**
     * adminInactiveUser 테스트 - 예외 처리
     * 시나리오: 사용자 비활성화 중 서비스 예외 발생
     * 기대: 예외 전파 및 호출 여부 검증
     */
    @Test
    void testAdminInactiveUser_serviceException() {
        String userId = "user1";

        doThrow(new RuntimeException("Error inactivating user")).when(userFetchService).inactiveUser(userId);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userAdminServiceImpl.adminInactiveUser(userId));

        assertEquals("Error inactivating user", exception.getMessage());
        verify(userFetchService, times(1)).inactiveUser(userId);
    }

    /**
     * adminGetUserList 테스트 - 사용자 리스트가 비어 있는 경우
     * 기대: 빈 결과 반환 및 호출 정상 수행 확인
     */
    @Test
    void testAdminGetUserList_emptyUsers() {
        int page = 1, pageSize = 10;

        when(userFetchService.fetchUsers(page, pageSize)).thenReturn(Collections.emptyList());

        List<UserListItem> result = userAdminServiceImpl.adminGetUserList(page, pageSize);

        assertEquals(0, result.size());
        verify(userFetchService, times(1)).fetchUsers(page, pageSize);
        verify(dashboardFetchService, times(1)).fetchDriveCountMap(eq(Collections.emptyList()));
    }

    /**
     * adminGetUserList 테스트 - driveCount 정보가 0인 경우
     * 시나리오: 사용자 정보는 존재하나 주행 수가 0으로 조회됨
     * 기대: 각 필드 값 매핑 검증
     */
    @Test
    void testAdminGetUserList_emptyDriveCountMap() {
        int page = 1, pageSize = 10;

        UCUserListItem mockUser = UCUserListItem.builder()
                .userId("user1")
                .nickname("User One")
                .email("user1@example.com")
                .experience(100)
                .joinedAt("2023-01-01")
                .isActive(1)
                .seedBalance(500)
                .build();

        List<UCUserListItem> mockUsers = Collections.singletonList(mockUser);

        when(userFetchService.fetchUsers(page, pageSize)).thenReturn(mockUsers);

        Map<String, Integer> driveCountMap = new HashMap<>();
        driveCountMap.put("user1", 0);
        when(dashboardFetchService.fetchDriveCountMap(Collections.singletonList("user1")))
                .thenReturn(driveCountMap);

        List<UserListItem> result = userAdminServiceImpl.adminGetUserList(page, pageSize);

        assertEquals(1, result.size());
        assertEquals(0, result.get(0).getDriveCount());

        verify(userFetchService, times(1)).fetchUsers(page, pageSize);
        verify(dashboardFetchService, times(1)).fetchDriveCountMap(Collections.singletonList("user1"));
    }

    /**
     * adminGetUserList 테스트 - 사용자 조회 실패
     * 시나리오: userFetchService 호출 중 예외 발생
     * 기대: 예외 전파 및 후속 호출 생략
     */
    @Test
    void testAdminGetUserList_userFetchServiceException() {
        int page = 1, pageSize = 10;

        when(userFetchService.fetchUsers(page, pageSize)).thenThrow(new RuntimeException("Service unavailable"));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userAdminServiceImpl.adminGetUserList(page, pageSize));
        assertEquals("Service unavailable", exception.getMessage());
        verify(userFetchService, times(1)).fetchUsers(page, pageSize);
        verify(dashboardFetchService, never()).fetchDriveCountMap(anyList());
    }

    /**
     * adminGetUserList 테스트 - dashboardFetchService 실패
     * 기대: 예외 발생 및 호출 순서 검증
     */
    @Test
    void testAdminGetUserList_dashboardFetchServiceException() {
        int page = 1, pageSize = 10;

        UCUserListItem mockUser = UCUserListItem.builder()
                .userId("user1")
                .nickname("User One")
                .email("user1@example.com")
                .experience(100)
                .joinedAt("2023-01-01")
                .isActive(1)
                .seedBalance(500)
                .build();

        List<UCUserListItem> mockUsers = Collections.singletonList(mockUser);
        when(userFetchService.fetchUsers(page, pageSize)).thenReturn(mockUsers);
        when(dashboardFetchService.fetchDriveCountMap(Collections.singletonList("user1")))
                .thenThrow(new RuntimeException("Dashboard service error"));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userAdminServiceImpl.adminGetUserList(page, pageSize));
        assertEquals("Dashboard service error", exception.getMessage());
        verify(userFetchService, times(1)).fetchUsers(page, pageSize);
        verify(dashboardFetchService, times(1)).fetchDriveCountMap(Collections.singletonList("user1"));
    }

    /**
     * adminGetUserList 테스트 - 정상적인 사용자 리스트 반환
     * 시나리오: 사용자 2명 정보가 정확히 매핑되어 반환됨
     * 기대: 각 사용자 정보가 정확하게 포함되어 있는지 검증
     */
    @Test
    void testAdminGetUserList() {
        int page = 1;
        int pageSize = 10;

        UCUserListItem mockUser1 = UCUserListItem.builder()
                .userId("user1")
                .nickname("User One")
                .email("user1@example.com")
                .experience(100)
                .joinedAt("2023-01-01")
                .isActive(1)
                .seedBalance(500)
                .build();

        UCUserListItem mockUser2 = UCUserListItem.builder()
                .userId("user2")
                .nickname("User Two")
                .email("user2@example.com")
                .experience(200)
                .joinedAt("2023-02-01")
                .isActive(0)
                .seedBalance(300)
                .build();

        List<UCUserListItem> mockUsers = Arrays.asList(mockUser1, mockUser2);
        when(userFetchService.fetchUsers(page, pageSize)).thenReturn(mockUsers);

        Map<String, Integer> mockDriveCountMap = new HashMap<>();
        mockDriveCountMap.put("user1", 5);
        mockDriveCountMap.put("user2", 3);
        when(dashboardFetchService.fetchDriveCountMap(Arrays.asList("user1", "user2"))).thenReturn(mockDriveCountMap);

        List<UserListItem> result = userAdminServiceImpl.adminGetUserList(page, pageSize);

        assertEquals(2, result.size());

        assertEquals("user1", result.get(0).getUserId());
        assertEquals("User One", result.get(0).getNickname());
        assertEquals("user1@example.com", result.get(0).getEmail());
        assertEquals(100, result.get(0).getExperience());
        assertEquals("2023-01-01", result.get(0).getJoinedAt());
        assertEquals(1, result.get(0).getIsActive());
        assertEquals(5, result.get(0).getDriveCount());
        assertEquals(500, result.get(0).getSeedBalance());

        assertEquals("user2", result.get(1).getUserId());
        assertEquals("User Two", result.get(1).getNickname());
        assertEquals("user2@example.com", result.get(1).getEmail());
        assertEquals(200, result.get(1).getExperience());
        assertEquals("2023-02-01", result.get(1).getJoinedAt());
        assertEquals(0, result.get(1).getIsActive());
        assertEquals(3, result.get(1).getDriveCount());
        assertEquals(300, result.get(1).getSeedBalance());

        verify(userFetchService, times(1)).fetchUsers(page, pageSize);
        verify(dashboardFetchService, times(1)).fetchDriveCountMap(Arrays.asList("user1", "user2"));
    }

    /**
     * adminGetUserList 테스트 - 사용자 목록이 null 반환
     * 시나리오: 외부 서비스에서 null 반환
     * 기대: NPE 발생 및 메시지 검증
     */
    @Test
    void testAdminGetUserList_userFetchServiceReturnsNull() {
        int page = 1;
        int pageSize = 10;

        when(userFetchService.fetchUsers(page, pageSize)).thenReturn(null);

        NullPointerException exception = assertThrows(NullPointerException.class,
                () -> userAdminServiceImpl.adminGetUserList(page, pageSize));

        assertTrue(exception.getMessage().contains("Cannot invoke"));
        verify(userFetchService, times(1)).fetchUsers(page, pageSize);
        verify(dashboardFetchService, never()).fetchDriveCountMap(anyList());
    }

    /**
     * adminGetUserList 테스트 - driveCountMap이 null 반환
     * 시나리오: 사용자 목록은 있으나 주행 수 정보 없음
     * 기대: NPE 발생 및 메시지 검증
     */
    @Test
    void testAdminGetUserList_dashboardFetchServiceReturnsNull() {
        int page = 1;
        int pageSize = 10;

        List<UCUserListItem> mockUsers = List.of(UCUserListItem.builder().userId("user1").build());
        when(userFetchService.fetchUsers(page, pageSize)).thenReturn(mockUsers);
        when(dashboardFetchService.fetchDriveCountMap(anyList())).thenReturn(null);

        NullPointerException exception = assertThrows(NullPointerException.class,
                () -> userAdminServiceImpl.adminGetUserList(page, pageSize));

        assertTrue(exception.getMessage().contains("Cannot invoke"));
        verify(userFetchService, times(1)).fetchUsers(page, pageSize);
        verify(dashboardFetchService, times(1)).fetchDriveCountMap(anyList());
    }

    /**
     * adminGetUserList 테스트 - 특정 사용자에 대한 driveCount 누락
     * 시나리오: driveCountMap에 일부 사용자 정보가 없음
     * 기대: NPE 발생 및 로그 또는 대응 검토 필요
     */
    @Test
    void testAdminGetUserList_logsWarningForMissingDriveCount() {
        int page = 1;
        int pageSize = 10;

        UCUserListItem mockUser = UCUserListItem.builder()
                .userId("user1")
                .nickname("User One")
                .build();

        when(userFetchService.fetchUsers(page, pageSize)).thenReturn(List.of(mockUser));
        when(dashboardFetchService.fetchDriveCountMap(List.of("user1")))
                .thenReturn(new HashMap<>());  // driveCount 없음

        NullPointerException ex = assertThrows(NullPointerException.class, () -> {
            userAdminServiceImpl.adminGetUserList(page, pageSize);
        });

        assertTrue(ex.getMessage().contains("intValue"));
        verify(userFetchService, times(1)).fetchUsers(page, pageSize);
        verify(dashboardFetchService, times(1)).fetchDriveCountMap(List.of("user1"));
    }

    /**
     * adminGetUserList 테스트 - 일부 사용자에 대한 driveCount 누락
     * 시나리오: dashboardFetchService가 일부 사용자에 대해 주행 수를 반환하지 않음
     * 기대: NPE 발생 및 누락된 데이터에 대한 처리 필요성 확인
     */
    @Test
    void testAdminGetUserListIncompleteDriveCount() {
        int page = 1;
        int pageSize = 10;

        UCUserListItem mockUser1 = UCUserListItem.builder()
                .userId("user1")
                .build();

        UCUserListItem mockUser2 = UCUserListItem.builder()
                .userId("user2")
                .build();

        List<UCUserListItem> mockUsers = Arrays.asList(mockUser1, mockUser2);
        when(userFetchService.fetchUsers(page, pageSize)).thenReturn(mockUsers);

        Map<String, Integer> mockDriveCountMap = new HashMap<>();
        mockDriveCountMap.put("user1", 5); // user2는 없음

        when(dashboardFetchService.fetchDriveCountMap(Arrays.asList("user1", "user2")))
                .thenReturn(mockDriveCountMap);

        RuntimeException ex = assertThrows(NullPointerException.class, () -> {
            userAdminServiceImpl.adminGetUserList(page, pageSize);
        });

        // 메시지까지 확인하고 싶다면
        assertTrue(ex.getMessage().contains("Cannot invoke \"java.lang.Integer.intValue()\""));
    }

    /**
     * adminGetUserDetail 테스트 - 정상 반환
     * 시나리오: 사용자 상세 정보 조회 성공
     * 기대: 필드 매핑 및 서비스 호출 정상 수행
     */
    @Test
    void testAdminGetUserDetail_successfullyFindUser() {
        String userId = "user1";

        UCUserListItem mockUser = UCUserListItem.builder()
                .userId("user1")
                .nickname("User One")
                .email("user1@example.com")
                .experience(200)
                .joinedAt("2023-01-01")
                .isActive(1)
                .seedBalance(400)
                .build();

        when(userFetchService.fetchUserDetail(userId)).thenReturn(Collections.singletonList(mockUser));
        when(dashboardFetchService.fetchDriveCountMap(Collections.singletonList("user1")))
                .thenReturn(Map.of("user1", 5));

        List<UserListItem> result = userAdminServiceImpl.adminGetUserDetail(userId);

        assertEquals(1, result.size());
        UserListItem user = result.get(0);
        assertEquals("user1", user.getUserId());
        assertEquals("User One", user.getNickname());
        assertEquals("user1@example.com", user.getEmail());
        assertEquals(200, user.getExperience());
        assertEquals("2023-01-01", user.getJoinedAt());
        assertEquals(1, user.getIsActive());
        assertEquals(400, user.getSeedBalance());
        assertEquals(5, user.getDriveCount());

        verify(userFetchService, times(1)).fetchUserDetail(userId);
        verify(dashboardFetchService, times(1)).fetchDriveCountMap(Collections.singletonList("user1"));
    }

    /**
     * adminGetUserDetail 테스트 - userFetchService 예외
     * 시나리오: 상세 정보 조회 실패
     * 기대: 예외 발생 및 후속 호출 생략
     */
    @Test
    void testAdminGetUserDetail_userFetchServiceException() {
        String userId = "user1";

        when(userFetchService.fetchUserDetail(userId)).thenThrow(new RuntimeException("User service error"));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userAdminServiceImpl.adminGetUserDetail(userId));

        assertEquals("User service error", exception.getMessage());
        verify(userFetchService, times(1)).fetchUserDetail(userId);
        verify(dashboardFetchService, never()).fetchDriveCountMap(anyList());
    }

    /**
     * adminGetUserDetail 테스트 - dashboardFetchService 예외
     * 시나리오: 사용자 정보는 있으나 주행 수 조회 실패
     * 기대: 예외 발생 및 서비스 호출 검증
     */
    @Test
    void testAdminGetUserDetail_dashboardFetchServiceException() {
        String userId = "user1";

        UCUserListItem mockUser = UCUserListItem.builder()
                .userId("user1")
                .nickname("User A")
                .email("usera@example.com")
                .experience(100)
                .joinedAt("2023-05-01")
                .isActive(1)
                .seedBalance(150)
                .build();

        when(userFetchService.fetchUserDetail(userId)).thenReturn(Collections.singletonList(mockUser));
        when(dashboardFetchService.fetchDriveCountMap(Collections.singletonList("user1")))
                .thenThrow(new RuntimeException("Dashboard service error"));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userAdminServiceImpl.adminGetUserDetail(userId));

        assertEquals("Dashboard service error", exception.getMessage());
        verify(userFetchService, times(1)).fetchUserDetail(userId);
        verify(dashboardFetchService, times(1)).fetchDriveCountMap(Collections.singletonList("user1"));
    }

    /**
     * adminGetUserReward 테스트 - 보상 내역 없음
     * 시나리오: 해당 사용자에 대한 리워드 없음
     * 기대: 빈 리스트 반환
     */
    @Test
    void testAdminGetUserReward_emptyRewards() {
        String userId = "user1";
        int page = 1;
        int pageSize = 10;

        when(rewardFetchService.fetchRewardFilter(eq(userId), any(RCRewardFilterReq.class)))
                .thenReturn(Collections.emptyList());

        List<UserRewardItem> result = userAdminServiceImpl.adminGetUserReward(userId, page, pageSize);

        assertEquals(0, result.size());
        verify(rewardFetchService, times(1)).fetchRewardFilter(eq(userId), any(RCRewardFilterReq.class));
    }

    /**
     * adminGetUserReward 테스트 - 보상 내역 존재
     * 시나리오: 사용자에게 리워드가 존재하는 경우
     * 기대: 필드 매핑 및 응답 검증
     */
    @Test
    void testAdminGetUserReward_withRewards() {
        String userId = "user1";
        int page = 1;
        int pageSize = 10;

        RCRewardFilterItem mockReward = RCRewardFilterItem.builder()
                .issuedDate("2025-06-01")
                .amount(100)
                .reason("Performance")
                .build();

        when(rewardFetchService.fetchRewardFilter(eq(userId), any(RCRewardFilterReq.class)))
                .thenReturn(Collections.singletonList(mockReward));

        List<UserRewardItem> result = userAdminServiceImpl.adminGetUserReward(userId, page, pageSize);

        assertEquals(1, result.size());
        UserRewardItem rewardItem = result.get(0);
        assertEquals("2025-06-01", rewardItem.getIssuedDate());
        assertEquals(100, rewardItem.getAmount());
        assertEquals("Performance", rewardItem.getReason());

        verify(rewardFetchService, times(1)).fetchRewardFilter(eq(userId), any(RCRewardFilterReq.class));
    }

    /**
     * adminGetUserDriveList 테스트 - 정상 주행 정보 반환
     * 시나리오: 주행 정보, 리워드, 이벤트 정상 수신
     * 기대: 올바른 정보 매핑 및 필드 검증
     */
    @Test
    void testAdminGetUserDriveList_successfulResult() {
        String userId = "user1";
        int pageSize = 10;
        String startTime = null;
        String driveId = null;

        // 모의 주행 기록 리스트 생성
        List<DCDriveListItem> mockDrives = List.of(
                DCDriveListItem.builder().driveId("drive1").date("2025-06-01").driveDuration(60).build(),
                DCDriveListItem.builder().driveId("drive2").date("2025-06-02").driveDuration(40).build()
        );

        // DriveHistoryWrapper는 builder 안 되므로 수동으로 세팅
        DCDriveListResData.DriveHistoryWrapper driveHistory = new DCDriveListResData.DriveHistoryWrapper();
        driveHistory.setList(mockDrives);
        driveHistory.setStartTime("2025-06-02");
        driveHistory.setDriveId("drive2");

        DCDriveListResData mockDashboardRes = DCDriveListResData.builder()
                .driveHistory(driveHistory)
                .build();

        // 리워드와 이벤트 더미 데이터
        Map<String, Integer> mockRewardMap = Map.of("drive1", 100, "drive2", 50);
        Map<String, List<EventsByDriveDTO>> mockEventMap = Map.of(
                "drive1", List.of(EventsByDriveDTO.builder().type("type1").count(10L).build()),
                "drive2", List.of(EventsByDriveDTO.builder().type("type2").count(5L).build())
        );

        when(dashboardFetchService.fetchDriveListByUserId(userId, pageSize, startTime, driveId))
                .thenReturn(mockDashboardRes);
        when(rewardFetchService.fetchRewardMapByDrive(eq(userId), any()))
                .thenReturn(mockRewardMap);
        when(analysisFetchService.getTotalEventCntByType(anyList()))
                .thenReturn(mockEventMap);

        // 테스트 대상 실행
        UserDriveListRes result = userAdminServiceImpl.adminGetUserDriveList(userId, pageSize, startTime, driveId);

        // 결과 검증
        assertNotNull(result);
        assertEquals(2, result.getDriveHistory().size());

        // 첫 번째 주행 내역
        var drive1 = result.getDriveHistory().get(0);
        assertEquals("2025-06-01", drive1.getDate());
        assertEquals(60, drive1.getDriveDuration());
        assertEquals(100, drive1.getRewards());
        assertEquals(10L, drive1.getEvents().get(0).getCount());

        // 두 번째 주행 내역
        var drive2 = result.getDriveHistory().get(1);
        assertEquals("2025-06-02", drive2.getDate());
        assertEquals(40, drive2.getDriveDuration());
        assertEquals(50, drive2.getRewards());
        assertEquals(5L, drive2.getEvents().get(0).getCount());

        // 호출 확인
        verify(dashboardFetchService, times(1)).fetchDriveListByUserId(userId, pageSize, startTime, driveId);
        verify(rewardFetchService, times(1)).fetchRewardMapByDrive(eq(userId), any());
        verify(analysisFetchService, times(1)).getTotalEventCntByType(anyList());
    }


    /**
     * adminGetUserDriveList 테스트 - dashboardFetchService 예외
     * 기대: 예외 발생 및 이후 호출 생략
     */
    @Test
    void testAdminGetUserDriveList_dashboardServiceException() {
        String userId = "user1";
        int pageSize = 10;
        String startTime = null;
        String driveId = null;

        when(dashboardFetchService.fetchDriveListByUserId(userId, pageSize, startTime, driveId))
                .thenThrow(new RuntimeException("Dashboard service error"));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userAdminServiceImpl.adminGetUserDriveList(userId, pageSize, startTime, driveId));

        assertEquals("Dashboard service error", exception.getMessage());
        verify(dashboardFetchService, times(1)).fetchDriveListByUserId(userId, pageSize, startTime, driveId);
        verify(rewardFetchService, never()).fetchRewardMapByDrive(anyString(), any());
        verify(analysisFetchService, never()).getTotalEventCntByType(anyList());
    }

    /**
     * adminGetUserDriveList 테스트 - rewardFetchService 예외
     * 기대: 예외 발생 및 이후 호출 생략
     */
    @Test
    void testAdminGetUserDriveList_rewardServiceException() {
        String userId = "user1";
        int pageSize = 10;
        String startTime = null;
        String driveId = null;

        DCDriveListItem driveItem = DCDriveListItem.builder().driveId("drive1").build();

        DCDriveListResData.DriveHistoryWrapper wrapper = new DCDriveListResData.DriveHistoryWrapper();
        wrapper.setList(List.of(driveItem));
        wrapper.setStartTime(null);
        wrapper.setDriveId(null);

        DCDriveListResData mockDashboardRes = DCDriveListResData.builder()
                .driveHistory(wrapper)
                .build();

        when(dashboardFetchService.fetchDriveListByUserId(userId, pageSize, startTime, driveId))
                .thenReturn(mockDashboardRes);

        when(rewardFetchService.fetchRewardMapByDrive(eq(userId), any()))
                .thenThrow(new RuntimeException("Reward service error"));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userAdminServiceImpl.adminGetUserDriveList(userId, pageSize, startTime, driveId));

        assertEquals("Reward service error", exception.getMessage());
        verify(dashboardFetchService, times(1)).fetchDriveListByUserId(userId, pageSize, startTime, driveId);
        verify(rewardFetchService, times(1)).fetchRewardMapByDrive(eq(userId), any());
        verify(analysisFetchService, never()).getTotalEventCntByType(anyList());
    }

    /**
     * adminGetUserDriveList 테스트 - analysisFetchService 예외
     * 기대: 예외 발생 및 호출 검증
     */
    @Test
    void testAdminGetUserDriveList_analysisServiceException() {
        String userId = "user1";
        int pageSize = 10;
        String startTime = null;
        String driveId = null;

        DCDriveListItem driveItem = DCDriveListItem.builder().driveId("drive1").build();

        DCDriveListResData.DriveHistoryWrapper wrapper = new DCDriveListResData.DriveHistoryWrapper();
        wrapper.setList(List.of(driveItem));
        wrapper.setStartTime(null);
        wrapper.setDriveId(null);

        DCDriveListResData mockDashboardRes = DCDriveListResData.builder()
                .driveHistory(wrapper)
                .build();

        Map<String, Integer> mockRewardMap = Map.of("drive1", 100);

        when(dashboardFetchService.fetchDriveListByUserId(userId, pageSize, startTime, driveId))
                .thenReturn(mockDashboardRes);

        when(rewardFetchService.fetchRewardMapByDrive(eq(userId), any()))
                .thenReturn(mockRewardMap);

        when(analysisFetchService.getTotalEventCntByType(anyList()))
                .thenThrow(new RuntimeException("Analysis service error"));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userAdminServiceImpl.adminGetUserDriveList(userId, pageSize, startTime, driveId));

        assertEquals("Analysis service error", exception.getMessage());
        verify(dashboardFetchService, times(1)).fetchDriveListByUserId(userId, pageSize, startTime, driveId);
        verify(rewardFetchService, times(1)).fetchRewardMapByDrive(eq(userId), any());
        verify(analysisFetchService, times(1)).getTotalEventCntByType(anyList());
    }
}