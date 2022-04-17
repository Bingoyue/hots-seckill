package io.hots.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.hots.entity.ProductEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author Albin_data@163.com
 * @date 2022/3/12 10:11 下午
 */
@Mapper
public interface ProductMapper extends BaseMapper<ProductEntity> {
    /**
     * 锁定商品库存
     * @param productId
     * @param buyNum
     * @return
     */
    int lockProductStock(@Param("productId") long productId, @Param("buyNum") int buyNum);

    /**
     * 解锁商品库存
     * @param productId
     * @param buyNum
     */
    void unlockProductStock(@Param("productId") Long productId, @Param("buyNum") Integer buyNum);

}

