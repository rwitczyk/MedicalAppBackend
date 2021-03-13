package dmcs.rwitczyk.security;

import dmcs.rwitczyk.repository.UserLoginDataRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider(Mockito.mock(UserLoginDataRepository.class));
    }

    @Test
    void shouldReturnTokenFromHeader() {
        // given
        String tokenBearer = "Bearer 1234";
        String expectedToken = "1234";

        // when
        String result = jwtTokenProvider.getTokenFromHeader(tokenBearer);

        // then
        Assert.isTrue(expectedToken.equals(result));
    }

    @Test
    void shouldReturnNullWhenHeaderHasNotBearer() {
        // given
        String tokenBearer = "Beare2r 1234";

        // when
        String result = jwtTokenProvider.getTokenFromHeader(tokenBearer);

        // then
        Assert.isNull(result);
    }

    @Test
    void shouldReturnTrueWhenTokenIsValidWithSecretKey() {
        // given
        String token = Jwts.builder().setId("123").signWith(SignatureAlgorithm.HS256, "SECRET_KEY").compact();

        // when
        boolean result = jwtTokenProvider.validateTokenWithSecretKey(token);

        // then
        Assert.isTrue(result);
    }

    @Test
    void shouldThrowExceptionWhenTokenIsSignedWithIncorrectKey() {
        // given
        String token = Jwts.builder().setId("123").signWith(SignatureAlgorithm.HS256, "TEST").compact();

        // when
        try {
            jwtTokenProvider.validateTokenWithSecretKey(token);
            Assertions.fail();
        } catch (RuntimeException e) { // then
            Assert.isTrue(e.getMessage().equals("Expired or invalid JWT token"));
        }
    }
}
