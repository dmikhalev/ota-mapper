package cs.vsu.otamapper.component;

import cs.vsu.otamapper.entity.Organization;
import cs.vsu.otamapper.entity.Role;
import cs.vsu.otamapper.entity.User;
import cs.vsu.otamapper.repository.OrganizationRepository;
import cs.vsu.otamapper.repository.RoleRepository;
import cs.vsu.otamapper.repository.UserRepository;
import cs.vsu.otamapper.service.RuleService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CommandLineAppStartupRunner implements CommandLineRunner {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RuleService ruleService;
    private final OrganizationRepository organizationRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public CommandLineAppStartupRunner(UserRepository userRepository, RoleRepository roleRepository, RuleService ruleService, OrganizationRepository organizationRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.ruleService = ruleService;
        this.organizationRepository = organizationRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        createDefaultOrganization();
        createInitialRoles();
        createAdminUser();
    }

    private void createInitialRoles() {
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
    }

    private void createAdminUser() {
        User admin = userRepository.findByUsername("admin").orElse(null);
        if (admin == null) {
            admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            Role adminRole = roleRepository.findByName("ROLE_ADMIN").orElse(null);
            admin.setRole(adminRole);
            admin.setName("admin");
            Organization organization = organizationRepository.findById(1L).orElse(null);
            admin.setOrganization(organization);
            userRepository.save(admin);
        }
    }

    private void createDefaultOrganization() {
        Organization organization = organizationRepository.findById(1L).orElse(null);
        if (organization == null) {
            organization = new Organization();
            organization.setName("OTAMapper");
            organization = organizationRepository.save(organization);
            ruleService.createInitialRoomTypeRules(organization);
        }
    }
}