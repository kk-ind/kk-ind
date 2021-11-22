package utilities;

import java.io.File;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import automationFramework.GenericKeywords;

public class Mailing {
	
	private static String userName = "";
	private static String passWord = "";
	private static String from = "";
	private static String mailSubject = "";
	private static String mailBody = "";
	
	
	public static void sendMail(String fileName) {
		 
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "zsmtp.hybridzimbra.com");
        props.put("mail.smtp.port", "25");
        String[] to;
        String[] cc;
        
        userName = GenericKeywords.getConfigProperty("SenderMailId");
        passWord = GenericKeywords.getConfigProperty("SenderPassword");
        from = userName;
        mailSubject = "Execution Report for "+GenericKeywords.getConfigProperty("SuiteName");
        mailBody = GenericKeywords.getConfigProperty("MailBody");
        to = GenericKeywords.getConfigProperty("to").split(";");
        cc = GenericKeywords.getConfigProperty("cc").split(";");
        
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
               protected PasswordAuthentication getPasswordAuthentication() {
                     return new PasswordAuthentication(userName, passWord);
               }
        });

        try {
               Message message = new MimeMessage(session);

               message.setFrom(new InternetAddress(from));

               for (int i = 0; i < to.length; i++) {
                     message.addRecipient(Message.RecipientType.TO, new InternetAddress(to[i]));
               }

               for (int i = 0; i < cc.length; i++) {
                     message.addRecipient(Message.RecipientType.CC, new InternetAddress(cc[i]));
               }

               message.setSubject(mailSubject);

               BodyPart messageBodyPart = new MimeBodyPart();

               messageBodyPart.setContent(mailBody,"text/html; charset=utf-8");

               Multipart multipart = new MimeMultipart();

               multipart.addBodyPart(messageBodyPart);

               messageBodyPart = new MimeBodyPart();
               DataSource source = new FileDataSource(fileName);
               messageBodyPart.setDataHandler(new DataHandler(source));
               messageBodyPart.setFileName(fileName);
               multipart.addBodyPart(messageBodyPart);

               message.setContent(multipart);
               //message.setContent(mailBody,"text/html; charset=utf-8");
               Transport.send(message);

        } catch (MessagingException e) {
               e.printStackTrace();
        }catch (Exception e)
        {
        	e.printStackTrace();
        }
        
 }
	
	public static void sendAttachmentMail(String fileName) {
		 
		String smtpHost = GenericKeywords.getConfigProperty("Smtp HostName");
		String smtpPort = GenericKeywords.getConfigProperty("Smtp Port");
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "false");
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", smtpPort);
        String[] to;
        String[] cc;
        
        userName = GenericKeywords.getConfigProperty("SenderMailId");
        passWord = GenericKeywords.getConfigProperty("SenderPassword");
        from = userName;
        mailSubject = "Execution Report for "+GenericKeywords.getConfigProperty("SuiteName");
        mailBody = GenericKeywords.getConfigProperty("MailBody");
        to = GenericKeywords.getConfigProperty("to").split(";");
        cc = GenericKeywords.getConfigProperty("cc").split(";");
        
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
               protected PasswordAuthentication getPasswordAuthentication() {
                     return new PasswordAuthentication(userName, passWord);
               }
        });

        try {
               Message message = new MimeMessage(session);

               message.setFrom(new InternetAddress(from));

               for (int i = 0; i < to.length; i++) {
            	   	GenericKeywords.writeToLogFile("INFO", "Email ID [to]: "+to[i]);
                     message.addRecipient(Message.RecipientType.TO, new InternetAddress(to[i]));
               }

               for (int i = 0; i < cc.length; i++) {
            	   GenericKeywords.writeToLogFile("INFO", "Email ID [cc]: "+cc[i]);
                     message.addRecipient(Message.RecipientType.CC, new InternetAddress(cc[i]));
               }

               message.setSubject(mailSubject);

               BodyPart messageBodyPart = new MimeBodyPart();

               messageBodyPart.setContent(mailBody,"text/html; charset=utf-8");

               Multipart multipart = new MimeMultipart();

               multipart.addBodyPart(messageBodyPart);

               messageBodyPart = new MimeBodyPart();
               File file = new File(fileName);
               if(!file.exists())
               {
            	   GenericKeywords.writeToLogFile("ERROR", "File does not exist for attachment");
            	   return;
               }
               
               DataSource source = new FileDataSource(fileName);
               messageBodyPart.setDataHandler(new DataHandler(source));
               messageBodyPart.setFileName(file.getName());
               multipart.addBodyPart(messageBodyPart);

               message.setContent(multipart);
               //message.setContent(mailBody,"text/html; charset=utf-8");
               Transport.send(message);

        } catch (MessagingException e) {
        	GenericKeywords.writeToLogFile("INFO", "Exception: "+e.toString());
               e.printStackTrace();
        }catch (Exception e)        {
        	GenericKeywords.writeToLogFile("INFO", "Exception: "+e.toString());
        }
        
 }

}
