package cs.vsu.otamapper.dto;

import cs.vsu.otamapper.entity.Rule;
import cs.vsu.otamapper.mapper.MappedParameter;
import lombok.Data;

import java.util.Set;
import java.util.stream.Collectors;

@Data
public class MappedParameterDto {
    private String value;
    private String additionalDetails;
    private Set<RuleDto> rules;

    public MappedParameterDto() {
    }

    public MappedParameterDto(String value, String additionalDetails, Set<RuleDto> rules) {
        this.value = value;
        this.additionalDetails = additionalDetails;
        this.rules = rules;
    }

    public MappedParameter toMappedParameter() {
        Set<Rule> rules = this.rules.stream().map(RuleDto::toRule).collect(Collectors.toSet());
        return new MappedParameter(value, additionalDetails, rules);
    }

    public static MappedParameterDto fromMappedParameter(MappedParameter mp) {
        Set<RuleDto> ruleDtos = mp.getRules().stream().map(RuleDto::fromRule).collect(Collectors.toSet());
        return new MappedParameterDto(mp.getValue(), mp.getAdditionalDetails(), ruleDtos);
    }
}
