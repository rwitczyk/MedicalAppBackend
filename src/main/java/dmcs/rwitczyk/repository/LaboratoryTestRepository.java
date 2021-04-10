package dmcs.rwitczyk.repository;

import dmcs.rwitczyk.domains.DoctorEntity;
import dmcs.rwitczyk.domains.LaboratoryTestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LaboratoryTestRepository extends JpaRepository<LaboratoryTestEntity, Integer> {
}
