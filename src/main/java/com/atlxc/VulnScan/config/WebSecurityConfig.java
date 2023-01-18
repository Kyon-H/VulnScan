package com.atlxc.VulnScan.config;

import com.atlxc.VulnScan.product.service.impl.CustomUserDetailsServiceImpl;
import com.atlxc.VulnScan.xss.LoginFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * //Security配置类必须继承WebSecurityConfigurerAdapter，并且重写configure方法
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private CustomUserDetailsServiceImpl userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 设置密码加密方式，验证密码的在这里
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }

    /**
     * 设置登录页、过滤页面
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                //.antMatchers("/login", "/register","/failurl", "/error", "/Login", "/Register").permitAll()
                .antMatchers("/kaptcha/getKaptchaImage").permitAll()
                .anyRequest().authenticated();
        //对应表单认证相关的配置
        //返回一个FormLoginConfigurer 对象，
        //formLogin().x.x 就是配置使用内置的登录验证过滤器，默认实现为 UsernamePasswordAuthenticationFilter
        http.formLogin()
                // 用户未登录时，访问任何资源都转跳到该路径，即登录页面
                .loginPage("/login");
        http.addFilterAt(loginFilter(), UsernamePasswordAuthenticationFilter.class);
        //对应了注销相关的配置
        http.logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login");
        // 关闭CSRF跨域
        http.csrf().disable();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // 设置拦截忽略文件夹，可以对静态资源放行
        //web.ignoring().antMatchers("/css/**", "/js/**", "/icon/**", "/img/**","/favicon.ico");
    }

    @Bean
    public UserDetailsService getUserDetailsService() {
        return new CustomUserDetailsServiceImpl();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public LoginFilter loginFilter() throws Exception {
        LoginFilter loginFilter = new LoginFilter();
        loginFilter.setFilterProcessesUrl("/Login");
        loginFilter.setAuthenticationManager(authenticationManagerBean());
        loginFilter.setAuthenticationSuccessHandler(new SimpleUrlAuthenticationSuccessHandler("/"));
        loginFilter.setAuthenticationFailureHandler(new SimpleUrlAuthenticationFailureHandler("/failurl"));
        return loginFilter;
    }
}
