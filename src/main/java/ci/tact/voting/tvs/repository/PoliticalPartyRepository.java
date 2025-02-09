package ci.tact.voting.tvs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ci.tact.voting.tvs.domain.PoliticalParty;

import java.util.Optional;

@Repository
public interface PoliticalPartyRepository extends JpaRepository<PoliticalParty, Long> {
    Optional<PoliticalParty> findByCode(String code);
    boolean existsByCode(String code);
}
