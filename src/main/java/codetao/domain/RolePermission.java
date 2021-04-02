package codetao.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="t_role_permission")
public class RolePermission {
    @Id
    @GeneratedValue
    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private Long roleId;

    @Getter
    @Setter
    private String api;     //对应资源的操作url，例如：GET:/api/<resources>/<id>
}
