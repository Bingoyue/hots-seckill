package io.hots.controller;


import io.hots.annotation.RepeatSubmit;
import io.hots.controller.request.SecKillItemRequest;
import io.hots.service.SecKillService;
import io.hots.util.ResultData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Albin_data@163.com
 * @date 2022/4/10 7:18 下午
 */

@Api("秒杀服务")
@RestController
@RequestMapping("/api/sec_kill/v1")
@Slf4j
public class SecKillController {

    @Autowired
    SecKillService secKillService;


    @ApiOperation("获取秒杀地址")
    @GetMapping("/get_address/{product_id}")
    public ResultData getSecKillAddress(@PathVariable String product_id){
        return secKillService.getSecKillAddress(product_id);
    }

    @ApiOperation("获取提交订单令牌")
    @GetMapping("get_token")
    public ResultData getSecRepeatToken(){
        String token = secKillService.getSecRepeatToken();
        return ResultData.buildSuccess(token);
    }

    @ApiOperation("秒杀")
    @PostMapping("/{path}/do_sec_kill")
    @RepeatSubmit
    public ResultData secKill(@RequestBody SecKillItemRequest request, @PathVariable("path")String path){
        return secKillService.doSecKill(request, path);
    }



}
