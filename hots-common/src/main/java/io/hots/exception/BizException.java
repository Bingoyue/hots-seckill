package io.hots.exception;

import io.hots.enums.BizCodeEnum;
import lombok.Data;

/**
 * @author Albin_data@163.com
 * @date 2022/3/13 8:24 上午
 */
@Data
public class BizException extends RuntimeException {

    private int code;
    private String msg;

    public BizException(int code, String msg){
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public BizException(BizCodeEnum bizCodeEnum){
        super(bizCodeEnum.getMessage());
        this.code = bizCodeEnum.getCode();
        this.msg = bizCodeEnum.getMessage();
    }


}
