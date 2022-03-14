package cs.vsu.otamapper.repository;

import cs.vsu.otamapper.entity.Rule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RuleRepository extends JpaRepository<Rule, Long> {

    List<Rule> findByParamNameAndOrganization_Name(String paramName, String organizationName);

    List<Rule> findByNameAndOrganization_Name(String name, String organizationName);

    List<Rule> findByOrganization_Name(String organizationName);
}
