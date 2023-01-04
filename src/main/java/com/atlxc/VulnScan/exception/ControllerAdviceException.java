package com.atlxc.VulnScan.exception;

import lombok.extern.slf4j.Slf4j;
import com.atlxc.VulnScan.utils.R;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.HashMap;
import java.util.Map;

/**
 * 集中处理所有异常
 */
@Slf4j
@RestControllerAdvice(basePackages = "com.atlxc.VulnScan.product.controller")
public class ControllerAdviceException {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R handleValidException(MethodArgumentNotValidException exception){
        log.error("数据校验出现问题{},异常类型{}",exception.getMessage(),exception.getClass());
        BindingResult bindingResult = exception.getBindingResult();
        // 获取数据校验的错误结果
        Map<String, String> result=new HashMap<>();
        bindingResult.getFieldErrors().forEach(fieldError -> {
            result.put(fieldError.getField(), fieldError.getDefaultMessage());
        });
        return R.error(400,"数据校验出现问题").put("data",result);
    }

    @ExceptionHandler(value = UserNameExistException.class)
    public R hadleUserNameExistException(UserNameExistException exception){
        log.error("数据校验出现问题{},异常类型{}",exception.getMessage(),exception.getClass());
        return R.error().put("data",exception.getMessage());
    }

    @ExceptionHandler(RRException.class)
    public R handleRRException(RRException e){
        R r = new R();
        r.put("code", e.getCode());
        r.put("msg", e.getMessage());

        return r;
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public R handlerNoFoundException(Exception e) {
        log.error(e.getMessage(), e);
        return R.error(404, "路径不存在，请检查路径是否正确");
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public R handleDuplicateKeyException(DuplicateKeyException e){
        log.error(e.getMessage(), e);
        return R.error("数据库中已存在该记录");
    }

    @ExceptionHandler(AuthorizationException.class)
    public R handleAuthorizationException(AuthorizationException e){
        log.error(e.getMessage(), e);
        return R.error("没有权限，请联系管理员授权");
    }

    // 处理任意类型异常
    @ExceptionHandler(value = Throwable.class)
    public R handleException(Throwable throwable){
        log.error("未知异常{},异常类型{}",throwable.getMessage(),throwable.getClass());
        return R.error();
    }
}
