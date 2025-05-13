package com.modive.adminservice.useradmin.service;

import com.modive.adminservice.useradmin.dto.req.UserFilterReq;
import com.modive.adminservice.useradmin.dto.res.UserDriveListItem;
import com.modive.adminservice.useradmin.dto.res.UserListItem;
import com.modive.adminservice.useradmin.dto.res.UserRewardItem;

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
