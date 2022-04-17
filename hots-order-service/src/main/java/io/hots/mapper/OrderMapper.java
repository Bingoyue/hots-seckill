package io.hots.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.hots.entity.OrderEntity;
import org.apache.ibatis.annotations.Param;

/**
 * @author Albin_data@163.com
 * @date 2022/3/14 7:10 下午
 */

public interface OrderMapper  extends BaseMapper<OrderEntity> {

    /**
     * 更新订单状态
     * @param outTradeNo
     * @param newState
     * @param oldState
     */
    void updateOrderState(@Param("outTradeNo") String outTradeNo, @Param("newState") String newState, @Param("oldState") String oldState);
}
