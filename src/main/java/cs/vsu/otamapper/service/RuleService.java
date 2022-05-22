package cs.vsu.otamapper.service;

import cs.vsu.otamapper.controller.mapper.OpenApiRestControllerV1;
import cs.vsu.otamapper.entity.Organization;
import cs.vsu.otamapper.entity.Rule;
import cs.vsu.otamapper.parser.Parser;
import cs.vsu.otamapper.parser.RegExp;
import cs.vsu.otamapper.repository.RuleRepository;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class RuleService {
    public static final String ROOM_TYPE_OTA_DICTIONARY_PATH = "src/main/resources/roomTypeOtaDictionary.csv";

    private final RuleRepository ruleRepository;

    public RuleService(RuleRepository ruleRepository) {
        this.ruleRepository = ruleRepository;
    }

    public Rule findById(long id) {
        return ruleRepository.findById(id).orElse(null);
    }

    public List<Rule> findByParamNameAndOrganization(String paramName, String organization) {
        if (paramName == null || organization == null) {
            return null;
        }
        return ruleRepository.findByParamNameAndOrganization_Name(paramName, organization);
    }

    public List<Rule> findAllByOrganization(long organizationId) {
        return ruleRepository.findByOrganization_Id(organizationId);
    }

    public List<Rule> findByNameAndOrganization(String name, String organization) {
        if (name == null || organization == null) {
            return null;
        }
        return ruleRepository.findByNameAndOrganization_Name(name, organization);
    }

    public Map<Long, Rule> findByOrganization(String organization) {
        if (organization == null) {
            return null;
        }
        List<Rule> rules = ruleRepository.findByOrganization_Name(organization);
        return rules.stream().collect(Collectors.toMap(Rule::getId, Function.identity()));
    }

    public Rule createOrUpdate(Rule rule) {
        return ruleRepository.save(rule);
    }

    public void delete(Long id) {
        ruleRepository.deleteById(id);
    }

    public boolean validateRegExp(String regExpStr) {
        RegExp regExp = Parser.parseRegExp(regExpStr);
        return regExp != null;
    }

    public void createInitialRoomTypeRules(Organization organization) {
        try (BufferedReader br = new BufferedReader(new FileReader(ROOM_TYPE_OTA_DICTIONARY_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] nameToCode = line.split(",");
                String paramName = nameToCode[0];
                int code = Integer.parseInt(nameToCode[1]);
                String regExp = String.format("IF '%s' THEN %d", paramName, code);
                Rule rule = new Rule(null, OpenApiRestControllerV1.ROOM_TYPE, "GRI", regExp, paramName, code, 1);
                rule.setOrganization(organization);
                ruleRepository.save(rule);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
