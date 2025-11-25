package com.wzh.intelligentcodeapp.exception;


import com.wzh.intelligentcodeapp.common.BaseResponse;
import com.wzh.intelligentcodeapp.common.ResultUtils;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * 全局异常处理器
 */
@Hidden   // spring v >= 3.4 以上版本，接口文档和RestControllerAdvice不兼容，需要使用hidden来忽略
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public BaseResponse<?> businessException (BusinessException e){
        log.error("BusinessException",e);
        return ResultUtils.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<?> businessException (RuntimeException e){
        log.error("RuntimeException",e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR);
    }
}
