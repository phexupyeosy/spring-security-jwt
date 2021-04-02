package codetao.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "t_role")
public class Role {
    public final static String CODE_ADMIN = "admin";
    public final static String CODE_USER = "user";

    @Id
    @GeneratedValue
    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private String code = CODE_USER;

    @Getter
    @Setter
    private String name;
}
