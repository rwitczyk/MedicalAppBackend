package dmcs.rwitczyk.services;

import dmcs.rwitczyk.domains.NurseEntity;
import dmcs.rwitczyk.domains.RoleEntity;
import dmcs.rwitczyk.domains.UserLoginDataEntity;
import dmcs.rwitczyk.dto.AddNurseAccountDto;
import dmcs.rwitczyk.exceptions.AccountAlreadyExistsException;
import dmcs.rwitczyk.models.RoleEnum;
import dmcs.rwitczyk.repository.NurseRepository;
import dmcs.rwitczyk.repository.UserLoginDataRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

}