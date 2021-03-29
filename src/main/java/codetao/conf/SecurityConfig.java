package codetao.conf;

import codetao.service.UserService;
import codetao.security.JwtAuthFilter;
import codetao.security.JwtLoginFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{

    @Autowired
    private UserService userService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().authorizeRequests()
                .antMatchers("/ping").permitAll()
                .antMatchers("/login.html").permitAll()
                .antMatchers("/index.html").permitAll()
                .anyRequest().authenticated()
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
}