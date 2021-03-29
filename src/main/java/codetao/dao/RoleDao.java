package codetao.dao;

import codetao.domain.Role;
import org.hibernate.criterion.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleDao extends JpaRepository<Role, Long>{

    Role findByCode(String code);

    Role findByName(String name);
}
