package io.hots.annotation;

import java.lang.annotation.*;

/**
 * @author Albin_data@163.com
 * @date 2022/3/21 8:13 下午
 */


@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RepeatSubmit {
}
