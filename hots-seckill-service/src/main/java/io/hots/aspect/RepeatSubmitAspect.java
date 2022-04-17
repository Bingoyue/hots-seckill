package io.hots.aspect;

import io.hots.annotation.RepeatSubmit;
import io.hots.constant.RedisKey;
import io.hots.entity.LoginUser;
import io.hots.enums.BizCodeEnum;
import io.hots.exception.BizException;
import io.hots.interceptor.LoginInterceptor;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Albin_data@163.com
 * @date 2022/3/21 8:22 下午
 */

@Aspect
@Component
public class RepeatSubmitAspect {

    @Autowired
    StringRedisTemplate redisTemplate;


    @Pointcut("@annotation(repeatSubmit)")
    public void pointcutNoRepeatSubmit(RepeatSubmit repeatSubmit) {
    }

    @Around("pointcutNoRepeatSubmit(noRepeatSubmit)")
    public Object around(ProceedingJoinPoint joinPoint, RepeatSubmit noRepeatSubmit) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        String requestToken = request.getHeader("request-token");
        if (StringUtils.isBlank(requestToken)) {
            throw new BizException(BizCodeEnum.SEC_KILL_SUBMIT_TOKEN_ERROR);
        }

        LoginUser loginUser = LoginInterceptor.threadLocal.get();
        String key = String.format(RedisKey.SUBMIT_ORDER_TOKEN_KEY, loginUser.getId(), requestToken);
        boolean res = redisTemplate.delete(key);

        if (!res) {
            throw new BizException(BizCodeEnum.OPS_REPEAT);
        }

        Object object = joinPoint.proceed();
        return object;
    }
}
