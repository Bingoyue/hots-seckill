package io.hots;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author Albin_data@163.com
 * @date 2022/4/17 11:14 上午
 */
@SpringBootApplication
@EnableDiscoveryClient
public class GatewayApplication {
    public static void main(String [] args){
        SpringApplication.run(GatewayApplication.class,args);
    }
}
