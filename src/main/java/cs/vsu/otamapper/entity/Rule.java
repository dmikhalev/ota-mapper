package cs.vsu.otamapper.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "rules")
public class Rule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "ota_type", nullable = false)
    private String otaType;

    @Column(name = "reg_exp", nullable = false)
    private String regExp;

    @Column(name = "param_name", nullable = false)
    private String paramName;

    @Column(name = "code", nullable = false)
    private Integer code;

    @Column(name = "priority", nullable = false)
    private Integer priority;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

    public Rule() {
    }

    public Rule(Long id, String name, String otaType, String regExp, String paramName, Integer code, Integer priority) {
        this.id = id;
        this.name = name;
        this.otaType = otaType;
        this.regExp = regExp;
        this.paramName = paramName;
        this.code = code;
        this.priority = priority;
    }
}
