package codetao.security;

import codetao.domain.RolePermission;
import codetao.service.RolePermissionService;
import codetao.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UrlAccessDecisionManager implements AccessDecisionManager {
    @Autowired
    private RolePermissionService rolePermissionService;

    /**
     * 判断url是否拥有权限的决策方法
     * @param authentication    包含用户认证信息
     * @param obj               包含客户端请求的request信息，可转换为HttpServletRequest
     * @param configAttributes  包含从FilterInvocationSecurityMetadataSource.getAttributes方法返回的结果集
     */
    @Override
    public void decide(Authentication authentication, Object obj, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
        System.out.println(">>>>>decide<<<<<");
        HttpServletRequest request = ((FilterInvocation)obj).getHttpRequest();
        System.out.println("requestURI="+request.getRequestURI());
        System.out.println("method="+request.getMethod());

        List<Long> roleIds = new ArrayList<>();
        for(GrantedAuthority authority : authentication.getAuthorities()){
            roleIds.add(Long.parseLong(authority.getAuthority()));
        }
        if(roleIds.size() > 0){
            List<RolePermission> rps = rolePermissionService.findAllByRoleIds(roleIds);
            for(RolePermission rp : rps){

            }
        }
    }

    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
