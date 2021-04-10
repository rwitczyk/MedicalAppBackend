package dmcs.rwitczyk.controller;

import dmcs.rwitczyk.dto.AddNurseAccountDto;
import dmcs.rwitczyk.dto.LaboratoryTestDto;
import dmcs.rwitczyk.services.LaboratoryTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/lab")
@CrossOrigin(origins = "http://localhost:4200")
public class LaboratoryTestController {

    @Autowired
    LaboratoryTestService laboratoryTestService;

    @PreAuthorize("hasRole('NURSE')")
    @PostMapping(value = "/add")
    public ResponseEntity<?> addNurseAccount(@RequestBody LaboratoryTestDto laboratoryTestDto) {
        laboratoryTestService.addLaboratoryTest(laboratoryTestDto);
        return ResponseEntity.ok().build();
    }
}
