package com.atlxc.VulnScan.handler;

import com.atlxc.VulnScan.utils.R;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Kyon-H
 * @date 2025/5/5 20:11
 */
@Slf4j
public class JsonAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        WebAuthenticationDetails details = (WebAuthenticationDetails) authentication.getDetails();
        log.info("{}登录成功, ip地址{}", authentication.getName(), details.getRemoteAddress());
        response.getWriter().write(objectMapper.writeValueAsString(R.ok(HttpStatus.SC_OK, "登录成功").put("username", authentication.getName())));
    }
}
