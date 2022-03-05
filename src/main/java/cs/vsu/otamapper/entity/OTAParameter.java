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

    @Column(name = "value", nullable = false)
    private String value;

    @Column(name = "additional_details", nullable = false)
    private String additionalDetails;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rule_id", nullable = false)
    private Rule rule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dictionary_id", nullable = false)
    private OTADictionary otaDictionary;

    public OTAParameter() {
    }
}
