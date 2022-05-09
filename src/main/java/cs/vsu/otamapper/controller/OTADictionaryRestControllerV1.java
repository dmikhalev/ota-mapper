package cs.vsu.otamapper.controller;

import cs.vsu.otamapper.dto.OTADictionaryDto;
import cs.vsu.otamapper.dto.OTAParameterViewDto;
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
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @PostMapping(value = "/dictionary/save")
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

    @GetMapping(value = "/dictionary/all_without_params")
    public ResponseEntity<List<OTADictionaryDto>> getAllOTADictionaryNames() {
        User user = userService.findAuthorizedUser();
        if (user == null) {
            log.error("User is not found");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        List<OTADictionary> dictionaries = otaDictionaryService.findAllWithoutParameters(user.getId());
        List<OTADictionaryDto> otaDictionaryDtos = dictionaries.stream()
                .sorted(Comparator.comparingLong(OTADictionary::getId))
                .map(OTADictionaryDto::fromOTADictionary)
                .collect(Collectors.toList());
        return new ResponseEntity<>(otaDictionaryDtos, HttpStatus.OK);
    }

    @GetMapping(value = "/dictionary/parameters/{id}")
    public ResponseEntity<List<OTAParameterViewDto>> getById(@PathVariable long id) {
        OTADictionary dictionary = otaDictionaryService.findById(id);
        if (dictionary == null) {
            log.error("Dictionary is not found");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        List<OTAParameterViewDto> result = dictionary.getOtaParameters()
                .stream()
                .map(OTAParameterViewDto::fromOTAParameter)
                .collect(Collectors.toList());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(value = "/dictionary/rename")
    public ResponseEntity<?> renameOTADictionary(@RequestBody OTADictionaryDto otaDictionaryDto) {
        if (otaDictionaryDto == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        OTADictionary dictionary = otaDictionaryService.findById(otaDictionaryDto.getId());
        dictionary.setName(otaDictionaryDto.getName());
        otaDictionaryService.createOrUpdate(dictionary, false);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @PostMapping(value = "/dictionary/delete")
    public void deleteOtaDictionary(@RequestBody Long id) {
        if (id != null) {
            otaDictionaryService.delete(id);
        }
    }
}
