package io.hots.feign;

import io.hots.util.ResultData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Albin_data@163.com
 * @date 2022/3/15 9:13 下午
 */


@FeignClient(name = "hots-order-service")
public interface OrderFeignService {

    /**
     * 查询订单状态
     * @param outTradeNo
     * @return
     */
    @GetMapping("/api/order/v1/query_state")
    ResultData queryProductOrderState(@RequestParam("out_trade_no") String outTradeNo);

}
