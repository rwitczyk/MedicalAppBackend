package dmcs.rwitczyk.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AccountAlreadyExistsException extends RuntimeException {
    public AccountAlreadyExistsException(String s) {
        super(s);
    }
}
