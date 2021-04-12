package dmcs.rwitczyk.services;

import dmcs.rwitczyk.domains.LaboratoryTestEntity;
import dmcs.rwitczyk.dto.LaboratoryTestDto;
import dmcs.rwitczyk.repository.LaboratoryTestRepository;
import dmcs.rwitczyk.utils.Converters;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public void addLaboratoryTest(LaboratoryTestDto laboratoryTestDto) {
        LaboratoryTestEntity laboratoryTestEntity = Converters.convertLaboratoryTestDtoToEntity(laboratoryTestDto);
        laboratoryTestEntity.setNurseEntity(nurseService.findNurseEntityById(laboratoryTestDto.getNurseId()));
        laboratoryTestEntity.setPatientEntity(patientService.getPatientById(laboratoryTestDto.getPatientId()));
        laboratoryTestRepository.save(laboratoryTestEntity);

    }
}
