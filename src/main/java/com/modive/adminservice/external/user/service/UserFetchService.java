package com.modive.adminservice.external.user.service;

import com.modive.adminservice.external.user.dto.res.UCFilterUserResData;
import com.modive.adminservice.external.user.dto.res.UCTotalCntAndRateItem;
import com.modive.adminservice.external.user.dto.res.UCUserListItem;
import com.modive.adminservice.api.user.dto.req.UserFilterReq;

import java.util.List;
import java.util.Map;

public interface UserFetchService {
    List<UCUserListItem> fetchUsers(int page, int pageSize);
    List<UCUserListItem> fetchSearchUsers(String searchKeyword);
    List<UCUserListItem> fetchUserDetail(String userId);
    UCFilterUserResData fetchFilteredUser(UserFilterReq params);
    void inactiveUser(String userId);
    UCTotalCntAndRateItem fetchUserTotalCountAndChangeRate();
    UCTotalCntAndRateItem fetchDevicesTotalCountAndChangeRate();
    Map<String, Object> fetchUserStatistics();
}
