package com.modive.adminservice.user.service;

import com.modive.adminservice.user.dto.res.UserListItem;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    List<UserListItem> adminGetUserList(int page, int pageSize);
    List<UserListItem> adminSearchUser(String searchKeyword);
    UserListItem adminGetUserDetail(Long userId);
}
