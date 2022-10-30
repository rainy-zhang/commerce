package org.rainy.commerce.advice;

import org.rainy.commerce.annotation.IgnoreResponseAdvice;
import org.rainy.commerce.vo.CommonResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Objects;

/**
 * <p>
 * 拦截接口返回值，并封装为{@link CommonResponse}对象
 * </p>
 *
 * @author zhangyu
 */
// 指定要拦截的包的范围，不然可能会拦截到其他依赖提供的接口的返回结果
@RestControllerAdvice(value = "org.rainy.commerce")
public class CommonResponseAdvice implements ResponseBodyAdvice<Object> {

    @Override
    @SuppressWarnings("all ")
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        if (methodParameter.getDeclaringClass().isAnnotationPresent(IgnoreResponseAdvice.class)) {
            return false;
        }
        if (Objects.requireNonNull(methodParameter.getMethod()).isAnnotationPresent(IgnoreResponseAdvice.class)) {
            return false;
        }
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        CommonResponse<Object> response = new CommonResponse<>(0, "ok");
        if (o == null) {
            return response;
        }
        if (o instanceof CommonResponse) {
            return o;
        }
        response.setData(o);
        return response;
    }
}
