package dmcs.rwitczyk.services;

import dmcs.rwitczyk.domains.PatientEntity;
import dmcs.rwitczyk.repository.DoctorRepository;
import dmcs.rwitczyk.repository.OneVisitRepository;
import dmcs.rwitczyk.repository.PatientRepository;
import dmcs.rwitczyk.repository.UserLoginDataRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

class PatientServiceTest {

    private PatientRepository patientRepository;

    private PatientService patientService;

    private DoctorRepository doctorRepository;

    private UserLoginDataRepository userLoginDataRepository;

    private AccountService accountService;

    private OneVisitRepository oneVisitRepository;

    @BeforeEach
    void setUp() {
        patientRepository = Mockito.mock(PatientRepository.class);
        doctorRepository = Mockito.mock(DoctorRepository.class);
        userLoginDataRepository = Mockito.mock(UserLoginDataRepository.class);
        accountService = Mockito.mock(AccountService.class);
        oneVisitRepository = Mockito.mock(OneVisitRepository.class);
        patientService = new PatientService(patientRepository, doctorRepository, userLoginDataRepository, accountService, oneVisitRepository);
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
