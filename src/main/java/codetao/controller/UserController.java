package codetao.controller;

import codetao.domain.User;
import codetao.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> list(){
        List<User> list = userService.findAll();
        return list;
    }

    @RequestMapping(value = "/{id}",method = RequestMethod.POST)
    public User get(@PathVariable Long id){
        User user = userService.get(id);
        return null;
    }

    @RequestMapping(method = RequestMethod.POST)
    public User save(@RequestBody Map<String, String> data){
        String username = data.get("username");
        String password = data.get("password");
        User user = null;
        if(!StringUtils.isEmpty(username) && !StringUtils.isEmpty(password)){
            user = new User();
            user.setUsername(username);
            user.setPassword(new BCryptPasswordEncoder().encode(password));
            user = userService.save(user);
        }
        return user;
    }
}
