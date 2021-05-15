package dmcs.rwitczyk.services;

import dmcs.rwitczyk.domains.AdvertisingGroupEntity;
import dmcs.rwitczyk.domains.DoctorEntity;
import dmcs.rwitczyk.domains.OneVisitEntity;
import dmcs.rwitczyk.domains.PatientEntity;
import dmcs.rwitczyk.dto.*;
import dmcs.rwitczyk.exceptions.AccountAlreadyExistsException;
import dmcs.rwitczyk.exceptions.AccountNotFoundException;
import dmcs.rwitczyk.exceptions.UnauthorizedAccessException;
import dmcs.rwitczyk.models.VisitStatusEnum;
import dmcs.rwitczyk.models.VisitTypeEnum;
import dmcs.rwitczyk.repository.*;
import dmcs.rwitczyk.utils.Converters;
import dmcs.rwitczyk.utils.OneVisitPdfGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
@Transactional
public class PatientService {

    private PatientRepository patientRepository;

    private DoctorRepository doctorRepository;

    private UserLoginDataRepository userLoginDataRepository;

    private AccountService accountService;

    private OneVisitRepository oneVisitRepository;

    private PasswordEncoder passwordEncoder;

    private AdvertisingGroupRepository advertisingGroupRepository;

    @Autowired
    public PatientService(PatientRepository patientRepository, DoctorRepository doctorRepository, UserLoginDataRepository userLoginDataRepository, AccountService accountService, OneVisitRepository oneVisitRepository, PasswordEncoder passwordEncoder, AdvertisingGroupRepository advertisingGroupRepository) {
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.userLoginDataRepository = userLoginDataRepository;
        this.accountService = accountService;
        this.oneVisitRepository = oneVisitRepository;
        this.passwordEncoder = passwordEncoder;
        this.advertisingGroupRepository = advertisingGroupRepository;
    }

    public void addPatient(AddPatientAccountDto addPatientAccountDto) {
        if (userLoginDataRepository.findUserLoginDataEntityByEmail(addPatientAccountDto.getEmail()) != null) {
            throw new AccountAlreadyExistsException("Konto o takim adresie email juz istnieje");
        }

        PatientEntity patientEntity = Converters.convertPatientDtoToEntity(addPatientAccountDto);
        patientEntity.getUserLoginDataEntity().setPassword(passwordEncoder.encode(patientEntity.getUserLoginDataEntity().getPassword()));
        PatientEntity patient = patientRepository.save(patientEntity);
        accountService.sendActivationEmail(patient.getId());
    }

    public void editPatient(EditPatientDataDto patientModel) {
        PatientEntity patientEntity = this.patientRepository.findById(patientModel.getPatientId()).get();

        patientEntity.setFirstName(patientModel.getFirstName());
        patientEntity.setLastName(patientModel.getLastName());
        patientEntity.setPhoneNumber(patientModel.getPhoneNumber());

        if (patientModel.getPassword().length() > 0) {
            patientEntity.getUserLoginDataEntity().setPassword(passwordEncoder.encode(patientModel.getPassword()));
        }
        this.patientRepository.save(patientEntity);
    }

    public List<PatientEntity> getAllPatients() {
        List<PatientEntity> allPatients = patientRepository.findAll();
        return allPatients;
    }

    public List<PatientEntity> getAllDoctorPatients(int doctorAccountId) {
        DoctorEntity doctor = doctorRepository.findByUserLoginDataEntityId(doctorAccountId);

        List<PatientEntity> allPatients = patientRepository.findAll();

        return allPatients.stream()
                .filter(patient -> patient.getAdvertisingGroups().stream().anyMatch(adsGroup -> adsGroup.getName().equals(doctor.getSpecialization()))
                ).collect(Collectors.toList());

    }

    public void changeAccountState(Integer patientId, boolean changeAccountAction) {
        if (!this.patientRepository.findById(patientId).isPresent()) {
            throw new AccountNotFoundException("Nie znaleziono konta o takim id!");
        }

        log.info("Konto zmienilo status na: " + changeAccountAction);

        PatientEntity patientEntity = this.patientRepository.findById(patientId).get();
        patientEntity.getUserLoginDataEntity().setEnabled(changeAccountAction);
        this.patientRepository.save(patientEntity);
    }

    public PatientEntity getPatientById(Integer patientAccountId) {
        if (patientRepository.findByUserLoginDataEntityId(patientAccountId) == null) {
            throw new AccountNotFoundException("Nie znaleziono konta o takim id!");
        }

        return patientRepository.findByUserLoginDataEntityId(patientAccountId);
    }

    public List<AvailableVisitsListDto> getAllPatientVisits(int patientAccountId) {
        PatientEntity patientEntity = patientRepository.findByUserLoginDataEntityId(patientAccountId);
        List<OneVisitEntity> oneVisitEntities = oneVisitRepository.findByPatientEntity(patientEntity);

        List<AvailableVisitsListDto> availableVisitsListDtos = new ArrayList<>();
        for (OneVisitEntity oneVisitEntity : oneVisitEntities) {
            availableVisitsListDtos.add(Converters.convertOneVisitEntityToAvailableVisitsListDto(oneVisitEntity));
        }

        return availableVisitsListDtos;
    }


    public List<AvailableVisitsListDto> getAvailableVisits(GetAvailableVisitsDto getAvailableVisitsDto) {
        Date visitDate = Date.valueOf(getAvailableVisitsDto.getDate());
        if (!doctorRepository.findById(getAvailableVisitsDto.getDoctorId()).isPresent()) {
            throw new AccountNotFoundException("Nie znaleziono konta o takim id!");
        }


        DoctorEntity doctorEntity = doctorRepository.findById(getAvailableVisitsDto.getDoctorId()).get();
        log.info("Pobieram wizyty dla daty: " + visitDate + " i doktora o id: " + doctorEntity.getId());
        List<OneVisitEntity> oneVisitEntities = oneVisitRepository.findByDoctorEntityAndDate(doctorEntity, visitDate);

        if (oneVisitEntities.size() == 0) {
            log.info("Nie znaleziono wizyt doktora w tym dniu. Tworze nowe");
            for (int i = 8; i < 16; i++) {
                OneVisitEntity oneVisitEntity = new OneVisitEntity();
                oneVisitEntity.setDoctorEntity(doctorEntity);
                oneVisitEntity.setDate(visitDate);
                oneVisitEntity.setFromTime(i + ":00");
                oneVisitEntity.setToTime(i + 1 + ":00");
                oneVisitEntity.setStatus(VisitStatusEnum.FREE);
                oneVisitRepository.save(oneVisitEntity);
            }
        }

        List<OneVisitEntity> finalAvailableVisits = oneVisitRepository.findByDoctorEntityAndDateAndStatus(doctorEntity, visitDate, VisitStatusEnum.FREE);
        finalAvailableVisits.addAll(oneVisitRepository.findByDoctorEntityAndDateAndStatus(doctorEntity, visitDate, VisitStatusEnum.CANCELLED));

        List<AvailableVisitsListDto> availableVisitsListDtos = new ArrayList<>();

        for (OneVisitEntity oneVisitEntity : finalAvailableVisits) {
            AvailableVisitsListDto availableVisitsListDto = Converters.convertOneVisitEntityToAvailableVisitsListDto(oneVisitEntity);
            availableVisitsListDtos.add(availableVisitsListDto);
        }

        return availableVisitsListDtos;
    }

    public void reserveVisit(ReserveVisitDto reserveVisitDto) {
        OneVisitEntity oneVisitEntity = oneVisitRepository.findById(reserveVisitDto.getVisitId()).get();
        PatientEntity patientEntity = patientRepository.findByUserLoginDataEntityId(reserveVisitDto.getPatientAccountId());

        if (!patientEntity.getAdvertisingGroups().stream().anyMatch(group -> oneVisitEntity.getDoctorEntity().getSpecialization().equals(group.getName()))) {
            advertisingGroupRepository.save(AdvertisingGroupEntity.builder()
                    .name(oneVisitEntity.getDoctorEntity().getSpecialization())
                    .patient(patientEntity)
                    .build());
        }
        ;

        oneVisitEntity.setVisitType(VisitTypeEnum.of(reserveVisitDto.getVisitType()));
        oneVisitEntity.setPatientEntity(patientEntity);
        oneVisitEntity.setStatus(VisitStatusEnum.TO_ACCEPT);
        oneVisitRepository.save(oneVisitEntity);
    }

    public void cancelVisit(int visitId) {
        if (!oneVisitRepository.findById(visitId).isPresent()) {
            throw new AccountNotFoundException("Nie znaleziono wizyty o takim id!");
        }

        OneVisitEntity oneVisitEntity = oneVisitRepository.findById(visitId).get();
        oneVisitEntity.setStatus(VisitStatusEnum.CANCELLED);
        oneVisitRepository.save(oneVisitEntity);
    }

    public ByteArrayOutputStream downloadPdf(int visitId) {
        if (!oneVisitRepository.findById(visitId).isPresent()) {
            throw new AccountNotFoundException("Nie znaleziono wizyty o takim id!");
        }
        OneVisitEntity oneVisitEntity = oneVisitRepository.findById(visitId).get();
        String visitUserEmail = oneVisitEntity.getPatientEntity() == null ? "" : oneVisitEntity.getPatientEntity().getUserLoginDataEntity().getEmail();

        if (!((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername().equals(visitUserEmail)) {
            throw new UnauthorizedAccessException("Nieprawidlowy user id. Próba pobrania pliku innego usera!");
        }

        return OneVisitPdfGenerator.getInstance(oneVisitEntity).generatePdf();
    }
}
