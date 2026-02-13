package Reports;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class SendReport {

    public static void sendEmailWithReport(String[] toEmails, String filePath) {
        final String fromEmail = "deepak.kumar1@magicbricks.com";
        final String password = "vzgy yjss nveo egto"; // App-specific password

        String timeStamp = new SimpleDateFormat("dd-MMM-yyyy HH:mm").format(new Date());
        String subject = "PaidOwner_B2C Automation_WEB Test Report – " + timeStamp;
        String bodyText = "Hi,\n\nThis is a B2C_PaidOwner_WEB Automation test report.\nPFA.\n\nThanks.\nB2C Automation Team\n\n**This is an auto-generated email. Please do not reply to this message.";

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail, "Paid Owner WEB Automation"));
            InternetAddress[] recipients = new InternetAddress[toEmails.length];
            for (int i = 0; i < toEmails.length; i++) {
                recipients[i] = new InternetAddress(toEmails[i]);
            }

            message.setRecipients(Message.RecipientType.TO, recipients);

            
            message.setSubject(subject);

            // Body
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(
            	    bodyText.replace("\n", "<br>"),
            	    "text/html"
            	);


            // Attachment
            MimeBodyPart attachmentPart = new MimeBodyPart();
            File file = new File(filePath);
            if (!file.exists()) {
                System.out.println("File not found: " + filePath);
                return;
            }

            DataSource source = new FileDataSource(file);
            attachmentPart.setDataHandler(new DataHandler(source));
            attachmentPart.setFileName(file.getName());

            // Combine
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            multipart.addBodyPart(attachmentPart);

            message.setContent(multipart);

            Transport.send(message);
            System.out.println(
            	    "✅ B2C_PaidOwner WEB Sanity Report sent successfully to " 
            	    + Arrays.toString(toEmails)
            	);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
