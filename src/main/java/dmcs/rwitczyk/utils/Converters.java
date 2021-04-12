package dmcs.rwitczyk.utils;

import dmcs.rwitczyk.domains.*;
import dmcs.rwitczyk.dto.AddDoctorAccountDto;
import dmcs.rwitczyk.dto.AddPatientAccountDto;
import dmcs.rwitczyk.dto.AvailableVisitsListDto;
import dmcs.rwitczyk.dto.LaboratoryTestDto;
import dmcs.rwitczyk.models.RoleEnum;

public class Converters {

    public static DoctorEntity convertDoctorDtoToEntity(AddDoctorAccountDto addDoctorAccountDto) {
        return DoctorEntity.builder()
                .firstName(addDoctorAccountDto.getFirstName())
                .lastName(addDoctorAccountDto.getLastName())
                .specialization(addDoctorAccountDto.getSpecialization())
                .phoneNumber(addDoctorAccountDto.getPhoneNumber())
                .userLoginDataEntity(UserLoginDataEntity.builder()
                        .email(addDoctorAccountDto.getEmail())
                        .password(addDoctorAccountDto.getPassword())
                        .roleEntity(RoleEntity.builder()
                                .role(RoleEnum.ROLE_DOCTOR)
                                .build())
                        .build())
                .build();
    }

    public static PatientEntity convertPatientDtoToEntity(AddPatientAccountDto addPatientAccountDto) {
        return PatientEntity.builder()
                .firstName(addPatientAccountDto.getFirstName())
                .lastName(addPatientAccountDto.getLastName())
                .pesel(addPatientAccountDto.getPesel())
                .phoneNumber(addPatientAccountDto.getPhoneNumber())
                .userLoginDataEntity(UserLoginDataEntity.builder()
                        .email(addPatientAccountDto.getEmail())
                        .password(addPatientAccountDto.getPassword())
                        .roleEntity(RoleEntity.builder()
                                .role(RoleEnum.ROLE_PATIENT)
                                .build())
                        .build())
                .build();
    }

    public static AvailableVisitsListDto convertOneVisitEntityToAvailableVisitsListDto(OneVisitEntity oneVisitEntity) {
        return AvailableVisitsListDto.builder()
                .id(oneVisitEntity.getId())
                .date(oneVisitEntity.getDate().toString())
                .fromTime(oneVisitEntity.getFromTime())
                .toTime(oneVisitEntity.getToTime())
                .description(oneVisitEntity.getDescription())
                .status(oneVisitEntity.getStatus())
                .price(oneVisitEntity.getPrice())
                .build();
    }


    public static LaboratoryTestEntity convertLaboratoryTestDtoToEntity(LaboratoryTestDto laboratoryTestDto) {
        return LaboratoryTestEntity.builder()
                .subject(laboratoryTestDto.getSubject())
                .description(laboratoryTestDto.getDescription())
                .antygenTest(laboratoryTestDto.isAntygen())
                .pcrTest(laboratoryTestDto.isPcr())
                .seroTest(laboratoryTestDto.isSero())
                .covidResult(laboratoryTestDto.isSero() || laboratoryTestDto.isPcr() || laboratoryTestDto.isAntygen())
                .build();
    }
}
