package com.atlxc.VulnScan.handler;

import com.atlxc.VulnScan.utils.R;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Kyon-H
 * @date 2025/5/5 20:11
 */
@Slf4j
public class JsonAuthenticationFailureHandler implements AuthenticationFailureHandler {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e)
            throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        log.info("登录失败{}", e.getMessage());
        response.getWriter().write(objectMapper.writeValueAsString(
                R.error(HttpServletResponse.SC_UNAUTHORIZED, "登录失败" + e.getMessage())
        ));
    }
}
