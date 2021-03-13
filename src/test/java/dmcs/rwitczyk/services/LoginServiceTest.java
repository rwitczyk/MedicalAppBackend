package dmcs.rwitczyk.services;

import dmcs.rwitczyk.domains.UserLoginDataEntity;
import dmcs.rwitczyk.dto.LoginDto;
import dmcs.rwitczyk.exceptions.AccountNotFoundException;
import dmcs.rwitczyk.repository.UserLoginDataRepository;
import dmcs.rwitczyk.security.JwtTokenProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

class LoginServiceTest {

    @Mock
    private UserLoginDataRepository userLoginDataRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    private LoginService loginService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        loginService = new LoginService(userLoginDataRepository, authenticationManager, jwtTokenProvider);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        // given
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("email");

        // when
        Mockito.when(userLoginDataRepository.findUserLoginDataEntityByEmail(Mockito.any())).thenReturn(null);

        // then
        try {
            loginService.login(loginDto);
            Assertions.fail();
        } catch (AccountNotFoundException e) {
            Assertions.assertEquals(e.getMessage(), "Nie znaleziono usera");
        }
    }

    @Test
    void shouldThrowExceptionWhenPasswordNotMatch() {
        // given
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("email");
        loginDto.setPassword("password2");

        UserLoginDataEntity userLoginDataEntity = UserLoginDataEntity.builder()
                .email("email")
                .password(new BCryptPasswordEncoder().encode("password"))
                .build();

        // when
        Mockito.when(userLoginDataRepository.findUserLoginDataEntityByEmail(Mockito.any())).thenReturn(userLoginDataEntity);

        // then
        try {
            loginService.login(loginDto);
            Assertions.fail();
        } catch (AccountNotFoundException e) {
            Assertions.assertEquals(e.getMessage(), "Nieprawidlowe hasło");
        }
    }

    @Test
    void shouldThrowExceptionWhenAccountIsNotActive() {
        // given
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("email");
        loginDto.setPassword("password");

        UserLoginDataEntity userLoginDataEntity = UserLoginDataEntity.builder()
                .email("email")
                .password(new BCryptPasswordEncoder().encode("password"))
                .enabled(false)
                .build();

        // when
        Mockito.when(userLoginDataRepository.findUserLoginDataEntityByEmail(Mockito.any())).thenReturn(userLoginDataEntity);

        // then
        try {
            loginService.login(loginDto);
            Assertions.fail();
        } catch (AccountNotFoundException e) {
            Assertions.assertEquals(e.getMessage(), "Konto nieaktywne!");
        }
    }
}
