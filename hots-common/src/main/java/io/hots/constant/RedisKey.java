package io.hots.constant;

/**
 * @author Albin_data@163.com
 * @date 2022/3/13 3:02 下午
 */

public class RedisKey {

    /**
     * 提交表单的token key   order:submit:userId:token
     */
    public static final String SUBMIT_ORDER_TOKEN_KEY = "order:submit:%s:%s";


    /**
     * 秒杀地址的key   sec:address:token
     */
    public static final String SEC_KILL_ADDRESS_KEY = "sec:address:%s";

    /**
     * 秒杀库存的key   sec:stock:product_id
     */
    public static final String SEC_KILL_STOCK_KEY = "sec:stock:%s";

    /**
     * 秒杀时间的key   sec:time:product_id
     */
    public static final String SEC_KILL_TIME_KEY = "sec:time:%s";
}
