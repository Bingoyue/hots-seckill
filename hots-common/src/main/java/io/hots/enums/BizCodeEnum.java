package io.hots.enums;

import lombok.Getter;

/**
 * @author Albin_data@163.com
 * @date 2022/3/12 6:16 下午
 *
 * @Description 状态码定义约束，共6位数，前3位代表服务，后3位代表接口
 * 用户服务100,订单服务200，秒杀服务300，商品服务500
 */
public enum BizCodeEnum {
    /**
     * 通用操作码
     */
    OPS_REPEAT(110001,"重复操作"),

    /**
     * 用户
     */
    USER_PWD_ERROR(100001,"用户或者密码错误"),
    USER_NOT_LOGIN(100002,"用户未登录"),
    USER_REPEAT(100003,"用户已经存在"),
    USER_UNREGISTER(100004,"用户不存在"),

    /**
     * 订单
     */
    ORDER_CONFIRM_LOCK_PRODUCT_FAIL(200001,"创建订单-商品库存不足锁定失败"),
    ORDER_NOT_EXIST(200002,"订单不存在"),

    /**
     * 秒杀
     */
    SEC_KILL_TIME_ERROR(300001,"秒杀时间不正确"),
    SEC_KILL_ADDRESS_ERROR(300002,"秒杀地址不正确,重新获取"),
    SEC_KILL_ORDER_REPEAT(300003,"秒杀订单已经存在"),
    SEC_KILL_STOCK_SHORTAGE(300004,"秒杀商品库存不足"),
    SEC_KILL_SUBMIT_TOKEN_ERROR(300005,"秒杀提交token错误"),


    /**
     * 流控操作
     */
    CONTROL_FLOW(400001,"限流控制"),
    CONTROL_DEGRADE(400002,"降级控制");


    @Getter
    private String message;

    @Getter
    private int code;

    private BizCodeEnum(int code, String message){
        this.code = code;
        this.message = message;
    }


}
