package dmcs.rwitczyk.security;

import dmcs.rwitczyk.domains.UserLoginDataEntity;
import dmcs.rwitczyk.exceptions.AccountNotFoundException;
import dmcs.rwitczyk.repository.UserLoginDataRepository;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class JwtTokenProvider {

    private static final String SECRET_KEY = "SECRET_KEY";

    private UserLoginDataRepository userLoginDataRepository;

    public JwtTokenProvider(UserLoginDataRepository userLoginDataRepository) {
        this.userLoginDataRepository = userLoginDataRepository;
    }

    public String createToken(UserLoginDataEntity user) {
        return Jwts.builder()
                .claim("user_id", user.getId())
                .claim("role", user.getRoleEntity().getRole().toString())
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    String getTokenFromHeader(String header) {
        if (header != null && header.startsWith("Bearer")) {
            return header.replace("Bearer ", "");
        }
        return null;
    }

    UsernamePasswordAuthenticationToken getAuthenticationToken(String token) {
        Integer userId = (Integer) Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().get("user_id");
        Optional<UserLoginDataEntity> user = userLoginDataRepository.findById(userId);
        if (!user.isPresent()) {
            throw new AccountNotFoundException("Nie znaleziono usera o id przekazanym w tokenie, ID = " + userId);
        }

        UserDetails userDetails = User.builder()
                .username(user.get().getEmail())
                .password(user.get().getPassword())
                .authorities(user.get().getRoleEntity().getRole().toString())
                .build();


        return new UsernamePasswordAuthenticationToken(userDetails, user.get().getRoleEntity().getRole(), userDetails.getAuthorities());
    }

    boolean validateTokenWithSecretKey(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            throw new RuntimeException("Expired or invalid JWT token");
        }
    }

}
