package cs.vsu.otamapper.service;

import cs.vsu.otamapper.entity.OTADictionary;
import cs.vsu.otamapper.repository.OTADictionaryRepository;
import org.springframework.stereotype.Service;

@Service
public class OTADictionaryService {

    private final OTADictionaryRepository dictionaryRepository;

    public OTADictionaryService(OTADictionaryRepository dictionaryRepository) {
        this.dictionaryRepository = dictionaryRepository;
    }

    public OTADictionary createOrUpdate(OTADictionary dictionary) {
        return dictionaryRepository.save(dictionary);
    }

    public void delete(Long id) {
        dictionaryRepository.deleteById(id);
    }
}
