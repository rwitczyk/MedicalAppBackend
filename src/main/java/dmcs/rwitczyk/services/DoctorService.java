package dmcs.rwitczyk.services;

import dmcs.rwitczyk.domains.DoctorEntity;
import dmcs.rwitczyk.domains.OneVisitEntity;
import dmcs.rwitczyk.dto.AcceptVisitDto;
import dmcs.rwitczyk.dto.AddDoctorAccountDto;
import dmcs.rwitczyk.dto.AvailableVisitsListDto;
import dmcs.rwitczyk.exceptions.AccountAlreadyExistsException;
import dmcs.rwitczyk.exceptions.AccountNotFoundException;
import dmcs.rwitczyk.exceptions.WrongDataException;
import dmcs.rwitczyk.models.VisitStatusEnum;
import dmcs.rwitczyk.repository.DoctorRepository;
import dmcs.rwitczyk.repository.OneVisitRepository;
import dmcs.rwitczyk.repository.UserLoginDataRepository;
import dmcs.rwitczyk.utils.Converters;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@Transactional
public class DoctorService {

    private UserLoginDataRepository userLoginDataRepository;

    private DoctorRepository doctorRepository;

    private OneVisitRepository oneVisitRepository;

    @Autowired
    public DoctorService(UserLoginDataRepository userLoginDataRepository, DoctorRepository doctorRepository, OneVisitRepository oneVisitRepository) {
        this.userLoginDataRepository = userLoginDataRepository;
        this.doctorRepository = doctorRepository;
        this.oneVisitRepository = oneVisitRepository;
    }

    public void addDoctor(AddDoctorAccountDto addDoctorAccountDto) {
        if (userLoginDataRepository.findUserLoginDataEntityByEmail(addDoctorAccountDto.getEmail()) != null) {
            throw new AccountAlreadyExistsException("Konto o takim adresie email juz istnieje");
        }

        DoctorEntity doctorEntity = Converters.convertDoctorDtoToEntity(addDoctorAccountDto);
        doctorEntity.getUserLoginDataEntity().setPassword(passwordEncoder().encode(doctorEntity.getUserLoginDataEntity().getPassword()));
        doctorEntity.getUserLoginDataEntity().setEnabled(true);
        doctorRepository.save(doctorEntity);
    }


    public void changeAccountState(Integer doctorId, boolean changeAccountAction) {
        if (!this.doctorRepository.findById(doctorId).isPresent()) {
            throw new AccountNotFoundException("Nie znaleziono konta o takim id!");
        }

        log.info("Konto doktora zmienilo status na: " + changeAccountAction);

        DoctorEntity doctorEntity = this.doctorRepository.findById(doctorId).get();
        doctorEntity.getUserLoginDataEntity().setEnabled(changeAccountAction);
        this.doctorRepository.save(doctorEntity);
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public List<DoctorEntity> getAllDoctors() {
        List<DoctorEntity> doctorEntities = doctorRepository.findAll();
        return doctorEntities;
    }

    public List<AvailableVisitsListDto> getAllDoctorVisits(int doctorAccountId) {
        DoctorEntity doctorEntity = doctorRepository.findByUserLoginDataEntityId(doctorAccountId);
        List<OneVisitEntity> oneVisitEntities = oneVisitRepository.findByDoctorEntity(doctorEntity);

        List<AvailableVisitsListDto> availableVisitsListDtos = new ArrayList<>();
        for (OneVisitEntity oneVisitEntity : oneVisitEntities) {
            if (oneVisitEntity.getStatus() != VisitStatusEnum.FREE) {
                availableVisitsListDtos.add(Converters.convertOneVisitEntityToAvailableVisitsListDto(oneVisitEntity));
            }
        }

        return availableVisitsListDtos;
    }

    public void acceptVisit(AcceptVisitDto acceptVisitDto) {
        if (!oneVisitRepository.findById(acceptVisitDto.getVisitId()).isPresent()) {
            throw new AccountNotFoundException("Nie znaleziono wizyty o takim id!");
        }

        if (acceptVisitDto.getPrice().length() == 0) {
            throw new WrongDataException("Brak ceny");
        }

        OneVisitEntity oneVisitEntity = oneVisitRepository.findById(acceptVisitDto.getVisitId()).get();
        oneVisitEntity.setDescription(acceptVisitDto.getDescription());
        oneVisitEntity.setPrice(acceptVisitDto.getPrice());
        oneVisitEntity.setStatus(VisitStatusEnum.ACCEPTED);
        oneVisitRepository.save(oneVisitEntity);
    }
}
