package com.modive.adminservice.user.service;

import com.modive.adminservice.user.dto.req.UserFilterReq;
import com.modive.adminservice.user.dto.res.UserListItem;

import java.util.List;

public interface UserService {
    List<UserListItem> adminGetUserList(int page, int pageSize);
    List<UserListItem> adminSearchUser(String searchKeyword);
    UserListItem adminGetUserDetail(Long userId);
    List<UserListItem> adminFilterUser(UserFilterReq req);
}
