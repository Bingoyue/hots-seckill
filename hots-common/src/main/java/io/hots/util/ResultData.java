package io.hots.util;

import io.hots.enums.BizCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Albin_data@163.com
 * @date 2022/3/12 6:11 下午
 */


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultData {

    /**
     * 状态码 0 表示成功
     */

    private Integer code;
    /**
     * 数据
     */
    private Object data;
    /**
     * 描述
     */
    private String msg;


    /**
     * 成功，不传入数据
     * @return
     */
    public static ResultData buildSuccess() {
        return new ResultData(0, null, null);
    }

    /**
     *  成功，传入数据
     * @param data
     * @return
     */
    public static ResultData buildSuccess(Object data) {
        return new ResultData(0, data, null);
    }

    /**
     * 失败，传入描述信息
     * @param msg
     * @return
     */
    public static ResultData buildError(String msg) {
        return new ResultData(-1, null, msg);
    }



    /**
     * 自定义状态码和错误信息
     * @param code
     * @param msg
     * @return
     */
    public static ResultData buildCodeAndMsg(int code, String msg) {
        return new ResultData(code, null, msg);
    }

    /**
     * 传入枚举，返回信息
     * @param codeEnum
     * @return
     */
    public static ResultData buildResult(BizCodeEnum codeEnum){
        return ResultData.buildCodeAndMsg(codeEnum.getCode(),codeEnum.getMessage());
    }
}
