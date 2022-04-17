package io.hots.feign;

import io.hots.controller.request.LockProductRequest;
import io.hots.util.ResultData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author Albin_data@163.com
 * @date 2022/3/13 4:20 下午
 */

@FeignClient(name = "hots-product-service")
public interface ProductFeignService {
    /**
     * 锁定商品购物项库存
     * @param lockProductRequest
     * @return
     */
    @PostMapping("/api/product/v1/lock_products")
    ResultData lockProductStock(@RequestBody LockProductRequest lockProductRequest);
}
