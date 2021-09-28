package cs.vsu.otamapper.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "ota_parameter")
public class OTAParameter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "ota_type", nullable = false)
    private String otaType;

    @Column(name = "value", nullable = false)
    private String value;

    @Column(name = "code", nullable = false)
    private Integer code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dictionary_id", nullable = false)
    private OTADictionary otaDictionary;

    public OTAParameter() {
    }
}
