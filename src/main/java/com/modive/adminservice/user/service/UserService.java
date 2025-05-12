package com.modive.adminservice.user.service;

import com.modive.adminservice.user.dto.req.UserFilterReq;
import com.modive.adminservice.user.dto.res.UserListItem;
import com.modive.adminservice.user.dto.res.UserRewardItem;

import java.util.List;

public interface UserService {
    List<UserListItem> adminGetUserList(int page, int pageSize);
    List<UserListItem> adminSearchUser(String searchKeyword);
    UserListItem adminGetUserDetail(Long userId);
    List<UserListItem> adminFilterUser(UserFilterReq req);
    void adminInactiveUser(Long userId);
    List<UserRewardItem> adminGetUserReward(Long userId, int page, int pageSize);
}
