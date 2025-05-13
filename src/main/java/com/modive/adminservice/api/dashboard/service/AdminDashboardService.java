package com.modive.adminservice.api.dashboard.service;

import com.modive.adminservice.api.dashboard.dto.res.TotalCntAndRateItem;

import java.util.Map;

public interface AdminDashboardService {
    Map<String, TotalCntAndRateItem> getDashboardStatistics();
}
