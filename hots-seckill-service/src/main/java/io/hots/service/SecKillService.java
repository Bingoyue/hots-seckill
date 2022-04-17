package io.hots.service;

import io.hots.controller.request.SecKillItemRequest;
import io.hots.controller.request.SecKillStockRequest;
import io.hots.controller.request.SecKillTimeRequest;
import io.hots.util.ResultData;

/**
 * @author Albin_data@163.com
 * @date 2022/4/15 7:52 下午
 */
public interface SecKillService {

    ResultData getSecKillAddress(String product_id);

    ResultData doSecKill(SecKillItemRequest request, String path);

    void setSecKillTime(SecKillTimeRequest request);

    void setStock(SecKillStockRequest request);

    String getSecRepeatToken();

}
