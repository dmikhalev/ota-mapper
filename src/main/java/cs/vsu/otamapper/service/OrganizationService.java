package cs.vsu.otamapper.service;

import cs.vsu.otamapper.entity.Organization;
import cs.vsu.otamapper.repository.OrganizationRepository;
import org.springframework.stereotype.Service;

@Service
public class OrganizationService {

    private final OrganizationRepository organizationRepository;

    public OrganizationService(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    public Organization findById(long id) {
        return organizationRepository.findById(id).orElse(null);
    }

    public Organization createOrUpdate(Organization organization) {
        return organizationRepository.save(organization);
    }

    public void delete(Long id) {
        organizationRepository.deleteById(id);
    }
}
