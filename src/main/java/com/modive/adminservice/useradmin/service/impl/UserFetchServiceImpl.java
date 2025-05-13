package com.modive.adminservice.useradmin.service.impl;

import com.modive.adminservice.external.client.user.UserClient;
import com.modive.adminservice.external.client.user.dto.res.UCSearchUserResData;
import com.modive.adminservice.external.client.user.dto.res.UCUserDetailResData;
import com.modive.adminservice.external.client.user.dto.res.UCUserListItem;
import com.modive.adminservice.external.client.user.dto.res.UCUserListResData;
import com.modive.adminservice.global.dto.res.CommonRes;
import com.modive.adminservice.global.error.code.ErrorCode;
import com.modive.adminservice.global.error.exception.RestApiException;
import com.modive.adminservice.useradmin.dto.req.UserFilterReq;
import com.modive.adminservice.useradmin.service.UserFetchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserFetchServiceImpl implements UserFetchService {
    private final UserClient userClient;

    /**
     * 사용자 서비스에서 사용자 목록 조회
     *
     * @param page 페이지 번호
     * @param pageSize 한 페이지당 사용자 수
     * @return 사용자 목록
     */
    @Override
    public List<UCUserListItem> fetchUsers(int page, int pageSize) {
        CommonRes<UCUserListResData> userClientRes = userClient.getUserList(page, pageSize);
        if (userClientRes == null || userClientRes.data == null) {
            log.warn("UserClient.getUserList(page={}, pageSize={}) - response or data is null", page, pageSize);
            throw new RestApiException(ErrorCode.FEIGN_DATA_MISSING);
        }

        return userClientRes.getData().getUsers();
    }

    /**
     * 사용자 서비스에서 사용자 검색
     *
     * @param searchKeyword 검색어
     * @return 검색 결과
     */
    @Override
    public List<UCUserListItem> fetchSearchUsers(String searchKeyword) {
        CommonRes<UCSearchUserResData> userClientRes = userClient.searchUser(searchKeyword);
        if (userClientRes == null || userClientRes.data == null) {
            log.warn("UserClient.getUserList(searchKeyword = {}) - response or data is null", searchKeyword);
            throw new RestApiException(ErrorCode.FEIGN_DATA_MISSING);
        }

        return userClientRes.getData().getSearchResult();
    }

    /**
     * 사용자 서비스에서 사용자 상세 데이터 조회
     *
     * @param userId 유저ID
     * @return 사용자 상세 데이터
     */
    @Override
    public UCUserDetailResData fetchUserDetail(Long userId) {
        CommonRes<UCUserDetailResData> userClientRes = userClient.getUserDetail(userId);
        if (userClientRes == null || userClientRes.data == null) {
            log.warn("UserClient.getUserDetail(userId = {}) - response or data is null", userId);
            throw new RestApiException(ErrorCode.FEIGN_DATA_MISSING);
        }

        return userClientRes.data;
    }

    /**
     * 사용자 서비스에서 필터링한 데이터 조회
     *
     * @param params 최소 운전 경력, 최대 운전 경력 등 필터링 데이터와 페이지네이션에 필요한 데이터
     * @return 필터링 결과
     */
    @Override
    public List<UCUserListItem> fetchFilteredUser(UserFilterReq params) {
        CommonRes<UCUserListResData> userClientRes = userClient.getFilteredUser(params);
        if (userClientRes == null || userClientRes.data == null) {
            log.warn("UserClient.getFilteredUser(params) - response or data is null", params);
            throw new RestApiException(ErrorCode.FEIGN_DATA_MISSING);
        }

        return userClientRes.getData().getUsers();
    }

    /**
     * 사용자 서비스에서 사용자 비활성화 처리
     *
     * @param userId 유저ID
     * @return 비활성화 처리 성공 유무
     */
    @Override
    public void inactiveUser(Long userId) {
        CommonRes userClientRes = userClient.getUserDetail(userId);
        if (userClientRes == null || userClientRes.data == null) {
            log.warn("UserClient.getUserDetail(userId = {}) - response or data is null", userId);
            throw new RestApiException(ErrorCode.FEIGN_DATA_MISSING);
        }
    }
}
