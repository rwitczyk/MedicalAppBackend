package dmcs.rwitczyk.services;

import dmcs.rwitczyk.domains.PatientEntity;
import dmcs.rwitczyk.domains.UserLoginDataEntity;
import dmcs.rwitczyk.domains.VerificationToken;
import dmcs.rwitczyk.repository.PatientRepository;
import dmcs.rwitczyk.repository.UserLoginDataRepository;
import dmcs.rwitczyk.repository.VerificationTokenRepository;
import dmcs.rwitczyk.utils.SendEmailBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@Transactional
public class AccountService {

    private PatientRepository patientRepository;

    private SendEmailBean sendEmailBean;

    private VerificationTokenRepository verificationTokenRepository;

    private UserLoginDataRepository userLoginDataRepository;

    @Autowired
    public AccountService(PatientRepository patientRepository, SendEmailBean sendEmailBean, VerificationTokenRepository verificationTokenRepository, UserLoginDataRepository userLoginDataRepository) {
        this.patientRepository = patientRepository;
        this.sendEmailBean = sendEmailBean;
        this.verificationTokenRepository = verificationTokenRepository;
        this.userLoginDataRepository = userLoginDataRepository;
    }

    public void sendActivationEmail(Integer patientId) {
        log.info("Wysyłam email do pacjenta o id: " + patientId);
        Optional<PatientEntity> patientEntity = patientRepository.findById(patientId);

        if (patientEntity.isPresent()) {
            sendEmailBean.sendEmail(patientEntity.get().getUserLoginDataEntity().getEmail(), prepareToken(patientEntity.get().getUserLoginDataEntity()));
        } else {
            log.info("Nie istnieje pacjent o id: " + patientId);
        }
    }

    private String prepareToken(UserLoginDataEntity userLoginDataEntity) {
        UUID token = UUID.randomUUID();
        VerificationToken verificationToken = new VerificationToken(token, userLoginDataEntity);
        this.verificationTokenRepository.save(verificationToken);
        log.info("Zapisuje token do bazy o uuid: " + token.toString());

        return token.toString();
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void confirmAccountByLink(String token) {
        VerificationToken verificationToken = this.verificationTokenRepository.findByToken(UUID.fromString(token));
        if (verificationToken != null) {
            UserLoginDataEntity userLoginDataEntity = verificationToken.getUserLoginDataEntity();
            userLoginDataEntity.setEnabled(true);
            this.userLoginDataRepository.save(userLoginDataEntity);
            log.info("Aktywacja konta o id: " + userLoginDataEntity.getId());

            this.verificationTokenRepository.removeByToken(UUID.fromString(token));
            log.info("Usuwam token autoryzacyjny = : " + token);
        } else {
            log.info("Nie znaleziono tokenu w bazie! TOKEN: " + token);
        }
    }
}
