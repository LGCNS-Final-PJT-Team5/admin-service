package com.modive.adminservice.user.controller;

import com.modive.adminservice.global.dto.res.CommonRes;
import com.modive.adminservice.global.error.dto.ErrorRes;
import com.modive.adminservice.user.dto.res.UserListItem;
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

        CommonRes res = CommonRes.builder()
                .status(HttpStatus.OK.value())
                .message("사용자 목록 조회에 성공하였습니다.")
                .data(data)
                .build();

        return new ResponseEntity<>(res, HttpStatus.OK);
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
            @RequestParam String searchKeyword) {
        List<UserListItem> userListItems = userService.adminSearchUser(searchKeyword);

        Map<String, Object> data = new HashMap<>();
        data.put("searchResult", userListItems);

        CommonRes res = CommonRes.builder()
                .status(HttpStatus.OK.value())
                .message("사용자 검색에 성공하였습니다.")
                .data(data)
                .build();

        return new ResponseEntity<>(res, HttpStatus.OK);
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

        CommonRes res = CommonRes.builder()
                .status(HttpStatus.OK.value())
                .message("사용자 조회에 성공하였습니다.")
                .data(data)
                .build();

        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
