package codetao.conf;

import codetao.security.UrlAccessDecisionManager;
import codetao.service.UserService;
import codetao.security.JwtAuthFilter;
import codetao.security.JwtLoginFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.access.SecurityConfig;

import java.util.ArrayList;
import java.util.Collection;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{

    @Autowired
    private UserService userService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.anonymous().disable().cors().and().csrf().disable().authorizeRequests()
                .antMatchers("/ping").permitAll()
                .antMatchers("/login.html").permitAll()
                .antMatchers("/index.html").permitAll()
                .anyRequest().authenticated()
                .and()
                // filter the /login requests
                .addFilter(newJwtLoginFilter(authenticationManager()))
                // filter other requests to check the presence of jwt in header
                .addFilter(newJwtAuthFilter(authenticationManager()))
                // filter request url and method decide
                .addFilter(newFilterSecurityInterceptor());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtLoginFilter newJwtLoginFilter(AuthenticationManager authenticationManager){
        return new JwtLoginFilter(authenticationManager);
    }

    @Bean
    public JwtAuthFilter newJwtAuthFilter(AuthenticationManager authenticationManager){
        return new JwtAuthFilter(authenticationManager);
    }


    @Bean
    public FilterSecurityInterceptor newFilterSecurityInterceptor(){
        FilterSecurityInterceptor fsi = new FilterSecurityInterceptor();
        fsi.setSecurityMetadataSource(newFilterInvocationSecurityMetadataSource());
        fsi.setAccessDecisionManager(newAccessDecisionManager());
        return fsi;
    }

    @Bean
    public FilterInvocationSecurityMetadataSource newFilterInvocationSecurityMetadataSource(){
        return new FilterInvocationSecurityMetadataSource(){
            /**
             * 此方法可用来校验客户端请求的url是否在权限表中
             * 如果存在返回结果给AccessDecisionManager.decide()方法决策是否由此权限，如果不存在则放行
             *
             *
             * 注：因不想每次请求都要去匹配权限表中是否包含请求的url，所以此处不管任何请求url都拦截，然后通过decide方法中做决策。
             */
            @Override
            public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {
                Collection<ConfigAttribute> list = new ArrayList<>();
                list.add(new SecurityConfig("all"));
                return list;
            }

            @Override
            public Collection<ConfigAttribute> getAllConfigAttributes() {
                return null;
            }

            @Override
            public boolean supports(Class<?> aClass) {
                return true;
            }
        };
    }

    @Bean
    public AccessDecisionManager newAccessDecisionManager(){
        return new UrlAccessDecisionManager();
    }
}