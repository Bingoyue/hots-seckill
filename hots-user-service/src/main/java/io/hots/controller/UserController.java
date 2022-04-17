package io.hots.controller;

import io.hots.controller.request.UserLoginRequest;
import io.hots.controller.request.UserRegisterRequest;
import io.hots.service.UserService;
import io.hots.util.ResultData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Albin_data@163.com
 * @date 2022/3/12 6:01 下午
 */

@Api("用户服务")
@RestController
@RequestMapping("/api/user/v1")
public class UserController {

    @Autowired
    UserService userService;


    /**
     *  用户注册
     * @param userRegisterRequest
     * @return
     */
    @ApiOperation("用户注册")
    @PostMapping("register")
    public ResultData register(@ApiParam("用户注册对象") @RequestBody UserRegisterRequest userRegisterRequest){

        ResultData resultData = userService.register(userRegisterRequest);
        return resultData;
    }

    /**
     * 用户登录
     * @return
     */
    @ApiOperation("用户登录")
    @PostMapping("login")
    public ResultData login(@ApiParam("用户登录对象") @RequestBody UserLoginRequest userLoginRequest){

        ResultData resultData = userService.login(userLoginRequest);

        return resultData;
    }

}
