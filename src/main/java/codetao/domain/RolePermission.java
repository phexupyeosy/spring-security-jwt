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
    private String relation;  //用来标记该权限是属于私人的，还是角色的，用于OwnerPolicy检测

    @Getter
    @Setter
    private String method;   //对应资源的操作方式，只能是GET、POST、PUT、DELETE中的其中一个

    @Getter
    @Setter
    private String url;     //对应资源的操作url
}
