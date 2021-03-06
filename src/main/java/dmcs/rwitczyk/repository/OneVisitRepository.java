package dmcs.rwitczyk.repository;

import dmcs.rwitczyk.domains.DoctorEntity;
import dmcs.rwitczyk.domains.OneVisitEntity;
import dmcs.rwitczyk.domains.PatientEntity;
import dmcs.rwitczyk.models.VisitStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OneVisitRepository extends JpaRepository<OneVisitEntity, Integer> {

    List<OneVisitEntity> findByDoctorEntity(DoctorEntity doctorEntity);

    List<OneVisitEntity> findByDoctorEntityAndPatientEntityId(DoctorEntity doctorEntity, int patientId);

    List<OneVisitEntity> findByDoctorEntityAndDate(DoctorEntity doctorEntity, Date date);

    List<OneVisitEntity> findByDoctorEntityAndDateAndStatus(DoctorEntity doctorEntity, Date date, VisitStatusEnum statusEnum);

    List<OneVisitEntity> findByPatientEntity(PatientEntity patientEntity);
}
