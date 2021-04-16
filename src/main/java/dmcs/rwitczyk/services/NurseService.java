package dmcs.rwitczyk.services;

import dmcs.rwitczyk.domains.NurseEntity;
import dmcs.rwitczyk.domains.RoleEntity;
import dmcs.rwitczyk.domains.UserLoginDataEntity;
import dmcs.rwitczyk.dto.AddNurseAccountDto;
import dmcs.rwitczyk.dto.EditNurseAccountDto;
import dmcs.rwitczyk.exceptions.AccountAlreadyExistsException;
import dmcs.rwitczyk.exceptions.AccountNotFoundException;
import dmcs.rwitczyk.models.RoleEnum;
import dmcs.rwitczyk.repository.NurseRepository;
import dmcs.rwitczyk.repository.UserLoginDataRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class NurseService {

    private final NurseRepository nurseRepository;

    private final UserLoginDataRepository userLoginDataRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public NurseService(NurseRepository nurseRepository, UserLoginDataRepository userLoginDataRepository, PasswordEncoder passwordEncoder) {
        this.nurseRepository = nurseRepository;
        this.userLoginDataRepository = userLoginDataRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void addNurseAccount(AddNurseAccountDto addNurseAccountDto) {
        if (userLoginDataRepository.findUserLoginDataEntityByEmail(addNurseAccountDto.getEmail()) != null) {
            throw new AccountAlreadyExistsException("Konto pielegniarki o takim email:" + addNurseAccountDto.getEmail() + " juz istnieje");
        }
        NurseEntity nurseEntity = NurseEntity.builder()
                .firstName(addNurseAccountDto.getFirstName())
                .lastName(addNurseAccountDto.getLastName())
                .userLoginDataEntity(UserLoginDataEntity.builder()
                        .email(addNurseAccountDto.getEmail())
                        .password(passwordEncoder.encode(addNurseAccountDto.getPassword()))
                        .enabled(true)
                        .roleEntity(RoleEntity.builder()
                                .role(RoleEnum.ROLE_NURSE)
                                .build())
                        .build())
                .build();
        nurseRepository.save(nurseEntity);

        log.info("Konto pielęgniarki zostało utworzone, email: " + addNurseAccountDto.getEmail());
    }

    public void editNurseAccount(EditNurseAccountDto nurseAccountDto) {
        NurseEntity nurseEntity = this.nurseRepository.findById(nurseAccountDto.getNurseId()).get();

        if (nurseAccountDto.getPassword().length() > 0) {
            nurseEntity.getUserLoginDataEntity().setPassword(passwordEncoder.encode(nurseAccountDto.getPassword()));
        }
        this.nurseRepository.save(nurseEntity);
    }

    public List<NurseEntity> getAllNurses() {
        List<NurseEntity> nurseEntities = nurseRepository.findAll();
        return nurseEntities;
    }

    public void changeAccountState(Long nurseId, boolean changeAccountAction) {
        if (!this.nurseRepository.findById(nurseId).isPresent()) {
            throw new AccountNotFoundException("Nie znaleziono konta o takim id!");
        }

        log.info("Konto pielegniarki zmienilo status na: " + changeAccountAction);

        NurseEntity nurseEntity = this.nurseRepository.findById(nurseId).get();
        nurseEntity.getUserLoginDataEntity().setEnabled(changeAccountAction);
        this.nurseRepository.save(nurseEntity);
    }

    public NurseEntity findNurseEntityById(Integer nurseId) {
        if (nurseRepository.findByUserLoginDataEntityId(nurseId) != null) {
            return nurseRepository.findByUserLoginDataEntityId(nurseId);
        }

        throw new AccountNotFoundException("Nie istnieje konto pielegniarki o id: " + nurseId);
    }
}
