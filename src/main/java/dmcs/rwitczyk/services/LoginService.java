package dmcs.rwitczyk.services;

import dmcs.rwitczyk.domains.UserLoginDataEntity;
import dmcs.rwitczyk.dto.LoginDto;
import dmcs.rwitczyk.dto.TokenResponse;
import dmcs.rwitczyk.exceptions.AccountNotFoundException;
import dmcs.rwitczyk.repository.UserLoginDataRepository;
import dmcs.rwitczyk.security.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@Slf4j
public class LoginService {

    private UserLoginDataRepository userLoginDataRepository;

    private AuthenticationManager authenticationManager;

    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    public LoginService(UserLoginDataRepository userLoginDataRepository, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.userLoginDataRepository = userLoginDataRepository;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse login(LoginDto loginDto) {
        UserLoginDataEntity user = userLoginDataRepository.findUserLoginDataEntityByEmail(loginDto.getEmail());

        if (user == null) {
            log.info("Nie znaleziono usera");
            throw new AccountNotFoundException("Nie znaleziono usera");
        }

        if (!passwordEncoder().matches(loginDto.getPassword(), user.getPassword())) {
            log.info("Nieprawidlowe hasło");
            throw new AccountNotFoundException("Nieprawidlowe hasło");
        }

        if (!user.isEnabled()) {
            log.info("Konto nieaktywne!");
            throw new AccountNotFoundException("Konto nieaktywne!");
        }

//        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));

        TokenResponse response = new TokenResponse();
        response.setToken(jwtTokenProvider.createToken(user));
        return response;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
