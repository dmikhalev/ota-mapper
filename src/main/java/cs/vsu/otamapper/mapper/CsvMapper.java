package cs.vsu.otamapper.mapper;

import cs.vsu.otamapper.entity.Rule;
import cs.vsu.otamapper.entity.User;
import cs.vsu.otamapper.service.RuleService;
import cs.vsu.otamapper.utils.StringUtils;

import java.util.*;

public class CsvMapper implements OTAMapper {

    public static final String CSV_SEPARATOR = ",";

    private RuleService ruleService;
    private User user;
    private String paramName;

    public CsvMapper(RuleService ruleService, User user, String paramName) {
        this.ruleService = ruleService;
        this.user = user;
        this.paramName = paramName;
    }

    @Override
    public List<MappedParameter> map(List<String> data) {
        // all parameter rules
        List<Rule> rules = ruleService.findByNameAndOrganization(paramName, user.getOrganization().getName());

        // cache: value to mapped rules
        Map<String, Set<Rule>> valueToRulesCache = new HashMap<>();

        int columnIndex = getCsvColumnIndex(data.get(0), CSV_SEPARATOR);
        if (columnIndex < 0) {
            return null;
        }
        List<MappedParameter> mappedParameters = new ArrayList<>();
        for (int i = 1; i < data.size(); i++) {
            String row = data.get(i);
            String value = row.split(CSV_SEPARATOR)[columnIndex];
            value = StringUtils.unescape(value);
            if (valueToRulesCache.get(value) != null) {
                MappedParameter mp = new MappedParameter(value, row, valueToRulesCache.get(value));
                mappedParameters.add(mp);
                continue;
            }
            MappedParameter mp = new MappedParameter();
            mp.setValue(value);
            mp.setAdditionalDetails(row);
            for (Rule rule : rules) {
                if (Matcher.matches(value, rule.getRegExp())) {
                    mp.addRule(rule);
                    // init cache
                    valueToRulesCache.computeIfAbsent(value, k -> new HashSet<>());
                    valueToRulesCache.get(value).add(rule);
                }
            }
            mappedParameters.add(mp);
        }
        return mappedParameters;
    }

    private int getCsvColumnIndex(String headers, String separator) {
        String[] titles = headers.toLowerCase().split(separator);
        // equals
        for (int i = 0; i < titles.length; i++) {
            if (paramName.equalsIgnoreCase(titles[i].replace("\"", ""))) {
                return i;
            }
        }
        // + name
        for (int i = 0; i < titles.length; i++) {
            if (titles[i].replace("\"", "").equalsIgnoreCase(paramName + "name")) {
                return i;
            }
        }
        // startsWith
        for (int i = 0; i < titles.length; i++) {
            if (titles[i].replace("\"", "").startsWith(paramName.toLowerCase())) {
                return i;
            }
        }
        // contains
        for (int i = 0; i < titles.length; i++) {
            if (titles[i].replace("\"", "").contains(paramName.toLowerCase())) {
                return i;
            }
        }
        return -1;
    }
}
