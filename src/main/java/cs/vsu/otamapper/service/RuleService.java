package cs.vsu.otamapper.service;

import cs.vsu.otamapper.entity.Rule;
import cs.vsu.otamapper.repository.RuleRepository;
import org.springframework.stereotype.Service;

@Service
public class RuleService {

    private final RuleRepository ruleRepository;

    public RuleService(RuleRepository ruleRepository) {
        this.ruleRepository = ruleRepository;
    }

    public Rule createOrUpdate(Rule rule) {
        return ruleRepository.save(rule);
    }

    public void delete(Long id) {
        ruleRepository.deleteById(id);
    }
}
