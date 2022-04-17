package io.hots.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author Albin_data@163.com
 * @date 2022/4/10 8:27 下午
 */

@ApiModel(value = "商品秒杀活动时间")
@Data
public class SecKillTimeRequest {
    /**
     * 商品id
     */
    @JsonProperty("product_id")
    private Long productId;

    /**
     * 开始时间
     */
    @JsonProperty("start_time")
    private String startTime;

    /**
     * 结束时间
     */
    @JsonProperty("end_time")
    private String endTime;
}
