package io.hots.service;

import io.hots.entity.CreateOrderMessage;
import io.hots.entity.OrderMessage;
import io.hots.util.ResultData;

/**
 * @author Albin_data@163.com
 * @date 2022/3/13 3:43 下午
 */
public interface OrderService {

    /**
     * 创建订单
     * @param createOrderMessage
     * @return
     */
    boolean createOrder(CreateOrderMessage createOrderMessage);


    /**
     * 查询订单状态
     * @param outTradeNo
     * @return
     */
    String queryProductOrderState(String outTradeNo);


    /**
     * 关闭订单
     * @param orderMessage
     * @return
     */
    public boolean closeProductOrder(OrderMessage orderMessage);
}
