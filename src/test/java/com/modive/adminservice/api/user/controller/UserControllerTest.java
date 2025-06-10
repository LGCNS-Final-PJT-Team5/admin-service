package com.modive.adminservice.api.user.controller;

import com.modive.adminservice.api.user.dto.req.UserFilterReq;
import com.modive.adminservice.api.user.dto.res.UserDriveListRes;
import com.modive.adminservice.api.user.dto.res.UserListItem;
import com.modive.adminservice.api.user.dto.res.UserRewardItem;
import com.modive.adminservice.api.user.service.UserAdminService;
import com.modive.adminservice.external.user.dto.res.UCFilterUserResData;
import com.modive.adminservice.api.user.dto.res.UserDriveListItem;
import com.modive.adminservice.global.dto.res.CommonRes;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    @Mock
    private UserAdminService userAdminService;

    @InjectMocks
    private UserController userController;

    public UserControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    // =========================
    // USER FILTER TEST
    // ==========================
    /**
     * userFilter 테스트 - 성공
     * 시나리오: 유효한 필터 조건으로 사용자 필터링 성공
     * 기대: 정상 응답 및 필터링 결과 포함
     */
    @Test
    void userFilter_Success() {
        // Mock behavior for successful filtering
        UserFilterReq filterReq = new UserFilterReq(6, 24, 12, 1, 1, 10);
        UCFilterUserResData mockFilteredData = new UCFilterUserResData(); // Assuming mockFilteredData contains valid data

        when(userAdminService.adminFilterUser(filterReq)).thenReturn(mockFilteredData);

        ResponseEntity<CommonRes> response = userController.userFilter(filterReq);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("사용자 필터링에 성공하였습니다.", response.getBody().getMessage());
        assertEquals(mockFilteredData, ((Map<?, ?>) response.getBody().getData()).get("filterResult"));
    }

    /**
     * userFilter 테스트 - 유효하지 않은 파라미터
     * 시나리오: 필터 요청 값이 잘못된 경우
     * 기대: IllegalArgumentException 발생
     */
    @Test
    void userFilter_InvalidParameters() {
        // Mock behavior for invalid parameters
        UserFilterReq filterReq = new UserFilterReq(-1, 24, 12, 1, 1, 10);

        when(userAdminService.adminFilterUser(filterReq)).thenThrow(new IllegalArgumentException("Invalid request parameters"));

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            userController.userFilter(filterReq);
        });

        assertEquals("Invalid request parameters", thrown.getMessage());
    }

    /**
     * userFilter 테스트 - 내부 서버 오류
     * 시나리오: 서비스 내부 오류로 실패
     * 기대: RuntimeException 발생
     */
    @Test
    void userFilter_InternalServerError() {
        // Mock for internal server error
        UserFilterReq filterReq = new UserFilterReq(6, 24, 12, 1, 1, 10);

        when(userAdminService.adminFilterUser(filterReq)).thenThrow(new RuntimeException("Internal Server Error"));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            userController.userFilter(filterReq);
        });

        assertEquals("Internal Server Error", thrown.getMessage());
    }

    // =========================
    // USER DETAIL TEST
    // ==========================
    /**
     * getUser 테스트 - 성공
     * 시나리오: 사용자 ID로 상세 정보 조회 성공
     * 기대: 사용자 상세 정보 포함된 응답 반환
     */
    @Test
    void getUser_Success() {
        // Mock behavior for successful retrieval of user detail
        String userId = "123";
        List<UserListItem> mockUserDetail = List.of(new UserListItem()); // Assuming UserListItem is a valid item

        when(userAdminService.adminGetUserDetail(userId)).thenReturn(mockUserDetail);

        ResponseEntity<CommonRes> response = userController.getUser(userId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("사용자 상세 조회에 성공하였습니다.", response.getBody().getMessage());
        assertEquals(mockUserDetail, ((Map<?, ?>) response.getBody().getData()).get("userDetail"));
    }

    /**
     * getUser 테스트 - 사용자 없음
     * 시나리오: 존재하지 않는 사용자 ID로 조회 시도
     * 기대: RuntimeException 발생
     */
    @Test
    void getUser_UserNotFound() {
        // Mock behavior where user is not found
        String userId = "999";

        when(userAdminService.adminGetUserDetail(userId)).thenThrow(new RuntimeException("User Not Found"));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            userController.getUser(userId);
        });

        assertEquals("User Not Found", thrown.getMessage());
    }

    /**
     * getUser 테스트 - 내부 서버 오류
     * 시나리오: 상세 조회 중 예외 발생
     * 기대: RuntimeException 발생
     */
    @Test
    void getUser_InternalServerError() {
        // Mock for internal server error
        String userId = "123";

        when(userAdminService.adminGetUserDetail(userId)).thenThrow(new RuntimeException("Internal Server Error"));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            userController.getUser(userId);
        });

        assertEquals("Internal Server Error", thrown.getMessage());
    }

    // =========================
    // USER LIST TEST
    // ==========================
    /**
     * getUserList 테스트 - 성공
     * 시나리오: 페이지 요청으로 사용자 리스트 정상 반환
     * 기대: 사용자 목록 응답 포함
     */
    @Test
    void getUserList_Success() {
        // Mock behavior for successful retrieval
        List<UserListItem> mockUsers = List.of(new UserListItem()); // Assuming UserListItem is a valid item

        when(userAdminService.adminGetUserList(1, 10)).thenReturn(mockUsers);

        ResponseEntity<CommonRes> response = userController.getUserList(1, 10);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("사용자 목록 조회에 성공하였습니다.", response.getBody().getMessage());
        assertEquals(mockUsers, ((Map<?, ?>) response.getBody().getData()).get("users"));
    }

    /**
     * getUserList 테스트 - 잘못된 파라미터
     * 시나리오: 음수 페이지 번호 등 유효하지 않은 값
     * 기대: IllegalArgumentException 발생
     */
    @Test
    void getUserList_InvalidParameters() {
        // Mock behavior for exception on invalid parameters
        when(userAdminService.adminGetUserList(-1, 10))
                .thenThrow(new IllegalArgumentException("Invalid page number"));

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            userController.getUserList(-1, 10);
        });

        assertEquals("Invalid page number", thrown.getMessage());
    }

    // =========================
    // USER SEARCH TEST
    // ==========================

    /**
     * searchUser 테스트 - 성공
     * 시나리오: 검색어로 사용자 검색 성공
     * 기대: 일치하는 사용자 목록 반환
     */
    @Test
    void searchUser_Success() {
        // Mock behavior for successful search
        String searchKeyword = "user@modive.com";
        List<UserListItem> mockUsers = List.of(new UserListItem()); // Assuming UserListItem is a valid item

        when(userAdminService.adminSearchUser(searchKeyword)).thenReturn(mockUsers);

        ResponseEntity<CommonRes> response = userController.searchUser(searchKeyword);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("사용자 검색에 성공하였습니다.", response.getBody().getMessage());
        assertEquals(mockUsers, ((Map<?, ?>) response.getBody().getData()).get("searchResult"));
    }

    /**
     * searchUser 테스트 - 내부 서버 오류
     * 시나리오: 검색 중 예외 발생
     * 기대: RuntimeException 발생
     */
    @Test
    void searchUser_InternalServerError() {
        // Mock behavior for exception
        String searchKeyword = "user@modive.com";

        when(userAdminService.adminSearchUser(searchKeyword)).thenThrow(new RuntimeException("Internal Server Error"));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            userController.searchUser(searchKeyword);
        });

        assertEquals("Internal Server Error", thrown.getMessage());
    }
    // =========================
    // USER REWARD TEST
    // ==========================

    /**
     * getUserRewards 테스트 - 성공
     * 시나리오: 사용자 보상 이력 정상 반환
     * 기대: 응답에 리워드 리스트 포함
     */
    @Test
    void getUserRewards_Success() {
        // Mock behavior for successful reward history retrieval
        String userId = "123";
        int page = 1;
        int pageSize = 10;
        List<UserRewardItem> mockRewards = List.of(new UserRewardItem()); // Assuming UserRewardItem is a valid object

        when(userAdminService.adminGetUserReward(userId, page, pageSize)).thenReturn(mockRewards);

        ResponseEntity<CommonRes> response = userController.getUserRewards(userId, page, pageSize);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("사용자 씨앗 내역 조회에 성공하였습니다.", response.getBody().getMessage());
        assertEquals(mockRewards, ((Map<?, ?>) response.getBody().getData()).get("rewardHistory"));
    }

    /**
     * getUserRewards 테스트 - 사용자 없음
     * 시나리오: 존재하지 않는 사용자 ID로 보상 요청
     * 기대: RuntimeException 발생
     */
    @Test
    void getUserRewards_UserNotFound() {
        // Mock behavior where the specified user does not exist
        String userId = "999";
        int page = 1;
        int pageSize = 10;

        when(userAdminService.adminGetUserReward(userId, page, pageSize)).thenThrow(new RuntimeException("User Not Found"));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            userController.getUserRewards(userId, page, pageSize);
        });

        assertEquals("User Not Found", thrown.getMessage());
    }

    /**
     * getUserRewards 테스트 - 내부 서버 오류
     * 시나리오: 서비스 예외 발생
     * 기대: RuntimeException 발생
     */
    @Test
    void getUserRewards_InternalServerError() {
        // Mock behavior for internal server error scenario
        String userId = "123";
        int page = 1;
        int pageSize = 10;

        when(userAdminService.adminGetUserReward(userId, page, pageSize)).thenThrow(new RuntimeException("Internal Server Error"));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            userController.getUserRewards(userId, page, pageSize);
        });

        assertEquals("Internal Server Error", thrown.getMessage());
    }

    // =========================
    // USER DRIVE TEST
    // ==========================
    /**
     * getUserDrives 테스트 - 성공
     * 시나리오: 운전 이력 조회 정상 수행
     * 기대: 주행 내역, startTime, driveId 응답 포함
     */
    @Test
    void getUserDrives_Success() {
        // Mock successful behavior
        String userId = "123";
        Integer pageSize = 10;
        String startTime = "2024-05-20T12:34:56Z";
        String driveId = "abc123";
        UserDriveListRes mockResponse = UserDriveListRes.builder()
                .driveHistory(List.of(new UserDriveListItem()))
                .startTime(startTime)
                .driveId(driveId)
                .build();

        when(userAdminService.adminGetUserDriveList(userId, pageSize, startTime, driveId)).thenReturn(mockResponse);

        ResponseEntity<CommonRes> response = userController.getUserDrives(userId, pageSize, startTime, driveId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("사용자 운전 내역 조회에 성공하였습니다.", response.getBody().getMessage());
        Map<?, ?> data = (Map<?, ?>) response.getBody().getData();
        assertEquals(mockResponse.getDriveHistory(), data.get("driveHistory"));
        assertEquals(mockResponse.getStartTime(), data.get("startTime"));
        assertEquals(mockResponse.getDriveId(), data.get("driveId"));
    }

    /**
     * getUserDrives 테스트 - 사용자 없음
     * 시나리오: 존재하지 않는 사용자로 요청 시도
     * 기대: RuntimeException 발생
     */
    @Test
    void getUserDrives_UserNotFound() {
        // Mock scenario where user is not found
        String userId = "999";
        Integer pageSize = 10;
        String startTime = "2024-05-20T12:34:56Z";
        String driveId = "abc123";

        when(userAdminService.adminGetUserDriveList(userId, pageSize, startTime, driveId))
                .thenThrow(new RuntimeException("User Not Found"));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            userController.getUserDrives(userId, pageSize, startTime, driveId);
        });

        assertEquals("User Not Found", thrown.getMessage());
    }


    /**
     * getUserDrives 테스트 - 내부 서버 오류
     * 시나리오: 주행 정보 조회 중 오류 발생
     * 기대: RuntimeException 발생
     */
    @Test
    void getUserDrives_InternalServerError() {
        // Mock internal server error scenario
        String userId = "123";
        Integer pageSize = 10;
        String startTime = "2024-05-20T12:34:56Z";
        String driveId = "abc123";

        when(userAdminService.adminGetUserDriveList(userId, pageSize, startTime, driveId))
                .thenThrow(new RuntimeException("Internal Server Error"));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            userController.getUserDrives(userId, pageSize, startTime, driveId);
        });

        assertEquals("Internal Server Error", thrown.getMessage());
    }
}

