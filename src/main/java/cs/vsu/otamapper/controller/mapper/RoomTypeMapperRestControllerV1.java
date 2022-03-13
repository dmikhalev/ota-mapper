package cs.vsu.otamapper.controller.mapper;

import cs.vsu.otamapper.dto.CsvDataDto;
import cs.vsu.otamapper.dto.MappedParameterDto;
import cs.vsu.otamapper.entity.User;
import cs.vsu.otamapper.mapper.CsvMapper;
import cs.vsu.otamapper.mapper.MappedParameter;
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

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1")
public class RoomTypeMapperRestControllerV1 {

    private static final String ROOM_TYPE = "RoomType";

    private final UserService userService;
    private final RuleService ruleService;

    @Autowired
    public RoomTypeMapperRestControllerV1(UserService userService, RuleService ruleService) {
        this.userService = userService;
        this.ruleService = ruleService;
    }

    @PostMapping(value = "/map/room_type")
    public ResponseEntity<List<MappedParameterDto>> mapRoomType(@RequestBody CsvDataDto data) {
        if (data == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        User user = userService.findAuthorizedUser();
        if (user == null) {
            log.error("User is non found");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        CsvMapper csvMapper = new CsvMapper(ruleService, user, ROOM_TYPE);
        List<MappedParameter> mappedParameters = csvMapper.map(data.getData());
        List<MappedParameterDto> result = mappedParameters
                .stream()
                .map(MappedParameterDto::fromMappedParameter)
                .collect(Collectors.toList());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
