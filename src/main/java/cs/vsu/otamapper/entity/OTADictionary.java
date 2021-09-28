package cs.vsu.otamapper.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "ota_dictionaries")
public class OTADictionary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "creator", nullable = false)
    private String creator;

    @Column(name = "last_modified_date", nullable = false)
    private Date lastModifiedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @OneToMany(mappedBy = "otaDictionary")
    private List<OTAParameter> otaParameters;

    public OTADictionary() {

    }
}
