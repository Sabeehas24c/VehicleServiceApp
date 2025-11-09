public class SendTest {
    public static void main(String[] args) {
        try {
            // SMTP configuration for Gmail
            String smtpHost = "smtp.gmail.com";
            int smtpPort = 587;

            // Replace these with your Gmail and App Password
            String username = "ssabeeha2006@gmail.com"; // 👉 your Gmail address
            String password = "mnhq xngh bhfq dlll"; // 👉 your App Password (not normal password)

            // Recipient email (can be same as your Gmail for testing)
            String toEmail = "ssabeeha2006@gmail.com"; 

            // Email content
            String subject = "Test Email from Java";
            String body = "Hello! This is a test email sent from my Java project.";

            // Send the email
            EmailUtil.sendEmail(smtpHost, smtpPort, username, password, username, toEmail, subject, body);

            System.out.println("✅ Email sent successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
