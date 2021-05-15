package dmcs.rwitczyk.services;

import dmcs.rwitczyk.domains.CovidTestRegistrationEntity;
import dmcs.rwitczyk.domains.LaboratoryTestEntity;
import dmcs.rwitczyk.domains.UserLoginDataEntity;
import dmcs.rwitczyk.dto.AvailableVisitsForCovidTestDto;
import dmcs.rwitczyk.dto.LaboratoryTestDto;
import dmcs.rwitczyk.dto.RegisterForACovidTestDto;
import dmcs.rwitczyk.exceptions.VisitAlreadyExistsException;
import dmcs.rwitczyk.models.CovidTestRegistrationStatusEnum;
import dmcs.rwitczyk.repository.CovidTestRegistrationRepository;
import dmcs.rwitczyk.repository.LaboratoryTestRepository;
import dmcs.rwitczyk.repository.UserLoginDataRepository;
import dmcs.rwitczyk.utils.Converters;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@Transactional
public class LaboratoryTestService {

    @Autowired
    private LaboratoryTestRepository laboratoryTestRepository;

    @Autowired
    private NurseService nurseService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private CovidTestRegistrationRepository covidTestRegistrationRepository;

    @Autowired
    private UserLoginDataRepository userLoginDataRepository;

    public void addLaboratoryTest(LaboratoryTestDto laboratoryTestDto) {
        LaboratoryTestEntity laboratoryTestEntity = Converters.convertLaboratoryTestDtoToEntity(laboratoryTestDto);
        laboratoryTestEntity.setNurseEntity(nurseService.findNurseEntityById(laboratoryTestDto.getNurseId()));
        laboratoryTestEntity.setPatientEntity(patientService.getPatientById(laboratoryTestDto.getPatientId()));
        laboratoryTestRepository.save(laboratoryTestEntity);
    }

    public void registerForACovidTest(RegisterForACovidTestDto registerForACovidTestDto) {
        UserLoginDataEntity user = userLoginDataRepository.findById(registerForACovidTestDto.getAccountId()).get();

        boolean isUserAlreadyRegistered = covidTestRegistrationRepository.existsByUserLoginDataEntityAndStatus(user, CovidTestRegistrationStatusEnum.BUSY);
        if (registerForACovidTestDto.getVisitType().equals("covidTest") && isUserAlreadyRegistered) {
            log.info("Istnieje juz zarezerwowane badanie covid dla konta o id:" + user.getId());
            throw new VisitAlreadyExistsException();
        }

        CovidTestRegistrationEntity covidTestRegistrationEntity = covidTestRegistrationRepository.findById(registerForACovidTestDto.getVisitId()).get();
        covidTestRegistrationEntity.setStatus(CovidTestRegistrationStatusEnum.BUSY);
        covidTestRegistrationEntity.setSeroTest(registerForACovidTestDto.isSeroTest());
        covidTestRegistrationEntity.setVisitType(registerForACovidTestDto.getVisitType());
        covidTestRegistrationEntity.setAntygenTest(registerForACovidTestDto.isAntygenTest());
        covidTestRegistrationEntity.setPcrTest(registerForACovidTestDto.isPcrTest());
        covidTestRegistrationEntity.setPrice(registerForACovidTestDto.getPrice());
        covidTestRegistrationEntity.setUserLoginDataEntity(user);

        covidTestRegistrationRepository.save(covidTestRegistrationEntity);
    }

    public List<AvailableVisitsForCovidTestDto> getAvailableVisitsForCovidTest(String date) {
        Date visitDate = Date.valueOf(date);
        log.info("Pobieram wizyty dla daty: " + visitDate);
        List<CovidTestRegistrationEntity> covidTestRegistrationEntities = covidTestRegistrationRepository.findByVisitDate(visitDate);

        if (covidTestRegistrationEntities.size() == 0) {
            log.info("Nie znaleziono wizyt covid w tym dniu. Tworze nowe");
            for (int i = 8; i < 16; i++) {
                CovidTestRegistrationEntity covidTestRegistrationEntity = CovidTestRegistrationEntity.builder()
                        .visitDate(visitDate)
                        .fromTime(i + ":00")
                        .toTime(i + 1 + ":00")
                        .status(CovidTestRegistrationStatusEnum.FREE)
                        .build();
                covidTestRegistrationRepository.save(covidTestRegistrationEntity);
            }
        }

        List<CovidTestRegistrationEntity> finalCovidTestRegistrationEntities = covidTestRegistrationRepository.findByVisitDateAndStatus(visitDate, CovidTestRegistrationStatusEnum.FREE);
        List<AvailableVisitsForCovidTestDto> availableVisitsListDtos = new ArrayList<>();

        for (CovidTestRegistrationEntity entity : finalCovidTestRegistrationEntities) {
            availableVisitsListDtos.add(AvailableVisitsForCovidTestDto.builder()
                    .id(entity.getId())
                    .visitDate(entity.getVisitDate())
                    .fromTime(entity.getFromTime())
                    .toTime(entity.getToTime())
                    .build());
        }

        return availableVisitsListDtos;
    }
}
