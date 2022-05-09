package cs.vsu.otamapper.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "ota_dictionaries")
public class OTADictionary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

    @OneToMany(mappedBy = "otaDictionary")
    private List<OTAParameter> otaParameters;

    public OTADictionary() {

    }

    public OTADictionary(Long id, String name, User user) {
        this.id = id;
        this.name = name;
        this.user = user;
    }

    public OTADictionary(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
