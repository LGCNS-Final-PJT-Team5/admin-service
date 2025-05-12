package com.modive.adminservice.user.controller;

import com.modive.adminservice.global.dto.res.CommonRes;
import com.modive.adminservice.global.error.dto.ErrorRes;
import com.modive.adminservice.user.dto.req.UserFilterReq;
import com.modive.adminservice.user.dto.res.UserListItem;
import com.modive.adminservice.user.dto.res.UserRewardItem;
import com.modive.adminservice.user.service.UserFetchService;
import com.modive.adminservice.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "회원 관리", description = "회원 관리와 관련된 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/users")
public class UserController {
    private final UserService userService;
    private final UserFetchService userFetchService;

    @GetMapping
    @Operation(summary = "사용자 전체 목록 조회", description = "등록된 전체 사용자를 페이징으로 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
            content = {@Content(schema = @Schema(implementation = CommonRes.class))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                content = {@Content(schema = @Schema(implementation = ErrorRes.class))})
    })
    public ResponseEntity<CommonRes> getUserList(
            @Parameter(name = "page", description = "페이지 번호", example = "1", required = true)
            @RequestParam int page,

            @Parameter(name = "pageSize", description = "페이지당 데이터 수", example = "10", required = true)
            @RequestParam int pageSize) {
        List<UserListItem> userListItems = userService.adminGetUserList(page, pageSize);

        Map<String, Object> data = new HashMap<>();
        data.put("users", userListItems);

        return new ResponseEntity<>(
                CommonRes.success(data, "사용자 목록 조회에 성공하였습니다."),
                HttpStatus.OK
        );
    }

    @GetMapping("/search")
    @Operation(summary = "사용자 검색", description = "이메일로 사용자 검색합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = {@Content(schema = @Schema(implementation = CommonRes.class))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                content = {@Content(schema = @Schema(implementation = ErrorRes.class))})
    })
    public ResponseEntity<CommonRes> searchUser(
            @Parameter(name = "searchKeyword", description = "사용자 이메일", example = "user@modive.com", required = true)
            @RequestParam String searchKeyword
    ) {
        List<UserListItem> userListItems = userService.adminSearchUser(searchKeyword);

        Map<String, Object> data = new HashMap<>();
        data.put("searchResult", userListItems);

        return new ResponseEntity<>(
                CommonRes.success(data, "사용자 검색에 성공하였습니다."),
                HttpStatus.OK
        );
    }

    @GetMapping("/{userId}")
    @Operation(summary = "특정 사용자 조회", description = "userId로 사용자 상세 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = {@Content(schema = @Schema(implementation = CommonRes.class))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = {@Content(schema = @Schema(implementation = ErrorRes.class))}),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = {@Content(schema = @Schema(implementation = ErrorRes.class))})
    })
    public ResponseEntity<CommonRes> getUser(
            @Schema(description = "유저 ID", example = "1")
            @PathVariable("userId") Long userId
    ) {
        UserListItem userListItem = userService.adminGetUserDetail(userId);

        Map<String, Object> data = new HashMap<>();
        data.put("userDetail", userListItem);

        return new ResponseEntity<>(
                CommonRes.success(data, "사용자 상세 조회에 성공하였습니다."),
                HttpStatus.OK
        );
    }

    @GetMapping("/filter")
    @Operation(summary = "사용자 조건 필터링", description = "운전 경력, 가입 기간, 활성 여부 기준으로 사용자를 필터링합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = {@Content(schema = @Schema(implementation = CommonRes.class))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<CommonRes> userFilter(
            @Parameter(name = "UserFilterReq", description = "필터링 데이터, 페이지네이션에 필요한 데이터")
            @ModelAttribute UserFilterReq req
    ) {
        List<UserListItem> userListItems = userService.adminFilterUser(req);

        Map<String, Object> data = new HashMap<>();
        data.put("users", userListItems);

        return new ResponseEntity<>(
                CommonRes.success(data, "사용자 필터링에 성공하였습니다."),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{userId}")
    @Operation(summary = "특정 사용자 조회", description = "userId로 사용자 상세 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = {@Content(schema = @Schema(implementation = CommonRes.class))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = {@Content(schema = @Schema(implementation = ErrorRes.class))}),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = {@Content(schema = @Schema(implementation = ErrorRes.class))})
    })
    public ResponseEntity<CommonRes> deleteUser(
            @Schema(description = "유저 ID", example = "1")
            @PathVariable("userId") Long userId
    ) {
        userService.adminInactiveUser(userId);

        return new ResponseEntity<>(
                CommonRes.success(null, "사용자 비활성화 처리가 완료되었습니다."),
                HttpStatus.OK
        );
    }


    @GetMapping("/{userId}/rewards")
    @Operation(summary = "사용자 씨앗 내역 조회", description = "userId 기준으로 씨앗 내역을 페이징으로 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = {@Content(schema = @Schema(implementation = CommonRes.class))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = {@Content(schema = @Schema(implementation = ErrorRes.class))}),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = {@Content(schema = @Schema(implementation = ErrorRes.class))}),
    })
    public ResponseEntity<CommonRes> getUserRewards(
            @Schema(description = "유저 ID", example = "1")
            @PathVariable("userId") Long userId,

            @Parameter(name = "page", description = "페이지 번호", example = "2", required = true)
            @RequestParam int page,

            @Parameter(name = "pageSize", description = "페이지당 항목 수", example = "10", required = true)
            @RequestParam int pageSize
    ) {
        List<UserRewardItem> rewardHistory = userService.adminGetUserReward(userId, page, pageSize);

        Map<String, Object> data = new HashMap<>();
        data.put("rewardHistory", rewardHistory);

        return new ResponseEntity<>(
                CommonRes.success(data, "사용자 씨앗 내역 조회에 성공하였습니다."),
                HttpStatus.OK
        );
    }
}
