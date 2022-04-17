package io.hots.controller;


import io.hots.controller.request.SecKillStockRequest;
import io.hots.controller.request.SecKillTimeRequest;
import io.hots.service.SecKillService;
import io.hots.util.ResultData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @author Albin_data@163.com
 * @date 2022/4/10 8:46 下午
 */

@Api("秒杀服务-加载秒杀相关数据到redis")
@RestController
@RequestMapping("/api/init_sec_kill/v1")
@Slf4j
public class InitSecKillController {

    @Autowired
    SecKillService secKillService;


    @ApiOperation("添加商品秒杀开始时间、结束时间")
    @PostMapping("/set_sec_time")
    public ResultData setSecKillTime(@RequestBody SecKillTimeRequest request){
        secKillService.setSecKillTime(request);
        return ResultData.buildSuccess();
    }


    @ApiOperation("添加秒杀商品和库存")
    @PostMapping("/set_stock")
    public ResultData setStock(@RequestBody SecKillStockRequest request){
        secKillService.setStock(request);
        return ResultData.buildSuccess();
    }




}
