package io.hots.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.hots.vo.ProductVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author Albin_data@163.com
 * @date 2022/3/13 4:15 下午
 */
@ApiModel(value = "商品锁定对象")
@Data
public class LockProductRequest {

    @ApiModelProperty(value = "用户id",example = "123")
    @JsonProperty("user_id")
    private Long userId;


    @ApiModelProperty(value = "订单id",example = "123")
    @JsonProperty("order_out_trade_no")
    private String orderOutTradeNo;

    @ApiModelProperty(value = "订单编号")
    @JsonProperty("product_id")
    private Long productId;
}
