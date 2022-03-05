package cs.vsu.otamapper.controller;

import cs.vsu.otamapper.dto.IdDto;
import cs.vsu.otamapper.dto.RuleDto;
import cs.vsu.otamapper.entity.Rule;
import cs.vsu.otamapper.entity.User;
import cs.vsu.otamapper.security.jwt.JwtUser;
import cs.vsu.otamapper.service.RuleService;
import cs.vsu.otamapper.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<RuleDto> getRuleById(@RequestBody IdDto id) {
        Rule rule = ruleService.findById(id.getId());
        if (rule == null) {
            log.error("Rule is not found");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        User user = findAuthorizedUser();
        if (user == null || !rule.getUser().getUsername().equals(user.getUsername())) {
            log.error("Rule is not found for current user");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        RuleDto result = RuleDto.fromRule(rule);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/rules_of_param")
    public ResponseEntity<List<RuleDto>> getRuleByParamName(@RequestBody RuleDto ruleDto) {
        User user = findAuthorizedUser();
        if (user == null) {
            log.error("User is non found");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        List<Rule> rules = ruleService.findByParamNameAndOrganization(ruleDto.getParamName(), user.getOrganization().getName());
        if (rules == null) {
            log.error("Rules are not found");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        List<RuleDto> ruleDtos = rules.stream()
                .map(RuleDto::fromRule)
                .collect(Collectors.toList());
        return new ResponseEntity<>(ruleDtos, HttpStatus.OK);
    }

    private User findAuthorizedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            log.error("Authorized user not found.");
            return null;
        }
        if (auth.getPrincipal() instanceof JwtUser) {
            return userService.findById(((JwtUser) auth.getPrincipal()).getId());
        }
        return null;
    }
}
