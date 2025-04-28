package ra.edu.utils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class SendEmail {
    public static void sendPasswordEmail(String toEmail, String password) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("theminh2005z@gmail.com", "nwzi bdsr vmev lggn");
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("theminh2005z@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Thông tin tài khoản");
            message.setText("Xin chào, \nMật khẩu đăng nhập: " + password + "\n\nVui lòng đổi mật khẩu sau khi đăng nhập!");

            Transport.send(message);
            System.out.println("Gửi email mật khẩu thành công.");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}