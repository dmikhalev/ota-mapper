package cs.vsu.otamapper.dto;

import cs.vsu.otamapper.entity.OTAParameter;
import cs.vsu.otamapper.entity.Rule;
import lombok.Data;

@Data
public class OTAParameterViewDto {
    private Long id;
    private String value;
    private String paramName;
    private String regExp;
    private Integer code;

    public OTAParameterViewDto() {
    }

    public OTAParameterViewDto(Long id, String value, String paramName, String regExp, Integer code) {
        this.id = id;
        this.value = value;
        this.paramName = paramName;
        this.regExp = regExp;
        this.code = code;
    }


    public static OTAParameterViewDto fromOTAParameter(OTAParameter parameter) {
        Rule rule = parameter.getRule();
        return new OTAParameterViewDto(
                parameter.getId(),
                parameter.getValue(),
                rule.getParamName(),
                rule.getRegExp(),
                rule.getCode());
    }
}
