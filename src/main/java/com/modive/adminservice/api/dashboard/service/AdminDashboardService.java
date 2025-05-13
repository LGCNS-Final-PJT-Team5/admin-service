package com.modive.adminservice.api.dashboard.service;

import com.modive.adminservice.api.dashboard.controller.AdminDashboardController;
import com.modive.adminservice.api.dashboard.dto.res.MonthlyDrivesItem;
import com.modive.adminservice.api.dashboard.dto.res.TotalCntAndRateItem;

import java.util.List;
import java.util.Map;

public interface AdminDashboardService {
    Map<String, TotalCntAndRateItem> getDashboardStatistics();
    List<MonthlyDrivesItem> getMonthlyDrivesStatistics();
}
