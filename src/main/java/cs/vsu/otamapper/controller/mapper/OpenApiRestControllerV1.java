package cs.vsu.otamapper.controller.mapper;

import cs.vsu.otamapper.dto.DictionaryParameterDto;
import cs.vsu.otamapper.dto.ResultMappedParameterDto;
import cs.vsu.otamapper.entity.User;
import cs.vsu.otamapper.mapper.CsvMapper;
import cs.vsu.otamapper.mapper.MappedParameter;
import cs.vsu.otamapper.service.RuleService;
import cs.vsu.otamapper.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1")
public class OpenApiRestControllerV1 {
    private static final String ROOM_TYPE = "RoomType";

    private final UserService userService;
    private final RuleService ruleService;

    @Autowired
    public OpenApiRestControllerV1(UserService userService, RuleService ruleService) {
        this.userService = userService;
        this.ruleService = ruleService;
    }

    @GetMapping(value = "/openapi/room_type_ota_dictionary")
    public ResponseEntity<List<DictionaryParameterDto>> getRoomTypeOtaDictionary() {
        List<DictionaryParameterDto> result = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/roomTypeOtaDictionary.csv"));) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] nameToCode = line.split(",");
                result.add(new DictionaryParameterDto(nameToCode[0], Integer.parseInt(nameToCode[1])));
            }
        } catch (Exception e) {
            log.error("Failed to read room type OTA dictionary from file", e);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(value = "/openapi/map")
    public ResponseEntity<List<ResultMappedParameterDto>> map(@RequestBody String[] data) {
        if (data == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
//        if (ROOM_TYPE.equalsIgnoreCase(paramName)) {
//            paramName = ROOM_TYPE;
//        }
        User user = userService.findAuthorizedUser();
        if (user == null) {
            log.error("User is not found");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        CsvMapper csvMapper = new CsvMapper(ruleService, user, ROOM_TYPE);
        List<MappedParameter> mappedParameters = csvMapper.map(Arrays.asList(data));
        List<ResultMappedParameterDto> result = mappedParameters
                .stream()
                .map(ResultMappedParameterDto::fromMappedParameter)
                .collect(Collectors.toList());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
