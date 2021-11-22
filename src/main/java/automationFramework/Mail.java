package automationFramework;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.AuthenticationFailedException;
import javax.mail.BodyPart;
import javax.mail.FetchProfile;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.FlagTerm;

import automationFramework.GenericKeywords;

public class Mail extends GenericKeywords {

	public String eMailBody = "";
	public String host;
	public String port;
	public String username;
	public String password;

	public Mail() {
		this.host = getConfigProperty("Host Name");
		this.port = getConfigProperty("Port Number");
		this.username = getConfigProperty("e-mail User Name");
		this.password = getConfigProperty("e-mail Password");

	}

	public void check(String host, String port, String user, String password, String subject) {
		try {

			// create properties field
			writeToLogFile("INFO","Debug");
			Properties properties = new Properties();
			final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

			properties.setProperty("mail.pop3.socketFactory.class", SSL_FACTORY);
			properties.setProperty("mail.pop3.socketFactory.fallback", "false");
			properties.setProperty("mail.pop3.socketFactory.port", port);
			properties.put("mail.pop3.host", host);
			properties.put("mail.pop3.port", port);// 995
			properties.put("mail.pop3.starttls.enable", "true");
			Session emailSession = Session.getDefaultInstance(properties);

			// create the POP3 store object and connect with the pop server
			Store store = emailSession.getStore("pop3s");

			store.connect(host, user, password);

			// create the folder object and open it
			String folder = getConfigProperty("Check Folder");
			Folder emailFolder = store.getFolder(folder);// "INBOX"
			emailFolder.open(Folder.READ_ONLY);

			// retrieve the messages from the folder in an array and print it
			Message messages[] = emailFolder.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
            writeToLogFile("INFO","No. of Unread Messages : " + messages.length);

            /* Use a suitable FetchProfile */
            FetchProfile fp = new FetchProfile();
            fp.add(FetchProfile.Item.ENVELOPE);

            fp.add(FetchProfile.Item.CONTENT_INFO);

            emailFolder.fetch(messages, fp);
            
            try

            {

                //printAllMessages(messages);
                for (int i = 0; i < messages.length; i++)
                {

                    writeToLogFile("INFO","MESSAGE #" + (i + 1) + ":");
                    Message message = messages[i];
                    
                    String subjectEmail = message.getSubject();
                    
                    if(subjectEmail.equals(subject)) {
                	
                    Address[] a;

                    // FROM

                    if ((a = message.getFrom()) != null) {
                        for (int j = 0; j < a.length; j++) {
                            writeToLogFile("INFO","FROM: " + a[j].toString());
                        }
                    }
                    // TO
                    if ((a = message.getRecipients(Message.RecipientType.TO)) != null) {
                        for (int j = 0; j < a.length; j++) {
                            writeToLogFile("INFO","TO: " + a[j].toString());
                        }
                    }
                    

                    Date receivedDate = message.getReceivedDate();
                    Date sentDate = message.getSentDate(); // receivedDate is returning null. So used getSentDate()                                                     
                  String content = message.getContent().toString();
//                    String content = getTextFromMessage(message);// to read mails that are forwarded
                    writeToLogFile("INFO","Subject : " + subject);
                    if (receivedDate != null) {
                        writeToLogFile("INFO","Received Date : " + receivedDate.toString());
                    }
                    writeToLogFile("INFO","Sent Date : " + sentDate.toString());
                    writeToLogFile("INFO","Content : " + content);
                    this.eMailBody = content;

                    }
                }

                emailFolder.close(true);
                store.close();

            }

            catch (Exception ex)

            {
                writeToLogFile("INFO","Exception arise at the time of read mail");

                ex.printStackTrace();

            }

        }

        catch(Exception e) {
        	e.printStackTrace();
        }
	}
	
	public void printAllMessages(Message[] msgs) throws Exception
    {
        for (int i = 0; i < msgs.length; i++)
        {

            writeToLogFile("INFO","MESSAGE #" + (i + 1) + ":");

            printEnvelope(msgs[i]);
        }

    }

    public void printEnvelope(Message message) throws Exception

    {

    	String subject = message.getSubject();
    	
        Address[] a;

        // FROM

        if ((a = message.getFrom()) != null) {
            for (int j = 0; j < a.length; j++) {
                writeToLogFile("INFO","FROM: " + a[j].toString());
            }
        }
        // TO
        if ((a = message.getRecipients(Message.RecipientType.TO)) != null) {
            for (int j = 0; j < a.length; j++) {
                writeToLogFile("INFO","TO: " + a[j].toString());
            }
        }
        

        Date receivedDate = message.getReceivedDate();
        Date sentDate = message.getSentDate(); // receivedDate is returning
                                                // null. So used getSentDate()

        String content = message.getContent().toString();
        writeToLogFile("INFO","Subject : " + subject);
        if (receivedDate != null) {
            writeToLogFile("INFO","Received Date : " + receivedDate.toString());
        }
        writeToLogFile("INFO","Sent Date : " + sentDate.toString());
        writeToLogFile("INFO","Content : " + content);

//        getContent(message);

    }

    public void getContent(Message msg)

    {
        try {
            String contentType = msg.getContentType();
            writeToLogFile("INFO","Content Type : " + contentType);
            Multipart mp = (Multipart) msg.getContent();
            int count = mp.getCount();
            for (int i = 0; i < count; i++) {
                dumpPart(mp.getBodyPart(i));
            }
        } catch (Exception ex) {
            writeToLogFile("INFO","Exception arise at get Content");
            ex.printStackTrace();
        }
    }

    public void dumpPart(Part p) throws Exception {
        // Dump input stream ..
        InputStream is = p.getInputStream();
        // If "is" is not already buffered, wrap a BufferedInputStream
        // around it.
        if (!(is instanceof BufferedInputStream)) {
            is = new BufferedInputStream(is);
        }
        int c;
        writeToLogFile("INFO","Message : ");
        while ((c = is.read()) != -1) {
            System.out.write(c);
        }
    }

	/**
	 * @author Mohamed
	 * @param Email Subject
	 * @param UserName
	 * @param Password
	 * @apiNote To check the received email and return the body using the given
	 *          email subject, this method uses the credentials from config sheet
	 */
	public void checkReceivedEMail(String subject) throws InterruptedException {

		Thread.sleep(12000);
		check(host, port, username, password, subject);

	}

	/**
	 * @author Mohamed
	 * @param Email Subject
	 * @param UserName
	 * @param Password
	 * @apiNote To check the received email and return the body using the given
	 *          email subject and email credentials
	 */
	public void checkReceivedEMail(String subject, String userName, String Password) throws InterruptedException {

		Thread.sleep(14000);
		
		check(host, port, userName, Password, subject);

	}
	
	public String getTextFromMessage(Message message) throws MessagingException, IOException {
		try {
			String result = "";
			if (message.isMimeType("text/plain")) {
				result = message.getContent().toString();
			} else if (message.isMimeType("multipart/*")) {
				MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
				result = getTextFromMimeMultipart(mimeMultipart);
			}
			return result;
		} catch (Exception e) {
			writeToLogFile("INFO", "Error getting text from e-mail");
			return null;
		}
	}

	public String getTextFromMimeMultipart(MimeMultipart mimeMultipart) throws MessagingException, IOException {
		try {
			String result = "";
			int count = mimeMultipart.getCount();
			for (int i = 0; i < count; i++) {
				InputStream is = mimeMultipart.getBodyPart(i).getInputStream();
				int in;
				char c;
				while ((in = is.read()) != -1) {

					// converts integer to character
					c = (char) in;

					// prints character
					System.out.print(c);
				}
				writeToLogFile("INFO",mimeMultipart.getBodyPart(i).getContentType());
				BodyPart bodyPart = mimeMultipart.getBodyPart(i);
				if (bodyPart.isMimeType("text/plain")) {
					result = result + "\n" + bodyPart.getContent();
					break; // without break same text appears twice in my tests
				} else if (bodyPart.getContent() instanceof MimeMultipart) {
					result = result + getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent());
				}
			}
			writeToLogFile("INFO", result);
			writeMailBody(result);

			return result;
		} catch (Exception e) {
			e.printStackTrace();
			writeToLogFile("INFO", "Error getting text from e-mail body");
			return null;
		}

	}

	public void writeMailBody(String emails) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("./e-mailBody.txt"));
			writer.write(emails);
			if (writer != null)
				writer.close();
		} catch (Exception e) {
			e.printStackTrace();
			writeToLogFile("INFO", "Error getting text from e-mail body");
		}
	}

}
