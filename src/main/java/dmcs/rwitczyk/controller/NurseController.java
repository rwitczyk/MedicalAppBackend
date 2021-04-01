package dmcs.rwitczyk.controller;

import dmcs.rwitczyk.domains.NurseEntity;
import dmcs.rwitczyk.dto.AddNurseAccountDto;
import dmcs.rwitczyk.dto.EditNurseAccountDto;
import dmcs.rwitczyk.services.NurseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/nurse")
@CrossOrigin(origins = "http://localhost:4200")
public class NurseController {

    @Autowired
    private NurseService nurseService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> addNurseAccount(@RequestBody AddNurseAccountDto addNurseAccountDto) {
        nurseService.addNurseAccount(addNurseAccountDto);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'NURSE')")
    @PostMapping("/edit")
    public ResponseEntity<?> editNurseAccount(@RequestBody EditNurseAccountDto editNurseAccountDto) {
        nurseService.editNurseAccount(editNurseAccountDto);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'NURSE')")
    @GetMapping("/{nurseId}")
    public ResponseEntity<NurseEntity> getNurseById(@PathVariable Integer nurseId) {
        return new ResponseEntity<>(nurseService.findNurseEntityById(nurseId), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<NurseEntity>> getAllNurses() {
        return new ResponseEntity<>(nurseService.getAllNurses(), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/activateAccount/{nurseId}")
    public ResponseEntity activateAccount(@PathVariable Long nurseId) {
        nurseService.changeAccountState(nurseId, true);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/deactivateAccount/{nurseId}")
    public ResponseEntity deactivateAccount(@PathVariable Long nurseId) {
        nurseService.changeAccountState(nurseId, false);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
