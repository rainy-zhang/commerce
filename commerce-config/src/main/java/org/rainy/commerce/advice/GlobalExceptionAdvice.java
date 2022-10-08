package org.rainy.commerce.advice;

import lombok.extern.slf4j.Slf4j;
import org.rainy.commerce.vo.CommonResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 全局异常处理
 * </p>
 *
 * @author zhangyu
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionAdvice {

    @ExceptionHandler(value = Exception.class)
    public CommonResponse<String> handleException(HttpServletRequest request, Exception ex) {
        CommonResponse<String> response = new CommonResponse<>(-1, "error");
        response.setData(ex.getMessage());
        log.error("commerce service has error: [{}]", ex.getMessage(), ex );
        return response;
    }

}
