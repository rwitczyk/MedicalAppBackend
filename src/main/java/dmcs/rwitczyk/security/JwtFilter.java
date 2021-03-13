package dmcs.rwitczyk.security;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtFilter extends BasicAuthenticationFilter {

    private JwtTokenProvider jwtTokenProvider;

    public JwtFilter(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        super(authenticationManager);
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader("Authorization");
        String token = jwtTokenProvider.getTokenFromHeader(header);

        try {
            if (token != null && jwtTokenProvider.validateTokenWithSecretKey(token)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = jwtTokenProvider.getAuthenticationToken(token);
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken); // kontekst na czas życia requestu
            }
        } catch (RuntimeException e) {
            SecurityContextHolder.clearContext();
            response.sendError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return;
        }

        chain.doFilter(request, response);
    }

}
