package codetao.service;

import codetao.dao.RolePermissionDao;
import codetao.domain.RolePermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RolePermissionService {

    @Autowired
    private RolePermissionDao rolePermissionDao;

    public List<RolePermission> findAllByRoleIds(List<Long> roleIds){
        return rolePermissionDao.findAllByRoleIdIn(roleIds);
    }
}
