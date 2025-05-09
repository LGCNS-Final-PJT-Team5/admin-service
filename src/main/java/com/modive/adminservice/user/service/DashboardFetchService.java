package com.modive.adminservice.user.service;

import java.util.List;
import java.util.Map;

public interface DashboardFetchService {
    Map<Long, Integer> fetchDriveCountMap(List<Long> userIds);
}
