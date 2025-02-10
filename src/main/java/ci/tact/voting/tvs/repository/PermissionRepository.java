package ci.tact.voting.tvs.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ci.tact.voting.tvs.domain.PermissionEntity;

@Repository
public interface PermissionRepository extends JpaRepository<PermissionEntity, String> {

    Optional<PermissionEntity> findByName(String name);
    List<PermissionEntity> findByNameIn(List<String> names);

}
