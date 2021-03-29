package codetao.service;

import codetao.domain.User;
import codetao.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.findByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException("user not found by username");
        }
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.emptyList()
        );
    }

    public User findByUsername(String username){
        return userDao.findByUsername(username);
    }

    public List<User> findAll() {
        List<User> list = userDao.findAll();
        return list;
    }

    public User save(User user) {
        return userDao.save(user);
    }

    public User get(Long id) {
        return userDao.getOne(id);
    }
}