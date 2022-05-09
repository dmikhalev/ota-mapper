package cs.vsu.otamapper.repository;

import cs.vsu.otamapper.entity.OTADictionary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OTADictionaryRepository extends JpaRepository<OTADictionary, Long> {

    @Query("SELECT d.id, d.name FROM OTADictionary d WHERE d.user.id=:userId")
    List<Object[]> getIdAndNameByUser_Id(long userId);
}
