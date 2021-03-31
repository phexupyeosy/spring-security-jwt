package codetao.dao;

import codetao.domain.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RolePermissionDao extends JpaRepository<RolePermission, Long> {

    List<RolePermission> findAllByRoleIdIn(List<Long> roleIds);

}
