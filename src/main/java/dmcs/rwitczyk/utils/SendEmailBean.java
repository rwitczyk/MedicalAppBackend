package dmcs.rwitczyk.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class SendEmailBean {

    private JavaMailSender javaMailSender;

    @Autowired
    public SendEmailBean(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendEmail(String email, String token) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);
        msg.setSubject("Link aktywacyjny do konta!");
        msg.setText("ePacjent \n" +
                "Kliknij link, aby aktywować konto: \n"
                + "http://localhost:4200/activateAccount/" + token);
        javaMailSender.send(msg);
    }
}
