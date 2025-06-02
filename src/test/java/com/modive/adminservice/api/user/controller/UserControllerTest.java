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

