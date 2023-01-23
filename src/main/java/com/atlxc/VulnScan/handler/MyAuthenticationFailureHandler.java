package com.atlxc.VulnScan.handler;

import com.atlxc.VulnScan.exception.UserNotExistException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 验证失败Handler
 * @ClassName: MyAuthenticationFailureHandler
 */
@Slf4j
public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.info("onAuthenticationFailure");
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        Map<String, Object> message = new HashMap<>();
        if (exception instanceof LockedException) {
            message.put("msg", "账户被锁定，请联系管理员!");
        } else if (exception instanceof CredentialsExpiredException) {
            message.put("msg", "密码过期，请联系管理员!");
        } else if (exception instanceof AccountExpiredException) {
            message.put("msg", "账户过期，请联系管理员!");
        } else if (exception instanceof DisabledException) {
            message.put("msg", "账户被禁用，请联系管理员!");
        } else if (exception.getCause() instanceof UserNotExistException) {
            message.put("msg", "账户不存在!");
        } else if (exception instanceof BadCredentialsException) {
            message.put("msg", "密码输入错误，请重新输入!");
        }
        out.write(new ObjectMapper().writeValueAsString(message));
        out.flush();
        out.close();
    }
}
