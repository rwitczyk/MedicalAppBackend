package dmcs.rwitczyk.repository;

import dmcs.rwitczyk.domains.NurseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NurseRepository extends JpaRepository<NurseEntity, Long> {

    NurseEntity findByUserLoginDataEntityId(Integer id);
}
