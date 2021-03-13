package dmcs.rwitczyk.utils;

import com.dumbster.smtp.SimpleSmtpServer;
import com.dumbster.smtp.SmtpMessage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SendEmailBeanTest {

    private SimpleSmtpServer server;

    private SendEmailBean sendEmailBean;

    @BeforeEach
    void setUp() throws IOException {
        server = SimpleSmtpServer.start(SimpleSmtpServer.AUTO_SMTP_PORT);

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("localhost");
        mailSender.setPort(server.getPort());
        sendEmailBean = new SendEmailBean(mailSender);
    }

    @AfterEach
    void tearDown() {
        server.stop();
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 3, 5, 7})
    void shouldSendEmailsNTimes(int number) {
        //given
        String email = "test@test.pl";
        String token = "1234567890";

        //when
        for (int i = 0; i < number; i++) {
            sendEmailBean.sendEmail(email, token);
        }
        List<SmtpMessage> emails = server.getReceivedEmails();

        //then
        assertThat(emails.size() == number);
    }

    @Test
    void shouldSendOneEmail() {
        //given
        String email = "test@test.pl";
        String token = "1234567890";

        //when
        sendEmailBean.sendEmail(email, token);
        List<SmtpMessage> emails = server.getReceivedEmails();

        //then
        assertThat(emails.size() == 1);
    }

    @Test
    void shouldSendThreeEmails() {
        //given
        String email1 = "1_test@test.pl";
        String email2 = "2_test@test.pl";
        String email3 = "3_test@test.pl";
        String token1 = "1_1234567890";
        String token2 = "2_1234567890";
        String token3 = "3_1234567890";

        //when
        sendEmailBean.sendEmail(email1, token1);
        sendEmailBean.sendEmail(email2, token2);
        sendEmailBean.sendEmail(email3, token3);
        List<SmtpMessage> emails = server.getReceivedEmails();

        //then
        assertThat(emails.size() == 3);
    }
}
