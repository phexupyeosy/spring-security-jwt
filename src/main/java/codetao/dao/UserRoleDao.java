package codetao.dao;

import codetao.domain.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleDao extends JpaRepository<UserRole, Long>{

    UserRole findByUserIdAndRoleId(Long userId, Long roleId);
}
