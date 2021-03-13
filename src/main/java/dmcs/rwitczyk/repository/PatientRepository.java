package dmcs.rwitczyk.repository;

import dmcs.rwitczyk.domains.PatientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface PatientRepository extends JpaRepository<PatientEntity, Integer> {
    PatientEntity findByUserLoginDataEntityId(Integer id);

}
