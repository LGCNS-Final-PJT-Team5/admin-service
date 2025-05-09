package com.modive.adminservice.user.service.impl;

import com.modive.adminservice.external.client.dashboard.DashBoardClient;
import com.modive.adminservice.external.client.dashboard.dto.res.DCDriveCountItem;
import com.modive.adminservice.external.client.dashboard.dto.res.DCDriveCountResData;
//import com.modive.adminservice.external.client.reward.RewardClient;
import com.modive.adminservice.external.client.user.UserClient;
import com.modive.adminservice.external.client.user.dto.res.UCSearchUserResData;
import com.modive.adminservice.external.client.user.dto.res.UCUserDetailResData;
import com.modive.adminservice.external.client.user.dto.res.UCUserListItem;
import com.modive.adminservice.external.client.user.dto.res.UCUserListResData;
import com.modive.adminservice.global.dto.res.CommonRes;
import com.modive.adminservice.global.error.code.ErrorCode;
import com.modive.adminservice.global.error.exception.RestApiException;
import com.modive.adminservice.global.util.DateUtils;
import com.modive.adminservice.user.dto.res.UserListItem;
import com.modive.adminservice.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserClient userClient;
//    private final RewardClient rewardClient;
    private final DashBoardClient dashBoardClient;

    /**
     * 사용자 서비스에서 사용자 목록 조회
     *
     * @param page 페이지 번호
     * @param pageSize 한 페이지당 사용자 수
     * @return 사용자 목록
     */
    private List<UCUserListItem> fetchUsersFromUserService(int page, int pageSize) {
        CommonRes<UCUserListResData> userClientRes = userClient.getUserList(page, pageSize);
        if (userClientRes == null || userClientRes.data == null) {
            log.warn("UserClient.getUserList(page={}, pageSize={}) - response or data is null", page, pageSize);
            throw new RestApiException(ErrorCode.FEIGN_DATA_MISSING);
        }

        return userClientRes.getData().getUsers();
    }

    /**
     * 대시보드 서비스에서 사용자별 운전 횟수 맵 조회
     *
     * @param userIds 사용자 ID 리스트
     * @return 사용자 ID별 운전 횟수 맵
     */
    private Map<Long, Integer> fetchDriveCountMapFromDashboardService(List<Long> userIds) {
        Map<String, List<Long>> clientReqBody = new HashMap<>();
        clientReqBody.put("userIds", userIds);

        CommonRes<DCDriveCountResData> dashboardClientRes = dashBoardClient.getDriveCountByUser(clientReqBody);
        if (dashboardClientRes == null || dashboardClientRes.data == null) {
            log.warn("DashboardClient.getDriveCountByUser - response or data is null");
            throw new RestApiException(ErrorCode.FEIGN_DATA_MISSING);
        }

        Map<Long, Integer> driveCountMap = new HashMap<>();
        for (DCDriveCountItem driveCount : dashboardClientRes.data.getDriveCountByUser()) {
            driveCountMap.put(driveCount.getUserId(), driveCount.getDriveCount());
        }

        return driveCountMap;
    }

    /**
     * 사용자 정보와 운전 횟수를 병합하여 최종 사용자 리스트 생성
     *
     * @param users 사용자 정보 리스트
     * @param driveCountMap 사용자별 운전 횟수 맵
     * @return 병합된 사용자 리스트
     */
    private List<UserListItem> mergeUserData(List<UCUserListItem> users, Map<Long, Integer> driveCountMap) {
        List<UserListItem> userListRes = new ArrayList<>();
        for (UCUserListItem user : users) {
            Integer driveCount = driveCountMap.get(user.getUserId());

            if (driveCount == null) {
                log.warn("Drive count missing for userId={}", user.getUserId());
            }

            UserListItem res = UserListItem.builder()
                    .userId(user.getUserId())
                    .nickname(user.getNickname())
                    .email(user.getEmail())
                    .experience(user.getExperience())
                    .joinedAt(user.getJoinedAt())
                    .isActive(user.getIsActive())
                    .driveCount(driveCount)
                    .seedBalance(user.getSeedBalance())
                    .build();
            userListRes.add(res);
        }

        return userListRes;
    }

    /**
     * 사용자 서비스에서 사용자 검색
     *
     * @param searchKeyword 검색어
     * @return 검색 결과
     */
    private List<UCUserListItem> fetchSearchUsersFromUserService(String searchKeyword) {
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
    private UCUserDetailResData fetchUserDetailFromUserService(Long userId) {
        CommonRes<UCUserDetailResData> userClientRes = userClient.getUserDetail(userId);
        if (userClientRes == null || userClientRes.data == null) {
            log.warn("UserClient.getUserDetail(userId = {}) - response or data is null", userId);
            throw new RestApiException(ErrorCode.FEIGN_DATA_MISSING);
        }

        return userClientRes.data;
    }

    /**
     * 관리자용 사용자 목록 조회
     *
     * @param page 페이지 번호
     * @param pageSize 한 페이지당 사용자 수
     * @return 사용자 목록
     */
    @Override
    public List<UserListItem> adminGetUserList(int page, int pageSize) {
        List<UCUserListItem> users = fetchUsersFromUserService(page, pageSize);
        List<Long> userIds = users.stream()
                .map(UCUserListItem::getUserId)
                .collect(Collectors.toList());

        Map<Long, Integer> driveCountMap = fetchDriveCountMapFromDashboardService(userIds);
        return mergeUserData(users, driveCountMap);
    }

    @Override
    public List<UserListItem> adminSearchUser(String searchKeyword) {
        List<UCUserListItem> users = fetchSearchUsersFromUserService(searchKeyword);
        List<Long> userIds = users.stream()
                .map(UCUserListItem::getUserId)
                .collect(Collectors.toList());

        Map<Long, Integer> driveCountMap = fetchDriveCountMapFromDashboardService(userIds);
        return mergeUserData(users, driveCountMap);
    }

    @Override
    public UserListItem adminGetUserDetail(Long userId) {
        UCUserDetailResData user = fetchUserDetailFromUserService(userId);
        List<Long> userIds = new ArrayList<>();
        userIds.add(userId);

        Map<Long, Integer> driveCountMap = fetchDriveCountMapFromDashboardService(userIds);

        UserListItem userListItem = UserListItem.builder()
                .userId(userId)
                .nickname(user.getNickname())
                .email(user.getEmail())
                .experience(DateUtils.getYearsSince(
                        LocalDate.parse(user.getLicenseDate(), DateTimeFormatter.ISO_LOCAL_DATE_TIME).atStartOfDay())
                )
                .build();

        return userListItem;
    }
}
