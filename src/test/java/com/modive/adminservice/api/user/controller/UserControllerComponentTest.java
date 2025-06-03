package com.modive.adminservice.api.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.modive.adminservice.api.user.dto.req.UserFilterReq;
import com.modive.adminservice.api.user.dto.res.UserDriveListItem;
import com.modive.adminservice.api.user.dto.res.UserDriveListRes;
import com.modive.adminservice.api.user.dto.res.UserListItem;
import com.modive.adminservice.api.user.dto.res.UserRewardItem;
import com.modive.adminservice.api.user.service.UserAdminService;
import com.modive.adminservice.external.user.dto.res.UCFilterUserResData;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(UserController.class)
@TestPropertySource(properties = {
        "spring.cloud.config.enabled=false",
        "spring.cloud.config.discovery.enabled=false",
        "spring.cloud.config.fail-fast=false",
        "spring.cloud.discovery.enabled=false",
        "eureka.client.enabled=false"
})
class UserControllerComponentTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserAdminService userAdminService;

    @Autowired
    private ObjectMapper objectMapper;

    private ResultActions performGet(String url, Map<String, String> params) throws Exception {
        var request = get(url);
        if (params != null) {
            params.forEach(request::param);
        }
        return mockMvc.perform(request);
    }

    @Test
    void getUserList_Success() throws Exception {
        List<UserListItem> mockUsers = List.of(
                UserListItem.builder()
                        .userId("user1")
                        .nickname("테스트 유저")
                        .email("test@example.com")
                        .build()
        );
        when(userAdminService.adminGetUserList(1, 10)).thenReturn(mockUsers);

        performGet("/admin/users", Map.of("page", "1", "pageSize", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("사용자 목록 조회에 성공하였습니다."))
                .andExpect(jsonPath("$.data.users[0].userId").value("user1"))
                .andDo(print());
    }

    @Test
    void searchUser_Success() throws Exception {
        // given
        String searchKeyword = "test@example.com";
        List<UserListItem> mockUsers = List.of(
                UserListItem.builder()
                        .userId("user1")
                        .email(searchKeyword)
                        .build()
        );
        when(userAdminService.adminSearchUser(searchKeyword)).thenReturn(mockUsers);

        // when & then
        performGet("/admin/users/search", Map.of("searchKeyword", searchKeyword))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.searchResult[0].email").value(searchKeyword));
    }


@Test
    void getUser_Success() throws Exception {
        String userId = "user1";
        List<UserListItem> mockUser = List.of(
                UserListItem.builder()
                        .userId(userId)
                        .nickname("테스트 유저")
                        .build()
        );
        when(userAdminService.adminGetUserDetail(userId)).thenReturn(mockUser);

        mockMvc.perform(get("/admin/users/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.userDetail[0].userId").value(userId));
    }

    @Test
    void userFilter_Success() throws Exception {
        UserFilterReq filterReq = new UserFilterReq(6, 24, 12, 1, 1, 10);
        UCFilterUserResData mockFilteredData = new UCFilterUserResData();
        when(userAdminService.adminFilterUser(any(UserFilterReq.class))).thenReturn(mockFilteredData);

        performGet("/admin/users/filter", Map.of(
                "minExperience", "6",
                "maxExperience", "24",
                "accountAgeInMonths", "12",
                "active", "1",
                "page", "1",
                "pageSize", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.filterResult").exists());
    }

    @Test
    void deleteUser_Success() throws Exception {
        String userId = "user1";
        doNothing().when(userAdminService).adminInactiveUser(userId);

        mockMvc.perform(post("/admin/users/{userId}/delete", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("사용자 비활성화 처리가 완료되었습니다."));
    }

    @Test
    void getUserRewards_Success() throws Exception {
        String userId = "user1";
        List<UserRewardItem> mockRewards = List.of(
                UserRewardItem.builder()
                        .amount(100)
                        .reason("테스트")
                        .build()
        );
        when(userAdminService.adminGetUserReward(userId, 1, 10)).thenReturn(mockRewards);

        performGet("/admin/users/{userId}/rewards".replace("{userId}", userId),
                Map.of("page", "1", "pageSize", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.rewardHistory[0].amount").value(100));
    }

    @Test
    void getUserDrives_Success() throws Exception {
        String userId = "user1";
        UserDriveListItem driveItem = UserDriveListItem.builder()
                .date("2024-05-20T12:34:56Z")
                .build();

        UserDriveListRes mockDrives = UserDriveListRes.builder()
                .driveId("drive1")
                .startTime("2024-05-20T12:34:56Z")
                .driveHistory(List.of(driveItem))
                .build();

        when(userAdminService.adminGetUserDriveList(userId, 10, null, null)).thenReturn(mockDrives);

        performGet("/admin/users/drives/{userId}".replace("{userId}", userId),
                Map.of("pageSize", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.driveId").value("drive1"))
                .andExpect(jsonPath("$.data.driveHistory[0].date").value("2024-05-20T12:34:56Z"));
    }

    @Test
    void getUserList_InvalidParameter() throws Exception {
        // Given
        when(userAdminService.adminGetUserList(-1, 10))
                .thenThrow(new IllegalArgumentException("잘못된 페이지 번호입니다."));

        // When & Then
        try {
            performGet("/admin/users", Map.of("page", "-1", "pageSize", "10"));
            fail("예외가 발생해야 합니다");
        } catch (ServletException e) {
            assertTrue(e.getCause() instanceof IllegalArgumentException);
            assertEquals("잘못된 페이지 번호입니다.", e.getCause().getMessage());
        }
    }

    @Test
    void searchUser_EmptyKeyword() throws Exception {
        performGet("/admin/users/search", Map.of("searchKeyword", ""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.searchResult").isArray())
                .andExpect(jsonPath("$.data.searchResult").isEmpty());
    }
}
