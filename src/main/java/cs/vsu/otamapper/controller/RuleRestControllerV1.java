package cs.vsu.otamapper.controller;

import cs.vsu.otamapper.dto.RegExpDto;
import cs.vsu.otamapper.dto.RuleDto;
import cs.vsu.otamapper.entity.Rule;
import cs.vsu.otamapper.entity.User;
import cs.vsu.otamapper.service.RuleService;
import cs.vsu.otamapper.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1")
public class RuleRestControllerV1 {
    private final RuleService ruleService;
    private final UserService userService;

    @Autowired
    public RuleRestControllerV1(RuleService ruleService, UserService userService) {
        this.ruleService = ruleService;
        this.userService = userService;
    }

    @GetMapping(value = "/rule")
    public ResponseEntity<RuleDto> getRuleById(@RequestBody Long id) {
        Rule rule = ruleService.findById(id);
        if (rule == null) {
            log.error("Rule is not found");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        User user = userService.findAuthorizedUser();
        if (user == null || !rule.getUser().getUsername().equals(user.getUsername())) {
            log.error("Rule is not found for current user");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        RuleDto result = RuleDto.fromRule(rule);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/rules")
    public ResponseEntity<List<RuleDto>> getAllRules() {
        User user = userService.findAuthorizedUser();
        if (user == null) {
            log.error("User is not found");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        List<Rule> rules = ruleService.findAllByOrganization(user.getOrganization().getId());
        if (rules == null) {
            log.error("Rules are not found");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        List<RuleDto> ruleDtos = rules.stream()
                .sorted(Comparator.comparing(Rule::getCode)
                        .thenComparing(Rule::getPriority))
                .map(RuleDto::fromRule)
                .collect(Collectors.toList());
        return new ResponseEntity<>(ruleDtos, HttpStatus.OK);
    }

    @GetMapping(value = "/rules_of_param/{param_name}")
    public ResponseEntity<List<RuleDto>> getRuleByParamName(@PathVariable String param_name) {
        User user = userService.findAuthorizedUser();
        if (user == null) {
            log.error("User is not found");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        List<Rule> rules = ruleService.findByParamNameAndOrganization(param_name, user.getOrganization().getName());
        if (rules == null) {
            log.error("Rules are not found");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        List<RuleDto> ruleDtos = rules.stream()
                .map(RuleDto::fromRule)
                .collect(Collectors.toList());
        return new ResponseEntity<>(ruleDtos, HttpStatus.OK);
    }

    @PostMapping(value = "/rule/create_or_update")
    public ResponseEntity<RuleDto> createOrUpdateRule(@RequestBody RuleDto ruleDto) {
        if (ruleDto == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        Rule rule = ruleDto.toRule();
        if (ruleDto.getId() != null) {
            rule.setId(ruleDto.getId());
        }
        User user = userService.findAuthorizedUser();
        if (user != null) {
            rule.setUser(user);
            rule.setOrganization(user.getOrganization());
        }
        RuleDto result = RuleDto.fromRule(ruleService.createOrUpdate(rule));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(value = "/rule/validate")
    public ResponseEntity<Boolean> validateRegExp(@RequestBody RegExpDto regExpDto) {
        if (regExpDto == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        String regExp = regExpDto.getRegExp();
        Boolean result = ruleService.validateRegExp(regExp);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @PostMapping(value = "/rule/delete")
    public void deleteRule(@RequestBody Long id) {
        if (id != null) {
            ruleService.delete(id);
        }
    }
}
