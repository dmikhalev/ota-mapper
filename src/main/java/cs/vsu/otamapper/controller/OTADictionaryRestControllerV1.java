package cs.vsu.otamapper.controller;

import cs.vsu.otamapper.dto.OTADictionaryDto;
import cs.vsu.otamapper.entity.OTADictionary;
import cs.vsu.otamapper.entity.Rule;
import cs.vsu.otamapper.entity.User;
import cs.vsu.otamapper.service.OTADictionaryService;
import cs.vsu.otamapper.service.RuleService;
import cs.vsu.otamapper.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1")
public class OTADictionaryRestControllerV1 {

    private final OTADictionaryService otaDictionaryService;
    private final UserService userService;
    private final RuleService ruleService;

    @Autowired
    public OTADictionaryRestControllerV1(OTADictionaryService otaDictionaryService, UserService userService, RuleService ruleService) {
        this.otaDictionaryService = otaDictionaryService;
        this.userService = userService;
        this.ruleService = ruleService;
    }

    @PostMapping(value = "/map/dictionary/save")
    public ResponseEntity<?> saveOTADictionary(@RequestBody OTADictionaryDto otaDictionaryDto) {
        if (otaDictionaryDto == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        User user = userService.findAuthorizedUser();
        if (user == null) {
            log.error("User is not found");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        Map<Long, Rule> idToRule = ruleService.findByOrganization(user.getOrganization().getName());
        if (idToRule == null || idToRule.isEmpty()) {
            log.error("Rules are not found");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        OTADictionary dictionary = otaDictionaryDto.toOTADictionary(idToRule, user);
        otaDictionaryService.createOrUpdate(dictionary, true);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }
}
