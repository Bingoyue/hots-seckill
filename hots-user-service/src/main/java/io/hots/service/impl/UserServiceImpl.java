package io.hots.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.hots.controller.request.UserLoginRequest;
import io.hots.controller.request.UserRegisterRequest;
import io.hots.entity.LoginUser;
import io.hots.entity.UserEntity;
import io.hots.enums.BizCodeEnum;
import io.hots.mapper.UserMapper;
import io.hots.service.UserService;
import io.hots.util.CommonUtil;
import io.hots.util.JWTUtil;
import io.hots.util.ResultData;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.Md5Crypt;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * @author Albin_data@163.com
 * @date 2022/3/12 8:40 下午
 */

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public ResultData register(UserRegisterRequest userRegisterRequest) {

        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(userRegisterRequest, userEntity);

        //设置密码 生成秘钥 盐
        userEntity.setSecret("$1$" + CommonUtil.getStringNumRandom(8));

        //密码+盐处理
        String cryptPwd = Md5Crypt.md5Crypt(userRegisterRequest.getPwd().getBytes(), userEntity.getSecret());
        userEntity.setPwd(cryptPwd);

        //账号唯一性检查 hots
        if (checkUnique(userEntity.getName())) {
            int rows = userMapper.insert(userEntity);
            log.info("用户注册成功:{}", userEntity.toString());

            return ResultData.buildSuccess();
        } else {
            return ResultData.buildResult(BizCodeEnum.USER_REPEAT);
        }
    }

    /**
     * 校验用户账号唯一
     *
     * @param
     * @return
     */
    private boolean checkUnique(String name) {

        QueryWrapper queryWrapper = new QueryWrapper<UserEntity>().eq("name", name);

        List<UserEntity> list = userMapper.selectList(queryWrapper);

        return list.size() > 0 ? false : true;

    }



    @Override
    public ResultData login(UserLoginRequest userLoginRequest) {
        List<UserEntity> userEntityList = userMapper.selectList(new QueryWrapper<UserEntity>().eq("name", userLoginRequest.getName()));

        if (userEntityList != null && userEntityList.size() == 1) {
            //已经注册
            UserEntity userEntity = userEntityList.get(0);
            String cryptPwd = Md5Crypt.md5Crypt(userLoginRequest.getPwd().getBytes(), userEntity.getSecret());
            if (cryptPwd.equals(userEntity.getPwd())) {
                //登录成功,生成token
                LoginUser loginUser =  LoginUser.builder().build();
                BeanUtils.copyProperties(userEntity, loginUser);
                //生成token
                String accessToken = JWTUtil.geneJsonWebToken(loginUser);
                return ResultData.buildSuccess(accessToken);

            } else {
                //密码错误
                return ResultData.buildResult(BizCodeEnum.USER_PWD_ERROR);
            }
        } else {
            //未注册
            return ResultData.buildResult(BizCodeEnum.USER_UNREGISTER);
        }
    }
}
