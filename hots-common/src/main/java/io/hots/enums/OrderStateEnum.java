package io.hots.enums;

/**
 * @author Albin_data@163.com
 * @date 2022/3/14 7:08 下午
 */
public enum OrderStateEnum {
    /**
     * 未支付订单
     */
    NEW,


    /**
     * 已经支付订单
     */
    PAY,

    /**
     * 超时取消订单
     */
    CANCEL;
}
