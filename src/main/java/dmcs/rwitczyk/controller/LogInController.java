package dmcs.rwitczyk.controller;

import dmcs.rwitczyk.dto.LoginDto;
import dmcs.rwitczyk.dto.TokenResponse;
import dmcs.rwitczyk.services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class LogInController {

    private LoginService loginService;

    @Autowired
    public LogInController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping(value = "/login", produces = "application/json")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginDto logInDto) {
        TokenResponse response = loginService.login(logInDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
