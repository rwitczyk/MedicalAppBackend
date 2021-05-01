package dmcs.rwitczyk.controller;

import dmcs.rwitczyk.dto.AvailableVisitsForCovidTestDto;
import dmcs.rwitczyk.dto.LaboratoryTestDto;
import dmcs.rwitczyk.dto.RegisterForACovidTestDto;
import dmcs.rwitczyk.services.LaboratoryTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/lab")
@CrossOrigin(origins = "http://localhost:4200")
public class LaboratoryTestController {

    @Autowired
    LaboratoryTestService laboratoryTestService;

    @PreAuthorize("hasRole('NURSE')")
    @PostMapping(value = "/add")
    public ResponseEntity<?> addLaboratoryTestResult(@RequestBody LaboratoryTestDto laboratoryTestDto) {
        laboratoryTestService.addLaboratoryTest(laboratoryTestDto);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('NURSE', 'DOCTOR', 'PATIENT')")
    @GetMapping(value = "/availableVisits/{visitDate}")
    public ResponseEntity<List<AvailableVisitsForCovidTestDto>> getAvailableVisitsListForCovidTest(@PathVariable String visitDate) {
        return new ResponseEntity<>(laboratoryTestService.getAvailableVisitsForCovidTest(visitDate), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('NURSE', 'DOCTOR', 'PATIENT')")
    @PostMapping(value = "/registerCovidTest")
    public ResponseEntity<?> registerForACovidTest(@RequestBody RegisterForACovidTestDto registerForACovidTestDto) {
        laboratoryTestService.registerForACovidTest(registerForACovidTestDto);
        return ResponseEntity.ok().build();
    }
}
