package com.modive.adminservice.external.user.client;

import com.modive.adminservice.global.dto.res.CommonRes;
import com.modive.adminservice.api.user.dto.req.UserFilterReq;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * user-service와 통신하는 Feign Client.
 */
@FeignClient(name="user-service")
public interface UserClient {
    /**
     * 페이지네이션 정보를 기반으로 사용자 목록 조회
     *
     * @param page 조회할 페이지 번호
     * @param pageSize 한 페이지당 사용자 수
     * @return CommonRes 형태의 사용자 목록 응답
     */
    @GetMapping("/user/list")
    CommonRes getUserList(@RequestParam int page, @RequestParam int pageSize);

    /**
     * 이메일을 기반으로 사용자 조회
     *
     * @param searchKeyword 검색어
     * @return CommonRes 형태의 검색 결과 응답
     */
    @GetMapping("/user")
    CommonRes searchUser(@RequestParam String searchKeyword);

    /**
     * userId를 기반으로 사용자 상세 조회
     *
     * @param userId 유저ID
     * @return CommonRes 형태의 상세 조회 결과 응답
     */
    @GetMapping("/user/{userId}")
    CommonRes getUserDetail(@PathVariable Long userId);

    /**
     * 페이지네이션 정보를 기반으로 사용자 필터링 결과 조회
     *
     * @param params 최소 운전 경력, 최대 운전 경력, ... 등 필터링 조건과 페이지네이션에 필요한 데이터 맵핑
     * @return CommonRes 형태의 필터링 결과 응답
     */
    @GetMapping("/user/filter")
    CommonRes getFilteredUser(@SpringQueryMap UserFilterReq params);

    /**
     * userId를 기반으로 사용자 비활성화
     *
     * @param userId 유저ID
     * @return 비활성화 결과
     */
    @DeleteMapping("/user/{userId}")
    CommonRes deleteUser(@PathVariable Long userId);

    @GetMapping("/user/total")
    CommonRes getTotalUser();

    @GetMapping("/user/total-cars")
    CommonRes getTotalUserCars();
}
