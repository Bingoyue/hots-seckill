package io.hots.entity;

import lombok.Data;

/**
 * @author Albin_data@163.com
 * @date 2022/3/13 1:52 下午
 */

@Data
public class ProductMessage {


    /**
     * 消息队列id
     */
    private long messageId;

    /**
     * 订单号
     */
    private String outTradeNo;

    /**
     * 商品id
     */
    private String productId;

    /**
     * 库存锁定taskId
     */
    private long taskId;
}

