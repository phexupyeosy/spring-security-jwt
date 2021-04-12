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
            List<RolePermission> permissions = rolePermissionService.findAllByRoleIds(roleIds);
            for(RolePermission rp : permissions){
                System.out.println(rp);
            }
            List<?> apis = permissionService.getApiPermissions();
            Iterator<?> ite = apis.iterator();
            while(ite.hasNext()){
                Map map = (Map)ite.next();
                System.out.println(map.get("api"));
                System.out.println(map.get("method"));
                System.out.println(map.get("permissionKey"));
            }

            /**
            for(RolePermission p : permissions){
                String[] strs = p.getApi().split(",");
                for(String str : strs){
                    String[] apis = str.split(":");
                    if(apis.length == 2 && new RegexRequestMatcher(apis[1], apis[0]).matches(request)){
                        return;
                    }
                }
            }
            */
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
