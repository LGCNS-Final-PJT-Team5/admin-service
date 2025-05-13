package com.modive.adminservice.useradmin.service.impl;

//import com.modive.adminservice.external.client.reward.RewardClient;
import com.modive.adminservice.event.service.EventService;
import com.modive.adminservice.external.client.dashboard.dto.res.DCDriveListItem;
import com.modive.adminservice.external.client.reward.RewardClient;
import com.modive.adminservice.external.client.reward.dto.req.RewardByDriveReq;
import com.modive.adminservice.external.client.reward.dto.req.RewardFilterReq;
import com.modive.adminservice.external.client.reward.dto.res.RewardFilterItem;
import com.modive.adminservice.external.client.user.dto.res.UCUserDetailResData;
import com.modive.adminservice.external.client.user.dto.res.UCUserListItem;
import com.modive.adminservice.global.util.DateUtils;
import com.modive.adminservice.useradmin.dto.req.UserFilterReq;
import com.modive.adminservice.useradmin.dto.res.UserDriveListEventItem;
import com.modive.adminservice.useradmin.dto.res.UserDriveListItem;
import com.modive.adminservice.useradmin.dto.res.UserListItem;
import com.modive.adminservice.useradmin.dto.res.UserRewardItem;
import com.modive.adminservice.useradmin.service.DashboardFetchService;
import com.modive.adminservice.useradmin.service.RewardFetchService;
import com.modive.adminservice.useradmin.service.UserFetchService;
import com.modive.adminservice.useradmin.service.UserAdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserAdminServiceImpl implements UserAdminService {
    private final UserFetchService userFetchService;
    private final DashboardFetchService dashboardFetchService;
    private final RewardFetchService rewardFetchService;
    private final EventService eventService;
    private final RewardClient rewardClient;

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
     * 관리자용 사용자 목록 조회
     *
     * @param page 페이지 번호
     * @param pageSize 한 페이지당 사용자 수
     * @return 사용자 목록
     */
    @Override
    public List<UserListItem> adminGetUserList(int page, int pageSize) {
        List<UCUserListItem> users = userFetchService.fetchUsers(page, pageSize);
        List<Long> userIds = users.stream()
                .map(UCUserListItem::getUserId)
                .collect(Collectors.toList());

        Map<Long, Integer> driveCountMap = dashboardFetchService.fetchDriveCountMap(userIds);
        return mergeUserData(users, driveCountMap);
    }

    /**
     * 관리자용 사용자 검색
     *
     * @param searchKeyword 검색어
     * @return 검색 결과
     */
    @Override
    public List<UserListItem> adminSearchUser(String searchKeyword) {
        List<UCUserListItem> users = userFetchService.fetchSearchUsers(searchKeyword);
        List<Long> userIds = users.stream()
                .map(UCUserListItem::getUserId)
                .collect(Collectors.toList());

        Map<Long, Integer> driveCountMap = dashboardFetchService.fetchDriveCountMap(userIds);
        return mergeUserData(users, driveCountMap);
    }

    /**
     * 사용자 정보 상세 조회
     *
     * @param userId 유저 ID
     * @return 사용자 상세 데이터
     */
    @Override
    public UserListItem adminGetUserDetail(Long userId) {
        UCUserDetailResData user = userFetchService.fetchUserDetail(userId);
        List<Long> userIds = new ArrayList<>();
        userIds.add(userId);

        Map<Long, Integer> driveCountMap = dashboardFetchService.fetchDriveCountMap(userIds);

        /**
         * TODO
         * 유저 서비스에서 특정 사용자 조회 시 응답 값으로
         * `joinedAt`, `seedBalance`, `isActive` 값 추가 필요
         * (UCUserDetailResData DTO 참고)
         */
        UserListItem userListItem = UserListItem.builder()
                .userId(userId)
                .nickname(user.getNickname())
                .email(user.getEmail())
                .experience(DateUtils.getYearsSince(
                        LocalDate.parse(user.getLicenseDate(), DateTimeFormatter.ISO_LOCAL_DATE_TIME).atStartOfDay())
                )
                .driveCount(driveCountMap.get(userId))
                .build();

        return userListItem;
    }

    /**
     * `운전 경력`, `가입 후 경과 개월 수`, `계정 활성 여부`를 기준으로 필터링
     *
     * @param req 필터링 데이터, 페이지네이션에 필요한 데이터
     * @return 필터링 결과
     */
    @Override
    public List<UserListItem> adminFilterUser(UserFilterReq req) {
        List<UCUserListItem> users = userFetchService.fetchFilteredUser(req);
        List<Long> userIds = users.stream()
                .map(UCUserListItem::getUserId)
                .collect(Collectors.toList());

        Map<Long, Integer> driveCountMap = dashboardFetchService.fetchDriveCountMap(userIds);

        return mergeUserData(users, driveCountMap);
    }

    /**
     * 사용자 비활성화
     *
     * @param userId 비활성화 대상의 유저 ID
     */
    @Override
    public void adminInactiveUser(Long userId) {
        userFetchService.inactiveUser(userId);
    }

    /**
     * 사용자 씨앗 내역 조회
     *
     * @param userId 씨앗 내역 조회 대상의 유저 ID
     * @param page 페이지 번호
     * @param pageSize 페이지당 데이터 수
     * @return 씨앗 내역
     */
    @Override
    public List<UserRewardItem> adminGetUserReward(Long userId, int page, int pageSize) {
        RewardFilterReq req = RewardFilterReq.builder()
                .userId(userId)
                .page(page)
                .pageSize(pageSize)
                .build();

        List<RewardFilterItem> rewards = rewardFetchService.fetchRewardFilter(req);

        List<UserRewardItem> userRewardItems = new ArrayList<>();
        for (RewardFilterItem item : rewards) {
            UserRewardItem userRewardItem = UserRewardItem.builder()
                    .issuedDate(item.getIssuedDate())
                    .amount(item.getAmount())
                    .reason(item.getReason())
                    .build();
            userRewardItems.add(userRewardItem);
        }

        return userRewardItems;
    }

    /**
     * 운전 이력 리스트에서 주행 ID 목록을 추출하여 List로 반환
     *
     * @param drives 운전 이력 리스트
     * @return drive ID 리스트
     */
    private List<Long> extractDriveIds(List<DCDriveListItem> drives) {
        return drives.stream()
                .map(DCDriveListItem::getDriveId)
                .collect(Collectors.toUnmodifiableList());
    }

    /**
     * 주행 리스트에 이벤트 및 리워드 정보를 매핑하여 사용자 주행 정보 생성
     *
     * @param drives 대시보드 서비스에서 조회한 운전 내역
     * @param rewardMap 리워드 서비스에서 조회한 리워드 내역
     * @return 주행 정보
     */
    private List<UserDriveListItem> enrichDriveItems(List<DCDriveListItem> drives, Map<Long, Integer> rewardMap) {
        List<UserDriveListItem> userDriveItems = new ArrayList<>();
        for (DCDriveListItem drive : drives) {
            List<UserDriveListEventItem> userDriveListItems = eventService.getEventCntByType(drive.getDriveId()).stream()
                    .map(item -> UserDriveListEventItem.builder()
                            .type(item.getType())
                            .count(item.getCount())
                            .build())
                    .collect(Collectors.toList());

            UserDriveListItem userDriveListItem = UserDriveListItem.builder()
                    .date(drive.getDate())
                    .driveDuration(drive.getDriveDuration())
                    .events(userDriveListItems)
                    .rewards(rewardMap.get(drive.getDriveId()))
                    .build();

            userDriveItems.add(userDriveListItem);
        }

        return userDriveItems;
    }

    /**
     * 사용자의 운전 내역 조회
     *
     * @param userId 유저 ID
     * @param page 페이지 번호
     * @param pageSize 페이지당 데이터 개수
     * @return 운전 내역 리스트
     */
    @Override
    public List<UserDriveListItem> adminGetUserDriveList(Long userId, int page, int pageSize) {
        List<DCDriveListItem> drives = dashboardFetchService.fetchDCDriveListByUserId(userId);

        List<Long> driveIds = extractDriveIds(drives);
        Map<Long, Integer> rewardMap = rewardFetchService.fetchRewardMapByDrive(new RewardByDriveReq(driveIds));

        return enrichDriveItems(drives, rewardMap);
    }
}
