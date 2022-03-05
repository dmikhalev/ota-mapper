package cs.vsu.otamapper.mapper;

import cs.vsu.otamapper.entity.Rule;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class MappedParameter {

    private String value;
    private String additionalDetails;
    private Set<Rule> rules = new HashSet<>();

    public MappedParameter() {
    }

    public MappedParameter(String value, String additionalDetails, Set<Rule> rules) {
        this.value = value;
        this.additionalDetails = additionalDetails;
        this.rules = rules;
    }

    public void addRule(Rule rule) {
        if (rule != null) {
            rules.add(rule);
        }
    }
}
