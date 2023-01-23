package com.atlxc.VulnScan.config;

import com.atlxc.VulnScan.handler.MyAuthenticationFailureHandler;
import com.atlxc.VulnScan.product.service.impl.CustomUserDetailsServiceImpl;
import com.atlxc.VulnScan.xss.LoginFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;
import java.util.UUID;

/**
 * //Security配置类必须继承WebSecurityConfigurerAdapter，并且重写configure方法
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final String DEFAULT_REMEMBER_ME_KEY = UUID.randomUUID().toString();
    @Value("${myConfig.security.tokenValiditySeconds}")
    private int tokenValiditySeconds;
    @Autowired
    private CustomUserDetailsServiceImpl userDetailsService;


    @Autowired
    private DataSource dataSource;

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
        http.rememberMe()
                .tokenValiditySeconds(tokenValiditySeconds) //设置rememberMe失效时间
                .userDetailsService(userDetailsService)
                .tokenRepository(persistentTokenRepository())
                .rememberMeServices(myPersistentTokenBasedRememberMeServices());
        //对应了注销相关的配置
        http.logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login");
        // 关闭CSRF跨域
        http.csrf().disable();
    }

    private RememberMeServices myPersistentTokenBasedRememberMeServices() {
        return new MyPersistentTokenBasedRememberMeServices(DEFAULT_REMEMBER_ME_KEY, userDetailsService(), persistentTokenRepository());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // 设置拦截忽略文件夹，可以对静态资源放行
        //web.ignoring().antMatchers("/css/**", "/js/**", "/icon/**", "/img/**","/favicon.ico");
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(systemUserDetailsService).passwordEncoder(passwordEncoder());
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
        loginFilter.setFilterProcessesUrl("/doLogin");
        loginFilter.setAuthenticationManager(authenticationManagerBean());
        loginFilter.setAuthenticationSuccessHandler(new SimpleUrlAuthenticationSuccessHandler("/"));
        loginFilter.setAuthenticationFailureHandler(new MyAuthenticationFailureHandler());
        return loginFilter;
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository(){
        JdbcTokenRepositoryImpl jdbcTokenRepository=new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        //自动创建相关的token表(首次运行时需要打开，二次运行时需要注解掉)
        jdbcTokenRepository.setCreateTableOnStartup(true);
        return jdbcTokenRepository;
    }

}
