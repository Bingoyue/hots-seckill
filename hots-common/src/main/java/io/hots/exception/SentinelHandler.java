package io.hots.exception;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import io.hots.enums.BizCodeEnum;
import io.hots.util.CommonUtil;
import io.hots.util.ResultData;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Albin_data@163.com
 * @date 2022/4/17 8:40 下午
 */

@Component
public class SentinelHandler implements BlockExceptionHandler {


    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, BlockException e) throws Exception {

        ResultData jsonData = null;

        if(e instanceof FlowException){
            jsonData = ResultData.buildResult(BizCodeEnum.CONTROL_FLOW);

        }else if(e instanceof DegradeException){
            jsonData = ResultData.buildResult(BizCodeEnum.CONTROL_DEGRADE);

        }
        httpServletResponse.setStatus(200);

        CommonUtil.sendJsonMessage(httpServletResponse,jsonData);

    }
}
