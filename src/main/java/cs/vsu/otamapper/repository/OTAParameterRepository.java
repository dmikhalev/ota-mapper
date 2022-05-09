package cs.vsu.otamapper.repository;

import cs.vsu.otamapper.entity.OTADictionary;
import cs.vsu.otamapper.entity.OTAParameter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OTAParameterRepository extends JpaRepository<OTAParameter, Long> {

    void deleteAllByOtaDictionary(OTADictionary otaDictionary);
}
