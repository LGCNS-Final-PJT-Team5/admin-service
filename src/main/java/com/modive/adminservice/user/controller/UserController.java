package com.modive.adminservice.user.controller;

import com.modive.adminservice.global.dto.res.CommonRes;
import com.modive.adminservice.user.dto.res.UserListRes;
import com.modive.adminservice.user.entity.User;
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
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<CommonRes> getUserList(@RequestParam int page, @RequestParam int pageSize) {
        List<UserListRes> userListRes = userService.adminGetUserList(page, pageSize);

        Map<String, Object> data = new HashMap<>();
        data.put("users", userListRes);

        CommonRes res = CommonRes.builder()
                .status(HttpStatus.OK.value())
                .message("사용자 목록 조회에 성공하였습니다.")
                .data(data)
                .build();

        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
