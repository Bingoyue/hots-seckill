package io.hots.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.hots.controller.request.ProductItemRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Albin_data@163.com
 * @date 2022/3/13 8:49 下午
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@TableName("order_item")
public class ProductItemEntity {

    private long productId;

    private String outTradeNo;

    private long orderId;

    private String name;

    private BigDecimal amount;

    private BigDecimal totalAmount;

    private int buyNum;

    private Date createTime;

    public ProductItemEntity(ProductItemRequest r) {
        this.productId = r.getProductId();
        this.name = r.getName();
        this.amount = r.getAmount();
        this.buyNum = r.getBuyNum();
    }
}
