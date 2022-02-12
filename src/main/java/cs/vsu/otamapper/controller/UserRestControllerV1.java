package cs.vsu.otamapper.controller;

import cs.vsu.otamapper.dto.IdDto;
import cs.vsu.otamapper.dto.UserDto;
import cs.vsu.otamapper.entity.Role;
import cs.vsu.otamapper.entity.User;
import cs.vsu.otamapper.security.jwt.JwtUser;
import cs.vsu.otamapper.service.RoleService;
import cs.vsu.otamapper.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping(value = "/user")
    public ResponseEntity<UserDto> getUserById(@RequestBody IdDto id) {
        User user = userService.findById(id.getId());
        if (user == null) {
            log.error("User is not found");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        UserDto result = UserDto.fromUser(user);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/user/all")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<User> users = userService.findAll();
        List<UserDto> result = users.stream()
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
        if (userDto.getId() != null) {
            user = userService.findById(userDto.getId());
            user.setName(userDto.getName());
            user.setUsername(userDto.getUsername());
            user.setPhone(userDto.getPhone());
            user.setEmail(userDto.getEmail());
            if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            }
        } else {
            User admin = findAuthorizedUser();
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

    @DeleteMapping(value = "/admin/user")
    public void deleteUser(@RequestBody IdDto id) {
        userService.delete(id.getId());
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
