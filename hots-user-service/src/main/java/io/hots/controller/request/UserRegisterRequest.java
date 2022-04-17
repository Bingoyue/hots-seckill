package io.hots.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Albin_data@163.com
 * @date 2022/3/12 7:57 下午
 */


@ApiModel(value = "用户注册对象",description = "用户注册请求对象")
@Data
public class UserRegisterRequest {
    @ApiModelProperty(value = "昵称",example = "hots")
    private String name;

    @ApiModelProperty(value = "密码",example = "123456")
    private String pwd;
}
