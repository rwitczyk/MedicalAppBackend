package dmcs.rwitczyk.utils;

import dmcs.rwitczyk.domains.PatientEntity;
import dmcs.rwitczyk.domains.RoleEntity;
import dmcs.rwitczyk.domains.UserLoginDataEntity;
import dmcs.rwitczyk.dto.AddPatientAccountDto;
import dmcs.rwitczyk.models.RoleEnum;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class ConvertersTest {

    @Test
    public void shouldReturnTrueIfCorrectlyConvertAddPatientAccountDtoToEntity() {
        //given
        AddPatientAccountDto addPatientAccountDto = AddPatientAccountDto.builder()
                .email("email")
                .firstName("imie")
                .lastName("nazwisko")
                .password("haslo")
                .pesel("pesel")
                .phoneNumber("telefon")
                .build();

        PatientEntity expectedPatientEntity = PatientEntity.builder()
                .firstName("imie")
                .lastName("nazwisko")
                .pesel("pesel")
                .phoneNumber("telefon")
                .userLoginDataEntity(UserLoginDataEntity.builder()
                        .email("email")
                        .password("haslo")
                        .roleEntity(RoleEntity.builder()
                                .role(RoleEnum.ROLE_PATIENT)
                                .build())
                        .build())
                .build();

        //when
        PatientEntity patientEntity = Converters.convertPatientDtoToEntity(addPatientAccountDto);

        //then
        assertThat(expectedPatientEntity).isEqualToComparingFieldByFieldRecursively(patientEntity);
    }
}
