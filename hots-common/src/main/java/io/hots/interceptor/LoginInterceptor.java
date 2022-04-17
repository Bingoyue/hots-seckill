package io.hots.interceptor;

import io.hots.entity.LoginUser;
import io.hots.enums.BizCodeEnum;
import io.hots.util.CommonUtil;
import io.hots.util.JWTUtil;
import io.hots.util.ResultData;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Albin_data@163.com
 * @date 2022/3/12 9:22 下午
 */

@Slf4j
public class LoginInterceptor implements HandlerInterceptor {


    public static ThreadLocal<LoginUser> threadLocal = new ThreadLocal<>();


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String token = request.getHeader("token");
        if(token == null) {
            token = request.getParameter("token");
        }

        if(StringUtils.isNotBlank(token)){
            //不为空
            Claims claims = JWTUtil.checkJWT(token);
            if(claims == null){
                //未登录
                CommonUtil.sendJsonMessage(response,ResultData.buildResult(BizCodeEnum.USER_NOT_LOGIN));
                return false;
            }

            long userId = Long.valueOf(claims.get("id").toString());
            String name = (String)claims.get("name");

            LoginUser loginUser = LoginUser
                    .builder()
                    .name(name)
                    .id(userId).build();

            //通过threadLocal传递用户登录信息
            threadLocal.set(loginUser);

            return true;

        }


        CommonUtil.sendJsonMessage(response, ResultData.buildResult(BizCodeEnum.USER_NOT_LOGIN));
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}

