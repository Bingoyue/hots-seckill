package io.hots.service;

import io.hots.controller.request.LockProductRequest;
import io.hots.controller.request.ProductItemRequest;
import io.hots.entity.ProductMessage;
import io.hots.util.ResultData;

import java.util.Map;

/**
 * @author Albin_data@163.com
 * @date 2022/3/12 9:46 下午
 */
public interface ProductService {

    /**
     * 分页查询商品列表
     * @param page
     * @param size
     * @return
     */
    Map<String,Object> page(int page, int size);

    /**
     * 锁定商品库存
     * @param lockProductRequest
     * @return
     */
    ResultData lockProductStock(LockProductRequest lockProductRequest);


    /**
     * 释放商品库存
     * @param productMessage
     * @return
     */
    boolean releaseProductStock(ProductMessage productMessage);
}
