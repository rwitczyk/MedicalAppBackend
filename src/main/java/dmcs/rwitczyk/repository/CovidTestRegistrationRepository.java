package dmcs.rwitczyk.repository;

import dmcs.rwitczyk.domains.CovidTestRegistrationEntity;
import dmcs.rwitczyk.domains.UserLoginDataEntity;
import dmcs.rwitczyk.models.CovidTestRegistrationStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface CovidTestRegistrationRepository extends JpaRepository<CovidTestRegistrationEntity, Long> {
    List<CovidTestRegistrationEntity> findByVisitDate(Date visitDate);

    List<CovidTestRegistrationEntity> findByVisitDateAndStatus(Date visitDate, CovidTestRegistrationStatusEnum covidTestRegistrationStatusEnum);

    Boolean existsByUserLoginDataEntityAndStatus(UserLoginDataEntity user, CovidTestRegistrationStatusEnum statusEnum);
}
