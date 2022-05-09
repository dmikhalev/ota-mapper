package cs.vsu.otamapper.service;

import cs.vsu.otamapper.entity.Rule;
import cs.vsu.otamapper.parser.Parser;
import cs.vsu.otamapper.parser.RegExp;
import cs.vsu.otamapper.repository.RuleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class RuleService {

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
}
