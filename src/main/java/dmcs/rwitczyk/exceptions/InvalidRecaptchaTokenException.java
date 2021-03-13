package dmcs.rwitczyk.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class InvalidRecaptchaTokenException extends RuntimeException {
    public InvalidRecaptchaTokenException(String message) {
        super(message);
    }
}
