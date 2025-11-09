// EmailUtil.java
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class EmailUtil {

    /**
     * Send an email via SMTP.
     * Signature used by your project:
     * sendEmail(String smtpHost, int smtpPort,
     *           String username, String password,
     *           String fromEmail, String toEmail,
     *           String subject, String body)
     */
    public static void sendEmail(String smtpHost, int smtpPort,
                                 String username, String password,
                                 String fromEmail, String toEmail,
                                 String subject, String body) throws MessagingException {

        // Use STARTTLS (port 587) — recommended. Timeouts added.
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.required", "true");
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", String.valueOf(smtpPort));
        props.put("mail.smtp.connectiontimeout", "10000"); // 10s
        props.put("mail.smtp.timeout", "15000"); // 15s
        props.put("mail.smtp.writetimeout", "15000");
        props.put("mail.smtp.ssl.trust", smtpHost);

        // If you want to try SSL (port 465) instead, comment the above block and
        // use the following block (and set smtpPort=465 when calling).
        /*
        Properties props = new Properties();
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.connectiontimeout", "10000");
        props.put("mail.smtp.timeout", "15000");
        props.put("mail.smtp.writetimeout", "15000");
        props.put("mail.smtp.ssl.trust", smtpHost);
        */

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        // Turn debug off to reduce console spam. Set to true only for troubleshooting.
        session.setDebug(false);

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(fromEmail));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        message.setSubject(subject);
        message.setText(body);

        Transport.send(message);
    }
}
