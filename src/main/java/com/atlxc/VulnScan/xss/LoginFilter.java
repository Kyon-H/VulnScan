package com.atlxc.VulnScan.xss;

import com.google.code.kaptcha.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (!request.getMethod().equals("POST")) {
            log.error("Request is not a POST!");
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        String kaptcha = request.getParameter("kaptcha");
        String sessionKaptcha = (String) request.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
        if (!StringUtils.isEmpty(kaptcha) && !StringUtils.isEmpty(sessionKaptcha) && kaptcha.equalsIgnoreCase(sessionKaptcha)) {
            log.error("Kaptcha is right!");
            return super.attemptAuthentication(request, response);
        }
        log.error("Kaptcha is wrong!");
        throw new AuthenticationServiceException("验证码不正确");
    }
}
