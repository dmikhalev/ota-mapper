package cs.vsu.otamapper.dto;

import cs.vsu.otamapper.entity.OTADictionary;
import cs.vsu.otamapper.entity.OTAParameter;
import cs.vsu.otamapper.entity.Rule;
import cs.vsu.otamapper.entity.User;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class OTADictionaryDto {
    private Long id;
    private String name;
    private List<OTAParameterDto> otaParameters;

    public OTADictionaryDto() {
    }

    public OTADictionaryDto(Long id, String name, List<OTAParameterDto> otaParameters) {
        this.id = id;
        this.name = name;
        this.otaParameters = otaParameters;
    }

    public OTADictionary toOTADictionary(Map<Long, Rule> idToRule, User user) {
        OTADictionary dictionary = new OTADictionary(id, name, user);
        List<OTAParameter> parameters = otaParameters.stream()
                .map(p -> p.toOTAParameter(idToRule, dictionary))
                .collect(Collectors.toList());
        dictionary.setOtaParameters(parameters);
        return dictionary;
    }

    public static OTADictionaryDto fromOTADictionary(OTADictionary p) {
        List<OTAParameterDto> parameters = null;
        if (p.getOtaParameters() != null) {
            parameters = p.getOtaParameters().stream()
                    .map(OTAParameterDto::fromOTAParameter)
                    .collect(Collectors.toList());
        }
        return new OTADictionaryDto(p.getId(), p.getName(), parameters);
    }
}
