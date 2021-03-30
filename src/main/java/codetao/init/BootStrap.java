package codetao.init;

import codetao.domain.Role;
import codetao.domain.User;
import codetao.domain.UserRole;
import codetao.dao.RoleDao;
import codetao.dao.UserDao;
import codetao.dao.UserRoleDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 服务启动引导
 * 优先级：1>2
 */
@Component
@Slf4j
@Order(value = 3)
public class BootStrap implements CommandLineRunner{
    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private UserRoleDao userRoleDao;

    @Override
    public void run(String... args) throws Exception {
        log.info(">>>>>>>>>>>>>>> BootStrap begin <<<<<<<<<<<<<");

        User user = userDao.findByUsername("demo");
        if(user == null){
            user = new User();
            user.setUsername("demo");
            user.setPassword(new BCryptPasswordEncoder().encode("demo"));
            userDao.save(user);
        }

        Role role = roleDao.findByCode("admin");
        if(role == null){
            role = new Role();
            role.setCode("admin");
            role.setName("管理员");
            roleDao.save(role);
        }

        UserRole userRole = userRoleDao.findByUserIdAndRoleId(user.getId(), role.getId());
        if(userRole == null){
            userRole = new UserRole();
            userRole.setUserId(user.getId());
            userRole.setRoleId(role.getId());
            userRoleDao.save(userRole);
        }

        log.info(">>>>>>>>>>>>>>> BootStrap end <<<<<<<<<<<<<");
    }

}
