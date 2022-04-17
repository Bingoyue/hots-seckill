package io.hots.config;

import io.hots.interceptor.LoginInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Albin_data@163.com
 * @date 2022/3/14 7:42 下午
 */

@Configuration
@Slf4j
public class InterceptorConfig implements WebMvcConfigurer {


    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(new LoginInterceptor())
                //拦截的路径
                .addPathPatterns("/api/sec_kill/*/**")

                //排查不拦截的路径
                .excludePathPatterns("");

    }
}
