package cs.vsu.otamapper.service;

import cs.vsu.otamapper.entity.OTADictionary;
import cs.vsu.otamapper.repository.OTADictionaryRepository;
import org.springframework.stereotype.Service;

@Service
public class OTADictionaryService {

    private final OTADictionaryRepository dictionaryRepository;
    private final OTAParameterService parameterService;

    public OTADictionaryService(OTADictionaryRepository dictionaryRepository, OTAParameterService parameterService) {
        this.dictionaryRepository = dictionaryRepository;
        this.parameterService = parameterService;
    }

    public OTADictionary createOrUpdate(OTADictionary dictionary, boolean createOrUpdateParams) {
        OTADictionary result = dictionaryRepository.save(dictionary);
        if (createOrUpdateParams && dictionary.getOtaParameters() != null) {
            parameterService.saveAll(dictionary.getOtaParameters());
        }
        return result;
    }

    public void delete(Long id) {
        dictionaryRepository.deleteById(id);
    }
}
