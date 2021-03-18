package dmcs.rwitczyk.controller;

import dmcs.rwitczyk.dto.AddNurseAccountDto;
import dmcs.rwitczyk.services.NurseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/nurse")
public class NurseController {

    @Autowired
    private NurseService nurseService;

    @PostMapping
    public ResponseEntity<?> addNurseAccount(@RequestBody AddNurseAccountDto addNurseAccountDto) {
        nurseService.addNurseAccount(addNurseAccountDto);
        return ResponseEntity.ok().build();
    }
}
