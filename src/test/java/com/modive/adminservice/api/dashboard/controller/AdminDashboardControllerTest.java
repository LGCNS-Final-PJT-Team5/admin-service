package com.modive.adminservice.api.dashboard.controller;
import com.modive.adminservice.api.dashboard.dto.res.MonthlyDrivesItem;
import com.modive.adminservice.api.dashboard.service.AdminDashboardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminDashboardControllerTest {

    @Mock
    private AdminDashboardService adminDashboardService;

    @InjectMocks
    private AdminDashboardController adminDashboardController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(adminDashboardController).build();
    }


    @Test
    void getMonthlyDrivesStatistics_ShouldReturnMonthlyDrivesStatistics() throws Exception {
        List<MonthlyDrivesItem> mockMonthlyDrivesStatistics = Arrays.asList(
                MonthlyDrivesItem.builder()
                        .year(2025)
                        .month(1)
                        .count(34L)
                        .build(),
                MonthlyDrivesItem.builder()
                        .year(2025)
                        .month(2)
                        .count(28L)
                        .build()
        );

        when(adminDashboardService.getMonthlyDrivesStatistics()).thenReturn(mockMonthlyDrivesStatistics);

        mockMvc.perform(get("/admin/dashboard/drives/monthly-stats")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("월별 운전 횟수 조회에 성공했습니다."))
                .andExpect(jsonPath("$.data.monthlyDrivesStatistics[0].year").value(2025))
                .andExpect(jsonPath("$.data.monthlyDrivesStatistics[0].month").value(1))
                .andExpect(jsonPath("$.data.monthlyDrivesStatistics[0].count").value(34))
                .andExpect(jsonPath("$.data.monthlyDrivesStatistics[1].year").value(2025))
                .andExpect(jsonPath("$.data.monthlyDrivesStatistics[1].month").value(2))
                .andExpect(jsonPath("$.data.monthlyDrivesStatistics[1].count").value(28));
    }

@Test
    void getMonthlyUsersStatistics_ShouldReturnMonthlyUsersStatistics() throws Exception {
        Map<String, Object> mockUserStatistics = Map.of(
                "2025-01", 120,
                "2025-02", 150
        );

        when(adminDashboardService.getMonthlyUserStatistics()).thenReturn(mockUserStatistics);

        mockMvc.perform(get("/admin/dashboard/users/monthly-stats")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("사용자 증감 추이 조회에 성공하였습니다."))
                .andExpect(jsonPath("$.data.userStatistics['2025-01']").value(120))
                .andExpect(jsonPath("$.data.userStatistics['2025-02']").value(150));
    }
}