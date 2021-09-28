package cs.vsu.otamapper.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "organizations")
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "email", nullable = false)
    private String email;

    @OneToMany(mappedBy = "organization")
    private List<User> users;

    @OneToMany(mappedBy = "organization")
    private List<Rule> rules;

    @OneToMany(mappedBy = "organization")
    private List<OTADictionary> dictionaries;

    public Organization() {

    }
}