package io.hots.vo;

import io.hots.entity.ProductEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Albin_data@163.com
 * @date 2022/3/12 10:18 下午
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductVO {

    /**
     * 商品编号
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 价格
     */
    private BigDecimal amount;

    /**
     * 库存
     */
    private Integer stock;

    public ProductVO(ProductEntity p) {
        this.id = p.getId();
        this.name = p.getName();
        this.amount = p.getAmount();
        this.stock = p.getStock();
    }
}
