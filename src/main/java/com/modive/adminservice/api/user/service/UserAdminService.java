package com.modive.adminservice.api.user.service;

import com.modive.adminservice.api.user.dto.req.UserFilterReq;
import com.modive.adminservice.api.user.dto.res.*;
import com.modive.adminservice.external.user.dto.res.UCFilterUserResData;

import java.util.List;

public interface UserAdminService {
    List<UserListItem> adminGetUserList(int page, int pageSize);
    List<UserListItem> adminSearchUser(String searchKeyword);
    List<UserListItem>  adminGetUserDetail(String userId);
    UCFilterUserResData adminFilterUser(UserFilterReq req);
    void adminInactiveUser(String userId);
    List<UserRewardItem> adminGetUserReward(String userId, int page, int pageSize);
    UserDriveListRes adminGetUserDriveList(String userId, int pageSize, String startTime, String driveId);
}
