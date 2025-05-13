package com.modive.adminservice.external.user.service;

import com.modive.adminservice.external.user.dto.res.UCTotalCntAndRateItem;
import com.modive.adminservice.external.user.dto.res.UCUserDetailResData;
import com.modive.adminservice.external.user.dto.res.UCUserListItem;
import com.modive.adminservice.api.user.dto.req.UserFilterReq;

import java.util.List;

public interface UserFetchService {
    List<UCUserListItem> fetchUsers(int page, int pageSize);
    List<UCUserListItem> fetchSearchUsers(String searchKeyword);
    UCUserDetailResData fetchUserDetail(Long userId);
    List<UCUserListItem> fetchFilteredUser(UserFilterReq params);
    void inactiveUser(Long userId);
    UCTotalCntAndRateItem fetchUserTotalCountAndChangeRate();
    UCTotalCntAndRateItem fetchDevicesTotalCountAndChangeRate();
}
