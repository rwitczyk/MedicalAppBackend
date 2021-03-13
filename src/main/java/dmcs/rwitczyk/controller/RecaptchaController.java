package dmcs.rwitczyk.controller;

import dmcs.rwitczyk.dto.RecaptchaTokenDto;
import dmcs.rwitczyk.services.RecaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class RecaptchaController {

    private RecaptchaService recaptchaService;

    @Autowired
    public RecaptchaController(RecaptchaService recaptchaService) {
        this.recaptchaService = recaptchaService;
    }

    @PostMapping(value = "/validateToken")
    public ResponseEntity validateRecaptcha(@RequestBody RecaptchaTokenDto recaptchaTokenDto) {
        recaptchaService.validateRecaptchaToken(recaptchaTokenDto);
        return new ResponseEntity(HttpStatus.OK);
    }
}
