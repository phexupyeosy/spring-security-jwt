package codetao.security;

import codetao.domain.Role;
import codetao.domain.RolePermission;
import codetao.service.PermissionService;
import codetao.service.RolePermissionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class UrlAccessDecisionManager implements AccessDecisionManager {
    @Autowired
    private RolePermissionService rolePermissionService;

    @Autowired
    private PermissionService permissionService;

    private ObjectMapper om = new ObjectMapper();

    /**
     * 判断url是否拥有权限的决策方法
     * @param authentication    包含用户认证信息
     * @param obj               包含客户端请求的request信息，可转换为HttpServletRequest
     * @param configAttributes  包含从FilterInvocationSecurityMetadataSource.getAttributes方法返回的结果集
     */
    @Override
    public void decide(Authentication authentication, Object obj, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
        System.out.println("decide");
        HttpServletRequest request = ((FilterInvocation)obj).getHttpRequest();
        List<Long> roleIds = new ArrayList<>();
        try{
            for(GrantedAuthority authority : authentication.getAuthorities()){
                Role role = om.readValue(authority.getAuthority(), Role.class);
                roleIds.add(role.getId());
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        if(roleIds.size() > 0){
            List<Map<String, String>> apiPermissions = permissionService.getApiPermissions();
            List<RolePermission> rolePermissions = rolePermissionService.findAllByRoleIds(roleIds);
            for(RolePermission rp : rolePermissions){
                apiPermissions = apiPermissions.stream().filter(api-> rp.getPermission().equals(api.get("permission")) && api.get("method").contains(request.getMethod())).collect(Collectors.toList());
                for(Map<String, String> permissionMeta : apiPermissions){
                    if(new RegexRequestMatcher(permissionMeta.get("api"), request.getMethod()).matches(request)){
                        return;
                    }
                }
            }
        }
        throw new AccessDeniedException("access denied");
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
