package cs.vsu.otamapper.component;

import cs.vsu.otamapper.entity.Role;
import cs.vsu.otamapper.entity.User;
import cs.vsu.otamapper.repository.RoleRepository;
import cs.vsu.otamapper.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CommandLineAppStartupRunner implements CommandLineRunner {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public CommandLineAppStartupRunner(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) {
        Role role = roleRepository.findByName("ROLE_ADMIN").orElse(null);
        if (role == null) {
            role = new Role();
            role.setName("ROLE_ADMIN");
            roleRepository.save(role);
        }

        role = roleRepository.findByName("ROLE_USER").orElse(null);
        if (role == null) {
            role = new Role();
            role.setName("ROLE_USER");
            roleRepository.save(role);
        }

        User admin = userRepository.findByUsername("admin").orElse(null);
        if (admin == null) {
            admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            Role adminRole = roleRepository.findByName("ROLE_ADMIN").orElse(null);
            admin.setRole(adminRole);
            admin.setName("admin");
            userRepository.save(admin);
        }
    }
}