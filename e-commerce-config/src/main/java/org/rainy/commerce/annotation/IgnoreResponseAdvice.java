package org.rainy.commerce.annotation;

import org.rainy.commerce.vo.CommonResponse;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * 忽略通用返回，当返回值不需要封装为{@link CommonResponse}对象，可以类或者方法上加上此注解
 * </p>
 *
 * @author zhangyu
 */
@Target(value = {ElementType.TYPE, ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME )
public @interface IgnoreResponseAdvice {
}
