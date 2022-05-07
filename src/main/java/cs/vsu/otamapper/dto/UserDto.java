package cs.vsu.otamapper.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import cs.vsu.otamapper.entity.User;
import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {
    private Long id;
    private String username;
    private String password;
    private String name;
    private String phone;
    private String email;
    private String role;

    public UserDto() {
    }

    public UserDto(Long id, String username, String password, String name, String phone, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    public User toUser(BCryptPasswordEncoder passwordEncoder) {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setName(name);
        user.setPhone(phone);
        user.setEmail(email);
        if (password != null && !password.isEmpty()) {
            user.setPassword(passwordEncoder.encode(password));
        }
        return user;
    }

    public static UserDto fromUser(User u) {
        return new UserDto(u.getId(), u.getUsername(), null, u.getName(), u.getPhone(), u.getEmail());
    }
}
