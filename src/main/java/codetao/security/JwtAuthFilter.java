package codetao.security;

import codetao.domain.Role;
import codetao.domain.User;
import codetao.service.RoleService;
import codetao.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JwtAuthFilter extends BasicAuthenticationFilter {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    private ObjectMapper om = new ObjectMapper();

    public JwtAuthFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String username = JwtProvider.getAuthentication(request);
        Authentication authentication = null;
        if(!StringUtils.isEmpty(username)){
            User user = userService.findByUsername(username);
            if(user != null){
                List<Role> roles = roleService.findByUserId(user.getId());
                List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                for(Role role : roles){
                    authorities.add(new SimpleGrantedAuthority(om.writeValueAsString(role)));
                }
                authentication = new UsernamePasswordAuthenticationToken(username, user.getPassword(), authorities);
            }
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }
}
