package dmcs.rwitczyk.services;

import dmcs.rwitczyk.domains.PatientEntity;
import dmcs.rwitczyk.repository.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

class PatientServiceTest {

    private PatientRepository patientRepository;

    private PatientService patientService;

    private DoctorRepository doctorRepository;

    private UserLoginDataRepository userLoginDataRepository;

    private AccountService accountService;

    private OneVisitRepository oneVisitRepository;

    private PasswordEncoder passwordEncoder;

    private AdvertisingGroupRepository advertisingGroupRepository;

    @BeforeEach
    void setUp() {
        patientRepository = Mockito.mock(PatientRepository.class);
        doctorRepository = Mockito.mock(DoctorRepository.class);
        userLoginDataRepository = Mockito.mock(UserLoginDataRepository.class);
        accountService = Mockito.mock(AccountService.class);
        oneVisitRepository = Mockito.mock(OneVisitRepository.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        patientService = new PatientService(patientRepository, doctorRepository, userLoginDataRepository, accountService, oneVisitRepository, passwordEncoder, advertisingGroupRepository);
    }

    @Test
    void getAllPatients() {
        // given
        List<PatientEntity> allPatients = new ArrayList<>();
        allPatients.add(PatientEntity.builder().firstName("Janusz").build());
        allPatients.add(PatientEntity.builder().firstName("Grazyna").build());

        // when
        Mockito.when(patientRepository.findAll()).thenReturn(allPatients);

        // then
        Assertions.assertEquals(allPatients, patientService.getAllPatients());
    }
}
