package cs.vsu.otamapper.dto;

import cs.vsu.otamapper.entity.Rule;
import cs.vsu.otamapper.mapper.MappedParameter;
import lombok.Data;

@Data
public class ResultMappedParameterDto {

    private String value;
    private int code;

    public ResultMappedParameterDto() {

    }

    public ResultMappedParameterDto(String value, int code) {
        this.value = value;
        this.code = code;
    }

    public static ResultMappedParameterDto fromMappedParameter(MappedParameter mp) {
        Rule rule = mp.getRules().iterator().next();
        return new ResultMappedParameterDto(mp.getValue(), rule.getCode());
    }
}
