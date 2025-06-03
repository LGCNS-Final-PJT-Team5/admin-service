package com.modive.adminservice.api.dashboard.controller;

import com.modive.adminservice.api.dashboard.dto.res.MonthlyDrivesItem;
import com.modive.adminservice.api.dashboard.service.AdminDashboardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import jakarta.servlet.ServletException;
import java.util.List;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(properties = {
        "eureka.client.enabled=false",
        "eureka.client.register-with-eureka=false",
        "eureka.client.fetch-registry=false"
})
public class AdminDashboardComponentTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminDashboardService adminDashboardService;


    private ResultActions performGet(String url) throws Exception {
        return mockMvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void getMonthlyUsersStatistics_Success() throws Exception {
        // Given
        Map<String, Object> mockUserStats = new HashMap<>();
        mockUserStats.put("totalUsers", 1000);
        mockUserStats.put("monthlyGrowth", Arrays.asList(
                Map.of("month", "2025-01", "count", 100),
                Map.of("month", "2025-02", "count", 150),
                Map.of("month", "2025-03", "count", 200)
        ));

        when(adminDashboardService.getMonthlyUserStatistics())
                .thenReturn(mockUserStats);

        // When & Then
        performGet("/admin/dashboard/users/monthly-stats")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("사용자 증감 추이 조회에 성공하였습니다."))
                .andExpect(jsonPath("$.data.userStatistics.totalUsers").value(1000))
                .andExpect(jsonPath("$.data.userStatistics.monthlyGrowth").isArray())
                .andExpect(jsonPath("$.data.userStatistics.monthlyGrowth[0].month").value("2025-01"))
                .andExpect(jsonPath("$.data.userStatistics.monthlyGrowth[0].count").value(100));
    }

    @Test
    void getMonthlyDrivesStatistics_Success() throws Exception {
        // Given
        List<MonthlyDrivesItem> mockStats = List.of(
                new MonthlyDrivesItem(2025, 5, 150L),
                new MonthlyDrivesItem(2025, 6, 200L)
        );

        when(adminDashboardService.getMonthlyDrivesStatistics())
                .thenReturn(mockStats);

        // When & Then
        mockMvc.perform(get("/admin/dashboard/drives/monthly-stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("월별 운전 횟수 조회에 성공했습니다."))
                .andExpect(jsonPath("$.data.monthlyDrivesStatistics[0].year").value(2025))
                .andExpect(jsonPath("$.data.monthlyDrivesStatistics[0].month").value(5))
                .andExpect(jsonPath("$.data.monthlyDrivesStatistics[0].count").value(150))
                .andExpect(jsonPath("$.data.monthlyDrivesStatistics[1].year").value(2025))
                .andExpect(jsonPath("$.data.monthlyDrivesStatistics[1].month").value(6))
                .andExpect(jsonPath("$.data.monthlyDrivesStatistics[1].count").value(200));
    }

    @Test
    void getMonthlyDrivesStatistics_InternalServerError() throws Exception {
        // Given
        doThrow(new RuntimeException("내부 서버 오류"))
                .when(adminDashboardService).getMonthlyDrivesStatistics();

        // When & Then
        Exception exception = assertThrows(
                ServletException.class,
                () -> mockMvc.perform(get("/admin/dashboard/drives/monthly-stats"))
        );

        assertTrue(exception.getCause() instanceof RuntimeException);
        assertEquals("내부 서버 오류", exception.getCause().getMessage());
    }

}