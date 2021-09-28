package cs.vsu.otamapper.repository;

import cs.vsu.otamapper.entity.OTADictionary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OTADictionaryRepository extends JpaRepository<OTADictionary, Long> {
}
