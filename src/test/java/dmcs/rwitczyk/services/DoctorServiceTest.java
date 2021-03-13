package dmcs.rwitczyk.services;

import dmcs.rwitczyk.domains.DoctorEntity;
import dmcs.rwitczyk.domains.OneVisitEntity;
import dmcs.rwitczyk.dto.AcceptVisitDto;
import dmcs.rwitczyk.dto.AvailableVisitsListDto;
import dmcs.rwitczyk.exceptions.AccountNotFoundException;
import dmcs.rwitczyk.exceptions.WrongDataException;
import dmcs.rwitczyk.models.VisitStatusEnum;
import dmcs.rwitczyk.repository.DoctorRepository;
import dmcs.rwitczyk.repository.OneVisitRepository;
import dmcs.rwitczyk.repository.UserLoginDataRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class DoctorServiceTest {

    private DoctorRepository doctorRepository;

    private OneVisitRepository oneVisitRepository;

    private UserLoginDataRepository userLoginDataRepository;

    private DoctorService doctorService;

    @BeforeEach
    void setUp() {
        doctorRepository = Mockito.mock(DoctorRepository.class);
        oneVisitRepository = Mockito.mock(OneVisitRepository.class);
        userLoginDataRepository = Mockito.mock(UserLoginDataRepository.class);
        doctorService = new DoctorService(userLoginDataRepository, doctorRepository, oneVisitRepository);
    }

    @Test
    void shouldReturnAllDoctorVisitsInStateNotEqualFree() {
        // given
        List<OneVisitEntity> oneVisitEntities = new ArrayList<>();
        oneVisitEntities.add(OneVisitEntity.builder().status(VisitStatusEnum.CANCELLED).date(Date.valueOf("2000-11-11")).build());
        oneVisitEntities.add(OneVisitEntity.builder().status(VisitStatusEnum.TO_ACCEPT).date(Date.valueOf("2000-11-11")).build());
        oneVisitEntities.add(OneVisitEntity.builder().status(VisitStatusEnum.ACCEPTED).date(Date.valueOf("2000-11-11")).build());
        oneVisitEntities.add(OneVisitEntity.builder().status(VisitStatusEnum.FREE).date(Date.valueOf("2000-11-11")).build());

        List<AvailableVisitsListDto> expectedDoctorVisitsList = new ArrayList<>();
        expectedDoctorVisitsList.add(AvailableVisitsListDto.builder().status(VisitStatusEnum.CANCELLED).date("2000-11-11").build());
        expectedDoctorVisitsList.add(AvailableVisitsListDto.builder().status(VisitStatusEnum.TO_ACCEPT).date("2000-11-11").build());
        expectedDoctorVisitsList.add(AvailableVisitsListDto.builder().status(VisitStatusEnum.ACCEPTED).date("2000-11-11").build());

        DoctorEntity doctorEntity = DoctorEntity.builder().build();

        // when
        Mockito.when(doctorRepository.findByUserLoginDataEntityId(Mockito.anyInt())).thenReturn(doctorEntity);
        Mockito.when(oneVisitRepository.findByDoctorEntity(doctorEntity)).thenReturn(oneVisitEntities);

        // then
        List<AvailableVisitsListDto> resultDoctorVisitsList = doctorService.getAllDoctorVisits(2);
        Assertions.assertEquals(expectedDoctorVisitsList, resultDoctorVisitsList);
    }

    @Test
    void shouldThrowExceptionWhenPriceInDtoNotFound() {
        //given
        OneVisitEntity oneVisitEntity = OneVisitEntity.builder().description("test").price("").build();
        Optional<OneVisitEntity> optional = Optional.of(oneVisitEntity);
        AcceptVisitDto acceptVisitDto = AcceptVisitDto.builder().visitId(2).description("Desc").price("").build();

        //when
        Mockito.when(oneVisitRepository.findById(Mockito.anyInt())).thenReturn(optional);

        //then
        try {
            doctorService.acceptVisit(acceptVisitDto);
            Assertions.fail();
        } catch (WrongDataException e) {
            Assertions.assertEquals(e.getMessage(), "Brak ceny");
        }
    }

    @Test
    void shouldThrowExceptionWhenVisitEntityNotFound() {
        //given
        AcceptVisitDto acceptVisitDto = AcceptVisitDto.builder().visitId(2).description("Desc").price("").build();
        Optional<OneVisitEntity> optional = Optional.empty();

        //when
        Mockito.when(oneVisitRepository.findById(2)).thenReturn(optional);

        //then
        try {
            doctorService.acceptVisit(acceptVisitDto);
            Assertions.fail();
        } catch (AccountNotFoundException e) {
            Assertions.assertEquals(e.getMessage(), "Nie znaleziono wizyty o takim id!");
        }
    }

    @Test
    void shouldAssertEqualsWhenSaveDoctorEntity() {
        //given
        DoctorEntity doctorEntity = DoctorEntity.builder().firstName("Roberto").lastName("Carlos").phoneNumber("123456").build();
        DoctorEntity expectedDoctorEntity = DoctorEntity.builder().firstName("Roberto").lastName("Carlos").phoneNumber("123456").build();

        //when
        Mockito.when(userLoginDataRepository.findUserLoginDataEntityByEmail(Mockito.anyString())).thenReturn(null);
        Mockito.when(doctorRepository.save(Mockito.any())).thenReturn(doctorEntity);

        //then
        org.assertj.core.api.Assertions.assertThat(doctorRepository.save(doctorEntity)).isEqualToComparingFieldByFieldRecursively(expectedDoctorEntity);
    }


}
