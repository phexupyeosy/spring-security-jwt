package codetao.dao;

import codetao.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleDao extends JpaRepository<Role, Long>{

    Role findByCode(String code);

    Role findByName(String name);

    @Query(nativeQuery = true, value = "select r.* from t_role r inner join t_user_role ur on r.id = ur.role_id inner join t_user u on u.id = ur.user_id where u.username=?1")
    List<Role> findByUser(String username);
}
