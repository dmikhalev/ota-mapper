package cs.vsu.otamapper.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import cs.vsu.otamapper.entity.Rule;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RuleDto {
    private Long id;
    private String regExp;
    private String paramName;
    private Integer code;
    private Integer priority;

    public RuleDto() {
    }

    public RuleDto(Long id, String regExp, String paramName, Integer code, Integer priority) {
        this.id = id;
        this.regExp = regExp;
        this.paramName = paramName;
        this.code = code;
        this.priority = priority;
    }

    public Rule toORule() {
        return new Rule(id, regExp, paramName, code, priority);
    }

    public static RuleDto fromRule(Rule r) {
        return new RuleDto(r.getId(), r.getRegExp(), r.getParamName(), r.getCode(), r.getPriority());
    }
}
