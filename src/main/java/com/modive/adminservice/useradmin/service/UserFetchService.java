package com.modive.adminservice.useradmin.service;

import com.modive.adminservice.external.client.user.dto.res.UCUserDetailResData;
import com.modive.adminservice.external.client.user.dto.res.UCUserListItem;
import com.modive.adminservice.useradmin.dto.req.UserFilterReq;

import java.util.List;

public interface UserFetchService {
    List<UCUserListItem> fetchUsers(int page, int pageSize);
    List<UCUserListItem> fetchSearchUsers(String searchKeyword);
    UCUserDetailResData fetchUserDetail(Long userId);
    List<UCUserListItem> fetchFilteredUser(UserFilterReq params);
    void inactiveUser(Long userId);
}
