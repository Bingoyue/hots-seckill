package io.hots.controller.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Albin_data@163.com
 * @date 2022/3/12 8:02 下午
 */


@ApiModel(value = "登录对象",description = "用户登录请求对象")
@Data
public class UserLoginRequest {
    @ApiModelProperty(value = "昵称", example = "Hots")
    private String name;

    @ApiModelProperty(value = "密码", example = "123456")
    private String pwd;
}
