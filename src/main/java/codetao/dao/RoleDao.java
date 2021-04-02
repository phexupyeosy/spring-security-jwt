package codetao.dao;

import codetao.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleDao extends JpaRepository<Role, Long>{

    @Query(nativeQuery=true, value = "select * from t_role where code <> 'admin'")
    List<Role> findAll();

    @Query(nativeQuery=true, value = "select * from t_role where code <> 'admin' and code = ?1")
    Role findByCode(String code);

    @Query(nativeQuery=true, value = "select * from t_role where code <> 'admin' and name = ?1")
    Role findByName(String name);

    @Query(nativeQuery = true, value = "select r.* from t_role r inner join t_user_role ur on r.id = ur.role_id where ur.user_id=?1")
    List<Role> findByUserId(Long userId);
}
