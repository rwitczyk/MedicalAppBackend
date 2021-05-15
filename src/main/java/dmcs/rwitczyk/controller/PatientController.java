package dmcs.rwitczyk.controller;

import dmcs.rwitczyk.dto.*;
import dmcs.rwitczyk.services.AccountService;
import dmcs.rwitczyk.services.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.util.List;

@RestController
@RequestMapping(value = "/patient")
@CrossOrigin(origins = "http://localhost:4200")
public class PatientController {

    private PatientService patientService;

    private AccountService accountService;

    @Autowired
    public PatientController(PatientService patientService, AccountService accountService) {
        this.patientService = patientService;
        this.accountService = accountService;
    }

    @PostMapping(value = "/add")
    public ResponseEntity addPatient(@RequestBody AddPatientAccountDto addPatientAccountDto) {
        patientService.addPatient(addPatientAccountDto);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN','NURSE', 'DOCTOR')")
    @GetMapping(value = "/all")
    public ResponseEntity getAllPatients() {
        return new ResponseEntity<>(patientService.getAllPatients(), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN','NURSE', 'DOCTOR')")
    @GetMapping(value = "/all/{doctorAccountId}")
    public ResponseEntity getAllDoctorPatients(@PathVariable Integer doctorAccountId) {
        return new ResponseEntity<>(patientService.getAllDoctorPatients(doctorAccountId), HttpStatus.OK);
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'PATIENT', 'DOCTOR')")
    @GetMapping(value = "/getById/{patientAccountId}")
    public ResponseEntity getPatientById(@PathVariable Integer patientAccountId) {
        return new ResponseEntity<>(patientService.getPatientById(patientAccountId), HttpStatus.OK);
    }

    @GetMapping(value = "/confirmAccount/{token}")
    public ResponseEntity confirmAccount(@PathVariable String token) {
        accountService.confirmAccountByLink(token);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping(value = "/edit")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity editPatient(@RequestBody EditPatientDataDto patient) {
        patientService.editPatient(patient);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/activateAccount/{patientId}")
    public ResponseEntity activateAccount(@PathVariable Integer patientId) {
        patientService.changeAccountState(patientId, true);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/deactivateAccount/{patientId}")
    public ResponseEntity deactivateAccount(@PathVariable Integer patientId) {
        patientService.changeAccountState(patientId, false);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PATIENT')")
    @GetMapping(value = "/allVisits/{patientAccountId}")
    public ResponseEntity getAllPatientVisits(@PathVariable int patientAccountId) {
        return new ResponseEntity<>(patientService.getAllPatientVisits(patientAccountId), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PATIENT')")
    @PostMapping(value = "/availableVisits")
    public ResponseEntity getAvailableVisitsInOneDay(@RequestBody GetAvailableVisitsDto availableVisitsDto) {
        List<AvailableVisitsListDto> availableVisitsListDtos = patientService.getAvailableVisits(availableVisitsDto);
        return new ResponseEntity<>(availableVisitsListDtos, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PATIENT')")
    @PostMapping(value = "/reserveVisit")
    public ResponseEntity reserveVisit(@RequestBody ReserveVisitDto reserveVisitDto) {
        patientService.reserveVisit(reserveVisitDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('PATIENT', 'DOCTOR')")
    @GetMapping(value = "/cancelVisit/{visitId}")
    public ResponseEntity cancelVisit(@PathVariable int visitId) {
        patientService.cancelVisit(visitId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PATIENT')")
    @GetMapping(value = "/downloadPdf/{visitId}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity downloadPdf(@PathVariable int visitId) {
        ByteArrayOutputStream pdf = patientService.downloadPdf(visitId);
        return ResponseEntity.ok().
                contentType(MediaType.APPLICATION_PDF)
                .body(pdf.toByteArray());
    }
}
