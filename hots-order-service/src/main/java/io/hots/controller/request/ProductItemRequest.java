package io.hots.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.hots.entity.ProductItemEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Albin_data@163.com
 * @date 2022/3/13 9:00 下午
 */

@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductItemRequest implements Serializable {
    private static final long serialVersionUID = 1L;

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

    public ProductItemRequest(ProductItemEntity entity) {
        this.productId = entity.getProductId();
        this.name = entity.getName();
        this.amount = entity.getAmount();
        this.buyNum = entity.getBuyNum();
    }
}
