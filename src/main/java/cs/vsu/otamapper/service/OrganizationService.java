package cs.vsu.otamapper.service;

import cs.vsu.otamapper.entity.Organization;
import cs.vsu.otamapper.repository.OrganizationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final RuleService ruleService;

    public OrganizationService(OrganizationRepository organizationRepository, RuleService ruleService) {
        this.organizationRepository = organizationRepository;
        this.ruleService = ruleService;
    }

    public Organization findById(long id) {
        return organizationRepository.findById(id).orElse(null);
    }

    public List<Organization> findAll() {
        return organizationRepository.findAll();
    }

    public Organization findByName(String name) {
        return organizationRepository.findByName(name).orElse(null);
    }

    @Transactional
    public Organization createOrUpdate(Organization organization) {
        boolean shouldCreateRules = organization.getId() == null || organization.getId() <= 0;
        organization = organizationRepository.save(organization);
        if (shouldCreateRules) {
            ruleService.createInitialRoomTypeRules(organization);
        }
        return organization;
    }

    public void delete(Long id) {
        organizationRepository.deleteById(id);
    }
}
