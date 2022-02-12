package cs.vsu.otamapper.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import cs.vsu.otamapper.entity.Organization;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrganizationDto {
    private Long id;
    private String name;
    private String phone;
    private String email;

    public OrganizationDto() {
    }

    public OrganizationDto(Long id, String name, String phone, String email) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    public Organization toOrganization() {
        return new Organization(id, name, phone, email);
    }

    public static OrganizationDto fromOrganization(Organization o) {
        return new OrganizationDto(o.getId(), o.getName(), o.getPhone(), o.getEmail());
    }
}
