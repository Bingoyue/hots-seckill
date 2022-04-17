package io.hots.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author Albin_data@163.com
 * @date 2022/4/10 7:16 下午
 */

@ApiModel(value = "商品秒杀信息")
@Data
public class SecKillItemRequest {

    //商品编号
    @JsonProperty("product_id")
    private Long productId;
}
