package cs.vsu.otamapper.service;

import cs.vsu.otamapper.entity.OTADictionary;
import cs.vsu.otamapper.entity.OTAParameter;
import cs.vsu.otamapper.repository.OTAParameterRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class OTAParameterService {

    private final OTAParameterRepository parameterRepository;

    public OTAParameterService(OTAParameterRepository parameterRepository) {
        this.parameterRepository = parameterRepository;
    }

    public OTAParameter createOrUpdate(OTAParameter parameter) {
        return parameterRepository.save(parameter);
    }

    public void saveAll(List<OTAParameter> parameters) {
        parameterRepository.saveAll(parameters);
    }

    public void delete(Long id) {
        parameterRepository.deleteById(id);
    }

    @Transactional
    public void deleteAllByDictionaryId(OTADictionary otaDictionary) {
        parameterRepository.deleteAllByOtaDictionary(otaDictionary);
    }
}
