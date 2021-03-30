package codetao.conf;

import codetao.service.UserService;
import codetao.security.JwtAuthFilter;
import codetao.security.JwtLoginFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.access.SecurityConfig;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{

    @Autowired
    private UserService userService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        HttpSecurity httpSecurity = http.cors().and().csrf().disable().authorizeRequests()
                .antMatchers("/ping").permitAll()
                .antMatchers("/login.html").permitAll()
                .antMatchers("/index.html").permitAll()
                .anyRequest().authenticated()
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>(){
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O fsi) {
                        fsi.setSecurityMetadataSource(newFilterSecurityInterceptor());
                        fsi.setAccessDecisionManager(newAccessDecisionManager());
                        return fsi;
                    }
                })
                .and()
                // filter the /login requests
                .addFilter(newJwtLoginFilter(authenticationManager()))
                // filter other requests to check the presence of jwt in header
                .addFilter(newJwtAuthFilter(authenticationManager()));

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
    public FilterInvocationSecurityMetadataSource newFilterSecurityInterceptor(){
        return new FilterInvocationSecurityMetadataSource(){
            @Override
            public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {

                System.out.println("getAttributes");

                Collection<ConfigAttribute> list = new ArrayList<>();
                list.add(new SecurityConfig("all"));

                return list;
            }

            @Override
            public Collection<ConfigAttribute> getAllConfigAttributes() {
                System.out.println("getAllConfigAttributes");
                return Collections.emptyList();
            }

            @Override
            public boolean supports(Class<?> aClass) {
                return true;
            }
        };
    }

    @Bean
    public AccessDecisionManager newAccessDecisionManager(){
        return new AccessDecisionManager(){

            @Override
            public void decide(Authentication authentication, Object o, Collection<ConfigAttribute> collection) throws AccessDeniedException, InsufficientAuthenticationException {
                System.out.println("decide");
                System.out.println(authentication);

                for(GrantedAuthority authoritie : authentication.getAuthorities()){
                    System.out.println(authoritie.getAuthority());
                }

                System.out.println(authentication.getAuthorities());

                System.out.println(o);
                System.out.println(collection);
            }

            @Override
            public boolean supports(ConfigAttribute configAttribute) {
                return true;
            }

            @Override
            public boolean supports(Class<?> aClass) {
                return true;
            }
        };
    }
}