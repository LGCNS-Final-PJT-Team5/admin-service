package com.modive.adminservice.user.service;

import com.modive.adminservice.user.dto.res.UserListRes;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    List<UserListRes> adminGetUserList(int page, int pageSize);
}
