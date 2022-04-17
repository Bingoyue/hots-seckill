package io.hots.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Albin_data@163.com
 * @date 2022/3/13 9:07 下午
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("product_order")
public class OrderEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 订单唯一标识
     */
    private String outTradeNo;

    /**
     * NEW 未支付订单,PAY已经支付订单,CANCEL超时取消订单
     */
    private String state;


    /**
     * 订单总金额
     */
    private BigDecimal totalAmount;


    /**
     * 用户id
     */
    private Long userId;

    /**
     * 0表示未删除，1表示已经删除
     */
    private Integer del;

    /**
     * 生成时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

}

