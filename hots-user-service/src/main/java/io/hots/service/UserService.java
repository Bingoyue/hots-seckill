package io.hots.service;

import io.hots.controller.request.UserLoginRequest;
import io.hots.controller.request.UserRegisterRequest;
import io.hots.util.ResultData;

/**
 * @author Albin_data@163.com
 * @date 2022/3/12 8:00 下午
 */
public interface UserService {
    /**
     * 用户注册
     * @param userRegisterRequest
     * @return
     */
    ResultData register(UserRegisterRequest userRegisterRequest);

    /**
     * 用户登录
     * @param userLoginRequest
     * @return
     */
    ResultData login(UserLoginRequest userLoginRequest);

}
