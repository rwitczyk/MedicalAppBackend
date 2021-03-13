package dmcs.rwitczyk.controller;

import dmcs.rwitczyk.dto.AcceptVisitDto;
import dmcs.rwitczyk.dto.AddDoctorAccountDto;
import dmcs.rwitczyk.services.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/doctor")
@CrossOrigin(origins = "http://localhost:4200")
public class DoctorController {

    private DoctorService doctorService;

    @Autowired
    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/add")
    public ResponseEntity addDoctor(@RequestBody AddDoctorAccountDto addDoctorAccountDto) {
        doctorService.addDoctor(addDoctorAccountDto);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PATIENT')")
    @GetMapping(value = "/all")
    public ResponseEntity getAllDoctors() {
        return new ResponseEntity<>(doctorService.getAllDoctors(), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/activateAccount/{doctorId}")
    public ResponseEntity activateAccount(@PathVariable Integer doctorId) {
        doctorService.changeAccountState(doctorId, true);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/deactivateAccount/{doctorId}")
    public ResponseEntity deactivateAccount(@PathVariable Integer doctorId) {
        doctorService.changeAccountState(doctorId, false);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('DOCTOR')")
    @GetMapping(value = "/allVisits/{doctorAccountId}")
    public ResponseEntity getAllDoctorVisits(@PathVariable int doctorAccountId) {
        return new ResponseEntity<>(doctorService.getAllDoctorVisits(doctorAccountId), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('DOCTOR')")
    @PostMapping(value = "/acceptVisit")
    public ResponseEntity addDoctor(@RequestBody AcceptVisitDto acceptVisitDto) {
        doctorService.acceptVisit(acceptVisitDto);
        return new ResponseEntity(HttpStatus.OK);
    }
}
