package codetao.web.controller;

import codetao.domain.Role;
import codetao.service.PermissionService;
import codetao.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import codetao.exception.NotFoundException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping
    public List<Role> list(){
        return roleService.findAll();
    }

    @GetMapping(value = "/{id}")
    public Role get(@PathVariable Long id){
        Role role = roleService.get(id);
        return role;
    }

    @PostMapping
    public Role save(@RequestBody Map<String,String> data) throws Exception{
        String name = data.get("name");
        if(StringUtils.isEmpty(name)){
            throw new NotFoundException("name is required");
        }
        Boolean exist = roleService.exists(name);
        if(exist){
            throw new NotFoundException("name aready exist");
        }
        Role role = new Role();
        role.setName(name);
        role = roleService.save(role);
        return role;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        Role role = roleService.get(id);
        if(role != null){
            roleService.delete(role);
        }
    }

    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @RequestBody Map<String,String> data) throws Exception{
        Role role = roleService.get(id);
        if(role == null){
            throw new NotFoundException("role not found by id");
        }
        String name = data.get("name");
        if(StringUtils.isEmpty(name)){
            throw new NotFoundException("name is required");
        }
        if(!role.getName().equals(name) && roleService.exists(name)){
            throw new NotFoundException("name aready exist");
        }
        role.setName(name);
        roleService.save(role);
    }
}
