package io.hots.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author Albin_data@163.com
 * @date 2022/4/13 9:56 下午
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderMessage {
    /**
     * 消息id
     */
    private Long messageId;

    /**
     * 用户id
     */
    private long userId;

    /**
     * 商品编号
     */
    private long productId;


    public CreateOrderMessage(long userId, long productId) {
        this.userId = userId;
        this.productId = productId;
    }
}
