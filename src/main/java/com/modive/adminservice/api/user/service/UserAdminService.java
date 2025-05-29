package com.modive.adminservice.api.user.service;

import com.modive.adminservice.api.user.dto.req.UserFilterReq;
import com.modive.adminservice.api.user.dto.res.*;

import java.util.List;

public interface UserAdminService {
    List<UserListItem> adminGetUserList(int page, int pageSize);
    List<UserListItem> adminSearchUser(String searchKeyword);
    List<UserListItem>  adminGetUserDetail(Long userId);
    List<UserListItem> adminFilterUser(UserFilterReq req);
    void adminInactiveUser(Long userId);
    List<UserRewardItem> adminGetUserReward(Long userId, int page, int pageSize);
    UserDriveListRes adminGetUserDriveList(Long userId, int pageSize, String startTime, String driveId);
}
