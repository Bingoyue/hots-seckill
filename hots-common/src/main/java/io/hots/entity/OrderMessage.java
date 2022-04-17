package io.hots.entity;

import lombok.Data;

/**
 * @author Albin_data@163.com
 * @date 2022/3/14 7:40 下午
 */

@Data
public class OrderMessage {
    /**
     * 消息id
     */
    private Long messageId;

    /**
     * 订单号
     */
    private String outTradeNo;
}
