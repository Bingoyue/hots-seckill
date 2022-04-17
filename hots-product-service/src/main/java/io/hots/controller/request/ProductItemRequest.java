package io.hots.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Albin_data@163.com
 * @date 2022/3/12 9:49 下午
 */
@ApiModel
@Data
public class ProductItemRequest {

    @ApiModelProperty(value = "商品编号",example = "100")
    @JsonProperty("product_id")
    private long productId;

    @ApiModelProperty(value = "商品名称",example = "深度学习")
    @JsonProperty("name")
    private String name;

    @ApiModelProperty(value = "商品单价")
    @JsonProperty("amount")
    private BigDecimal amount;


    @ApiModelProperty(value = "购买数量",example = "1")
    @JsonProperty("buy_num")
    private int buyNum;

}
