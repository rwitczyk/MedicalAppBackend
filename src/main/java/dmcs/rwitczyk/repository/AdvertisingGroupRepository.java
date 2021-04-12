package dmcs.rwitczyk.repository;

import dmcs.rwitczyk.domains.AdvertisingGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface AdvertisingGroupRepository extends JpaRepository<AdvertisingGroupEntity, Integer> {
    List<AdvertisingGroupEntity> findByPatientId(int patientId);
}
