package io.hots.controller;

import io.hots.entity.LoginUser;
import io.hots.enums.BizCodeEnum;
import io.hots.interceptor.LoginInterceptor;
import io.hots.service.OrderService;
import io.hots.util.ResultData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * @author Albin_data@163.com
 * @date 2022/3/13 2:43 下午
 */

@Api("订单服务")
@RestController
@RequestMapping("/api/order/v1")
@Slf4j
public class OrderController {

    @Autowired
    RedisTemplate<String,String> redisTemplate;

    @Autowired
    OrderService orderService;


    /**
     * 查询订单状态
     * @param outTradeNo
     * @return
     */
    @ApiOperation("查询订单状态")
    @GetMapping("query_state")
    public ResultData queryProductOrderState(@ApiParam("订单号") @RequestParam("out_trade_no")String outTradeNo){

        String state = orderService.queryProductOrderState(outTradeNo);

        return StringUtils.isBlank(state)?ResultData.buildResult(BizCodeEnum.ORDER_NOT_EXIST):ResultData.buildSuccess(state);

    }
}
