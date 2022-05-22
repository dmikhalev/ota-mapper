package cs.vsu.otamapper.controller;

import cs.vsu.otamapper.dto.OrganizationDto;
import cs.vsu.otamapper.entity.Organization;
import cs.vsu.otamapper.service.OrganizationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1")
public class OrganizationRestControllerV1 {

    private final OrganizationService organizationService;

    public OrganizationRestControllerV1(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @GetMapping(value = "/organization/all")
    public ResponseEntity<List<OrganizationDto>> getAll() {
        List<OrganizationDto> result = organizationService.findAll()
                .stream()
                .sorted(Comparator.comparing(Organization::getName))
                .map(OrganizationDto::fromOrganization)
                .collect(Collectors.toList());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/organization")
    public ResponseEntity<OrganizationDto> getOrganizationById(@RequestBody Long id) {
        Organization organization = organizationService.findById(id);
        if (organization == null) {
            log.error("Organization is not found");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        OrganizationDto result = OrganizationDto.fromOrganization(organization);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(value = "/admin/organization/create_or_update")
    public void createOrUpdateOrganization(@RequestBody OrganizationDto organizationDto) {
        if (organizationDto == null) {
            return;
        }
        Organization organization = organizationDto.toOrganization();
        organizationService.createOrUpdate(organization);
    }

    @PostMapping(value = "/admin/organization/delete")
    public void deleteOrganization(@RequestBody Long id) {
        if (id != null) {
            organizationService.delete(id);
        }
    }
}
