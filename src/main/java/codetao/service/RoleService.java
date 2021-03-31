package codetao.service;

import codetao.dao.RolePermissionDao;
import codetao.domain.Role;
import codetao.dao.RoleDao;
import codetao.domain.RolePermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private RolePermissionDao rolePermissionDao;

    public List<Role> findAll(){
        List<Role> roles = roleDao.findAll();
        return roles;
    }

    public Role save(Role entity){
        return roleDao.save(entity);
    }

    public Role get(Long id){
        return roleDao.findOne(id);
    }

    public void delete(Role entity){
        roleDao.delete(entity);
    }

    public Boolean exists(String name){
        Role role = roleDao.findByName(name);
        return role != null;
    }

    public List<Role> findByUserId(Long userId){
        return roleDao.findByUserId(userId);
    }
}
