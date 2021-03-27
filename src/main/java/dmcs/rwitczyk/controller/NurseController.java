package dmcs.rwitczyk.controller;

import dmcs.rwitczyk.dto.AddNurseAccountDto;
import dmcs.rwitczyk.services.NurseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/nurse")
@CrossOrigin(origins = "http://localhost:4200")
public class NurseController {

    @Autowired
    private NurseService nurseService;

    @PostMapping
    public ResponseEntity<?> addNurseAccount(@RequestBody AddNurseAccountDto addNurseAccountDto) {
        nurseService.addNurseAccount(addNurseAccountDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping()
    public ResponseEntity getAllNurses() {
        return new ResponseEntity<>(nurseService.getAllNurses(), HttpStatus.OK);
    }


}
