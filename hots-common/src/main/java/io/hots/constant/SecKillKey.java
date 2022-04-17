package io.hots.constant;

/**
 * @author Albin_data@163.com
 * @date 2022/4/10 7:41 下午
 */
public class SecKillKey {

    /**
     * 秒杀地址的TOKEN   sec:address:userId:productId:token
     */
    public static final String SEC_KILL_ADDRESS_TOKEN = "sec:address:%s:%s:%s";

    /**
     * 秒杀订单标记key   sec:order:userId:productId
     */
    public static final String SEC_KILL_ORDER_KEY = "sec:order:%s:%s";
}
