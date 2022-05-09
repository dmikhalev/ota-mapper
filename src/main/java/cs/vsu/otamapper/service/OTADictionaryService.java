package cs.vsu.otamapper.service;

import cs.vsu.otamapper.entity.OTADictionary;
import cs.vsu.otamapper.repository.OTADictionaryRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<OTADictionary> findAllWithoutParameters(long userId) {
        return dictionaryRepository.getIdAndNameByUser_Id(userId).stream()
                .map(d -> new OTADictionary((long) d[0], (String) d[1]))
                .collect(Collectors.toList());
    }

    public OTADictionary findById(long id) {
        return dictionaryRepository.findById(id).orElse(null);
    }

    @Transactional
    public void delete(Long id) {
        OTADictionary otaDictionary = findById(id);
        if (otaDictionary != null) {
            parameterService.deleteAllByDictionaryId(otaDictionary);
            dictionaryRepository.deleteById(id);
        }
    }
}
