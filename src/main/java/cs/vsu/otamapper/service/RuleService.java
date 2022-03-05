package cs.vsu.otamapper.service;

import cs.vsu.otamapper.entity.Rule;
import cs.vsu.otamapper.repository.RuleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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
        return ruleRepository.findByParamNameAndOrganization(paramName, organization);
    }

    public List<Rule> findByNameAndOrganization(String name, String organization) {
        if (name == null || organization == null) {
            return null;
        }
        return ruleRepository.findByNameAndOrganization(name, organization);
    }

    public Rule createOrUpdate(Rule rule) {
        return ruleRepository.save(rule);
    }

    public void delete(Long id) {
        ruleRepository.deleteById(id);
    }
}
