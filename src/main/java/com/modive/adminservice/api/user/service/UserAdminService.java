package com.modive.adminservice.api.user.service;

import com.modive.adminservice.api.user.dto.req.UserFilterReq;
import com.modive.adminservice.api.user.dto.res.UserDriveListItem;
import com.modive.adminservice.api.user.dto.res.UserListItem;
import com.modive.adminservice.api.user.dto.res.UserRewardItem;

import java.util.List;

public interface UserAdminService {
    List<UserListItem> adminGetUserList(int page, int pageSize);
    List<UserListItem> adminSearchUser(String searchKeyword);
    UserListItem adminGetUserDetail(Long userId);
    List<UserListItem> adminFilterUser(UserFilterReq req);
    void adminInactiveUser(Long userId);
    List<UserRewardItem> adminGetUserReward(Long userId, int page, int pageSize);
    List<UserDriveListItem> adminGetUserDriveList(Long userId, int page, int pageSize);
}
