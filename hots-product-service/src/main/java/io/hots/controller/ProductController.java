package io.hots.controller;

import java.util.Map;
import io.hots.controller.request.LockProductRequest;
import io.hots.service.ProductService;
import io.hots.util.ResultData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @author Albin_data@163.com
 * @date 2022/3/12 9:35 下午
 */

@Api("商品服务")
@RestController
@RequestMapping("/api/product/v1")
public class ProductController {

    @Autowired
    ProductService productService;

    @ApiOperation("分页查询商品列表")
    @GetMapping("page")
    public ResultData pageProductList(
            @ApiParam(value = "当前页")  @RequestParam(value = "page", defaultValue = "1") int page,
            @ApiParam(value = "每页显示多少条") @RequestParam(value = "size", defaultValue = "10") int size
    ){

        Map<String,Object> pageResult = productService.page(page,size);

        return ResultData.buildSuccess(pageResult);

    }


    @ApiOperation("锁定商品库存")
    @PostMapping("lock_products")
    public ResultData lockProducts(@ApiParam("购物项") @RequestBody LockProductRequest lockProductRequest){

        ResultData resultData = productService.lockProductStock(lockProductRequest);

        return resultData;
    }


}
