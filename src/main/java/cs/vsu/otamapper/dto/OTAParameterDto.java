package cs.vsu.otamapper.dto;

import cs.vsu.otamapper.entity.OTADictionary;
import cs.vsu.otamapper.entity.OTAParameter;
import cs.vsu.otamapper.entity.Rule;
import lombok.Data;

import java.util.Map;

@Data
public class OTAParameterDto {
    private Long id;
    private String value;
    private String additionalDetails;
    private Long ruleId;

    public OTAParameterDto() {
    }

    public OTAParameterDto(Long id, String value, String additionalDetails, Long ruleId) {
        this.id = id;
        this.value = value;
        this.additionalDetails = additionalDetails;
        this.ruleId = ruleId;
    }

    public OTAParameter toOTAParameter(Map<Long, Rule> idToRule, OTADictionary dictionary) {
        Rule rule = null;
        if (ruleId != null) {
            rule = idToRule.get(ruleId);
        }
        return new OTAParameter(id, value, additionalDetails, rule, dictionary);
    }

    public static OTAParameterDto fromOTAParameter(OTAParameter p) {
        return new OTAParameterDto(p.getId(), p.getValue(), p.getAdditionalDetails(), p.getRule().getId());
    }
}
