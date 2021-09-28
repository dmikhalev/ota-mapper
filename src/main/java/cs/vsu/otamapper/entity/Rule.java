package cs.vsu.otamapper.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "rules")
public class Rule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    public Rule() {
    }
}
