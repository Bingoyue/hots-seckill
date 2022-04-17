package io.hots.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.hots.entity.ProductItemEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Albin_data@163.com
 * @date 2022/3/14 7:28 下午
 */
public interface OrderItemMapper extends BaseMapper<ProductItemEntity> {


    /**
     * 批量插入
     * @param list
     */
    void insertBatch(@Param("orderItemList") List<ProductItemEntity> list);
}
