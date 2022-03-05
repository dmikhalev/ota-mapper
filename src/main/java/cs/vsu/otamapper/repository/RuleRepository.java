package cs.vsu.otamapper.repository;

import cs.vsu.otamapper.entity.Rule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RuleRepository extends JpaRepository<Rule, Long> {

    @Query(value = "SELECT r FROM Rule r WHERE r.organization.name=:organization AND r.paramName=:paramName")
    List<Rule> findByParamNameAndOrganization(String paramName, String organization);

    @Query(value = "SELECT r FROM Rule r WHERE r.organization.name=:organization AND r.name=:name")
    List<Rule> findByNameAndOrganization(String name, String organization);
}
