package cs.vsu.otamapper.controller;

import cs.vsu.otamapper.dto.UserDto;
import cs.vsu.otamapper.entity.Role;
import cs.vsu.otamapper.entity.User;
import cs.vsu.otamapper.service.RoleService;
import cs.vsu.otamapper.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1")
public class UserRestControllerV1 {

    private final UserService userService;
    private final RoleService roleService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserRestControllerV1(UserService userService, RoleService roleService, BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping(value = "/admin/user")
    public ResponseEntity<UserDto> getUserById(@RequestBody Long id) {
        User user = userService.findById(id);
        if (user == null) {
            log.error("User is not found");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        UserDto result = UserDto.fromUser(user);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/admin/user/all")
    public ResponseEntity<List<UserDto>> getAllUsersOfOrganization() {
        User user = userService.findAuthorizedUser();
        if (user == null) {
            log.error("User is not found");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        List<User> users = userService.findAllByOrganization(user.getOrganization().getId());
        List<UserDto> result = users.stream()
                .sorted(Comparator.comparingLong(User::getId))
                .map(UserDto::fromUser)
                .collect(Collectors.toList());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(value = "/admin/user")
    public void createOrUpdateUser(@RequestBody UserDto userDto) {
        if (userDto == null) {
            return;
        }
        User user;
        if (userDto.getId() != null && userDto.getId() > 0) {
            user = userService.findById(userDto.getId());
            user.setName(userDto.getName());
            user.setUsername(userDto.getUsername());
            user.setPhone(userDto.getPhone());
            user.setEmail(userDto.getEmail());
            if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            }
        } else {
            User admin = userService.findAuthorizedUser();
            if (admin == null) {
                return;
            }
            user = userDto.toUser(passwordEncoder);
            Role role = roleService.findByName("ROLE_USER");
            user.setRole(role);
            user.setOrganization(admin.getOrganization());
        }
        userService.createOrUpdate(user);
    }

    @PostMapping(value = "/admin/user/delete")
    public void deleteUser(@RequestBody Long id) {
        if (id != null) {
            userService.delete(id);
        }
    }
}
