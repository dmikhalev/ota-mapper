package cs.vsu.otamapper.dto;

import lombok.Data;

@Data
public class DictionaryParameterDto {

    private String paramName;
    private int code;

    public DictionaryParameterDto() {

    }

    public DictionaryParameterDto(String paramName, int code) {
        this.paramName = paramName;
        this.code = code;
    }
}
