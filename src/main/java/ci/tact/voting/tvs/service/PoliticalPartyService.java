package ci.tact.voting.tvs.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ci.tact.voting.tvs.domain.PoliticalParty;
import ci.tact.voting.tvs.exception.ResourceNotFoundException;
import ci.tact.voting.tvs.repository.PoliticalPartyRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PoliticalPartyService {

    private final PoliticalPartyRepository politicalPartyRepository;
    private static final String ENTITY_NAME = "Political Party";

    @Transactional(readOnly = true)
    public List<PoliticalParty> findAll() {
        return politicalPartyRepository.findAll();
    }

    @Transactional(readOnly = true)
    public PoliticalParty findById(Long id) {
        return politicalPartyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ENTITY_NAME, "id", id));
    }

    @Transactional(readOnly = true)
    public PoliticalParty findByCode(String code) {
        return politicalPartyRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException(ENTITY_NAME, "code", code));
    }

    @Transactional
    public PoliticalParty create(PoliticalParty party) {
        if (politicalPartyRepository.existsByCode(party.getCode())) {
            throw new IllegalArgumentException("Political party already exists with code: " + party.getCode());
        }
        return politicalPartyRepository.save(party);
    }

    @Transactional
    public PoliticalParty update(Long id, PoliticalParty party) {
        PoliticalParty existingParty = findById(id);
        
        if (!existingParty.getCode().equals(party.getCode()) && 
            politicalPartyRepository.existsByCode(party.getCode())) {
            throw new IllegalArgumentException("Political party already exists with code: " + party.getCode());
        }

        existingParty.setCode(party.getCode());
        existingParty.setName(party.getName());
        
        return politicalPartyRepository.save(existingParty);
    }

    @Transactional
    public void delete(Long id) {
        PoliticalParty party = findById(id);
        politicalPartyRepository.delete(party);
    }
}