package io.hots.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Albin_data@163.com
 * @date 2022/3/13 9:10 上午
 */

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("product_task")
@AllArgsConstructor
@NoArgsConstructor
public class ProductTaskEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 商品id
     */
    private Long productId;

    /**
     * 购买数量
     */
    private Integer buyNum;

    /**
     * 锁定状态锁定LOCK  完成FINISH-取消CANCEL
     */
    private String lockState;

    /**
     * 订单号编号
     */
    private String outTradeNo;

    private Date createTime;


    public ProductTaskEntity(Long productId, String lockState, String outTradeNo) {
        this.productId = productId;
        this.buyNum = 1;
        this.lockState = lockState;
        this.outTradeNo = outTradeNo;
        this.createTime = new Date();
    }
}

