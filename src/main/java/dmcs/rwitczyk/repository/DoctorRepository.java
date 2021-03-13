package dmcs.rwitczyk.repository;

import dmcs.rwitczyk.domains.DoctorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorRepository extends JpaRepository<DoctorEntity, Integer> {

    DoctorEntity findByUserLoginDataEntityId(int accountId);
}
