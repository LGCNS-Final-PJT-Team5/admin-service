package com.modive.adminservice.user.service.impl;

import com.modive.adminservice.external.client.dashboard.DashBoardClient;
import com.modive.adminservice.external.client.dashboard.dto.res.DCDriveCountItem;
import com.modive.adminservice.external.client.dashboard.dto.res.DCDriveCountResData;
import com.modive.adminservice.external.client.reward.RewardClient;
import com.modive.adminservice.external.client.user.UserClient;
import com.modive.adminservice.external.client.user.dto.res.UCUserListItem;
import com.modive.adminservice.external.client.user.dto.res.UCUserListResData;
import com.modive.adminservice.global.dto.res.CommonRes;
import com.modive.adminservice.global.error.code.ErrorCode;
import com.modive.adminservice.global.error.exception.RestApiException;
import com.modive.adminservice.user.dto.res.UserListRes;
import com.modive.adminservice.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserClient userClient;
    private final RewardClient rewardClient;
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
    private List<UserListRes> mergeUserData(List<UCUserListItem> users, Map<Long, Integer> driveCountMap) {
        List<UserListRes> userListRes = new ArrayList<>();
        for (UCUserListItem user : users) {
            Integer driveCount = driveCountMap.get(user.getUserId());

            if (driveCount == null) {
                log.warn("Drive count missing for userId={}", user.getUserId());
            }

            UserListRes res = UserListRes.builder()
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
     * 관리자용 사용자 목록 조회
     *
     * @param page 페이지 번호
     * @param pageSize 한 페이지당 사용자 수
     * @return 사용자 목록
     */
    @Override
    public List<UserListRes> adminGetUserList(int page, int pageSize) {
        List<UCUserListItem> users = fetchUsersFromUserService(page, pageSize);
        List<Long> userIds = users.stream()
                .map(UCUserListItem::getUserId)
                .collect(Collectors.toList());

        Map<Long, Integer> driveCountMap = fetchDriveCountMapFromDashboardService(userIds);
        return mergeUserData(users, driveCountMap);
    }
}
