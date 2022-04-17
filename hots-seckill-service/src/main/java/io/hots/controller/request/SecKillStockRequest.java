package io.hots.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author Albin_data@163.com
 * @date 2022/4/13 10:15 下午
 */


@ApiModel(value = "商品库存信息")
@Data
public class SecKillStockRequest {
    //商品编号
    @JsonProperty("product_id")
    private Long productId;

    //商品数量
    private Integer num;
}
