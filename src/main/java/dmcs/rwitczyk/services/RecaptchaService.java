package dmcs.rwitczyk.services;

import dmcs.rwitczyk.dto.RecaptchaResponse;
import dmcs.rwitczyk.dto.RecaptchaTokenDto;
import dmcs.rwitczyk.exceptions.InvalidRecaptchaTokenException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class RecaptchaService {

    private final String RECAPTCHA_SECRET_KEY = "6LfN7QAaAAAAACW3YPrTxbfsnegmENYOgKUgo2m3";

    private final String RECAPTCHA_VALIDATE_URL = "https://www.google.com/recaptcha/api/siteverify";

    public void validateRecaptchaToken(RecaptchaTokenDto recaptchaTokenDto) {
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
        param.add("secret", RECAPTCHA_SECRET_KEY);
        param.add("response", recaptchaTokenDto.getRecaptcha());
        log.info("validateRecaptchaToken - walidacja recaptchy. Token response: " + recaptchaTokenDto.getRecaptcha());
        RecaptchaResponse recaptchaResponse = restTemplate.postForObject(RECAPTCHA_VALIDATE_URL, param, RecaptchaResponse.class);

        log.info("validateRecaptchaToken - recaptcha response: " + (recaptchaResponse != null && recaptchaResponse.isSuccess()));
        if (recaptchaResponse == null || !recaptchaResponse.isSuccess()) {
            throw new InvalidRecaptchaTokenException("Nieprawidłowy token");
        }
    }
}
