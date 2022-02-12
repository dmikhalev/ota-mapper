package cs.vsu.otamapper.controller;

import cs.vsu.otamapper.dto.IdDto;
import cs.vsu.otamapper.dto.OrganizationDto;
import cs.vsu.otamapper.entity.Organization;
import cs.vsu.otamapper.service.OrganizationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1")
public class OrganizationRestControllerV1 {

    private final OrganizationService organizationService;

    @Autowired
    public OrganizationRestControllerV1(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @GetMapping(value = "/organization")
    public ResponseEntity<OrganizationDto> getOrganizationById(@RequestBody IdDto id) {
        Organization organization = organizationService.findById(id.getId());
        if (organization == null) {
            log.error("Organization is not found");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        OrganizationDto result = OrganizationDto.fromOrganization(organization);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(value = "/admin/organization")
    public void createOrUpdateOrganization(@RequestBody OrganizationDto organizationDto) {
        if (organizationDto == null) {
            return;
        }
        Organization organization;
        if (organizationDto.getId() != null) {
            organization = organizationService.findById(organizationDto.getId());
            organization.setName(organizationDto.getName());
            organization.setEmail(organizationDto.getEmail());
            organization.setPhone(organizationDto.getPhone());
        } else {
            organization = organizationDto.toOrganization();
        }
        organizationService.createOrUpdate(organization);

    }

    @DeleteMapping(value = "/admin/organization")
    public void deleteOrganization(@RequestBody IdDto id) {
        organizationService.delete(id.getId());
    }
}
