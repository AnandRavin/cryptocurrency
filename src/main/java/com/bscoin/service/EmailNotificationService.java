package com.bscoin.service;

import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@SuppressWarnings("serial")
@Service
public class EmailNotificationService extends HttpServlet {
	private static final Logger LOG = LoggerFactory.getLogger(EmailNotificationService.class);

	@SuppressWarnings("unused")
	@Autowired
	private JavaMailSender javaMailService;

	@Value("${email.host}")
	private String host;

	@Value("${email.port}")
	private Integer port;

	@Value("${email.username}")
	private String username;

	@Value("${email.password}")
	private String password;

	@Value("${spring.mail.transport.protocol}")
	private String transportProtocol;

	@Value("${env}")
	private String env;

	// Supply your SMTP credentials below. Note that your SMTP credentials are
	// different from your AWS credentials.
	@Value("${smtp.username}")
	private String SMTP_USERNAME;

	@Value("${smtp.password}")
	private String SMTP_PASSWORD;

	public boolean sendEmail(String toEamilId, String subject, String content , String verificationLink) {

		LOG.info("Before sending email  ENV : " + env);
		LOG.info("host  : " + host);
		LOG.info("port  : " + port);
		LOG.info("username  : " + username);
		LOG.info("transportProtocol  : " + transportProtocol);
		// DEV Env
		if (env.equalsIgnoreCase("dev")) {
			return sendEmailDEV(toEamilId, subject, content, verificationLink);
		} else if (env.equalsIgnoreCase("prod")) {
			return sendEmailPROD(toEamilId, subject, content, verificationLink);
		} else {
			return sendEmailDEV(toEamilId, subject, content, verificationLink);
		}

	}

	private boolean sendEmailDEV(String toEamilId, String subject, String content, String verificationLink) {
		try {
			LOG.info("In  sendEmailDEV  START: " + env);

			SimpleMailMessage mail = new SimpleMailMessage();
			mail.setTo(toEamilId);
			mail.setFrom(username);
			mail.setSubject(subject);
			Properties properties = new Properties();
			properties.put("mail.smtp.host", host);
			properties.put("mail.smtp.port", port);
			properties.put("mail.smtp.auth", "true");
			properties.put("mail.smtp.starttls.enable", "true");
			properties.put("mail.smtp.ssl.trust", host);

			// creates a new session with an authenticator
			Authenticator auth = new Authenticator() {
				public PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password);
				}
			};

			Session session = Session.getInstance(properties, auth);

			// creates a new e-mail message
			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(username));
			InternetAddress[] toAddresses = { new InternetAddress(toEamilId) };
			msg.setRecipients(Message.RecipientType.TO, toAddresses);
			msg.setSubject(subject);
			msg.setSentDate(new Date());
			MimeMessageHelper helper = new MimeMessageHelper(msg, true);

			String message = "<b>Hi,</b><br><br>";
			message += "<b>Thank you for your interest in creating ViCoin wallet. Your VI wallet is created successfully & wallet address is mentioned below</b><br>";
			message += "<font color=red> Wallet Id: " + content + "</font><br>";
			message += "<b>If you have any problem, please email us to: support@virtuallakshmi.com</b><br>";
			message += "<b>Click here to login to our site : https://www.virtuallakshmi.com/virtual_lakshmi/login.php</b><br>";
			message += "<b>Thank you,</b><br>";
			message += "<b>Vi Coin team.</b><br>";
			message += "<font color=red> Please click below link for account verfication: " + verificationLink + "</font><br>";
			helper.setText(message);
			// set plain text message
			msg.setContent(message, "text/html");
			// sends the e-mail

			LOG.info("Attempting to send an email");

			Transport.send(msg);

			LOG.info("Email sent successfully !");
			LOG.info("In  sendEmailDEV  END: " + env);
			return true;
		} catch (MailException e) {
			LOG.info("Problem in sending mail sendEmailDEV  END: " + env);
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			LOG.info("Problem in sending mail sendEmailDEV  END: " + env);
			e.printStackTrace();
			return false;
		}
	}

	private boolean sendEmailPROD(String toEamilId, String subject, String content, String verificationLink) {
		Transport transport = null;
		try {
			LOG.info("In  sendEmail PROD  START: " + env);

			// Create a Properties object to contain connection configuration
			// information.
			Properties props = System.getProperties();
			props.put("mail.transport.protocol", transportProtocol);
			props.put("mail.smtp.port", port);
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.starttls.required", "true");

			// Create a Session object to represent a mail session with the
			// specified properties.
			Session session = Session.getDefaultInstance(props);
			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(username));
			// Use to send an single 'TO' Recipient
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(toEamilId));
			msg.setSubject(subject);
			msg.setContent(content, "text/html");

			/*
			 * // Add multiple 'TO' Recipient in the Email msg =
			 * addMultipleMailId(msg, cc_SendAddress, Message.RecipientType.TO);
			 * 
			 * // Add multiple 'CC' Recipient in the Email msg =
			 * addMultipleMailId(msg, cc_SendAddress, Message.RecipientType.CC);
			 */

			// Create a transport
			transport = session.getTransport();
			LOG.info("Attempting to send an email through the Amazon SES SMTP interface...");
			transport.connect(host, SMTP_USERNAME, SMTP_PASSWORD);
			transport.sendMessage(msg, msg.getAllRecipients());
			LOG.info("Email sent successfully !");
			transport.close();

			LOG.info("In  sendEmail PROD  END: " + env);
			return true;
		} catch (MailException e) {
			LOG.info("Problem in sending mail sendEmail PROD  END: " + env);
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			LOG.info("Problem in sending mail sendEmail PROD  END: " + env);
			e.printStackTrace();
			return false;
		}

	}

	public boolean sendEmailforgot(String toEamilId, String subject, String content, String verificationLink) {
		try {
			LOG.info("In  sendEmailDEV  START: " + env);

			SimpleMailMessage mail = new SimpleMailMessage();
			mail.setTo(toEamilId);
			mail.setFrom(username);
			mail.setSubject(subject);
			Properties properties = new Properties();
			properties.put("mail.smtp.host", host);
			properties.put("mail.smtp.port", port);
			properties.put("mail.smtp.auth", "true");
			properties.put("mail.smtp.starttls.enable", "true");
			properties.put("mail.smtp.ssl.trust", host);

			// creates a new session with an authenticator
			Authenticator auth = new Authenticator() {
				public PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password);
				}
			};

			Session session = Session.getInstance(properties, auth);

			// creates a new e-mail message
			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(username));
			InternetAddress[] toAddresses = { new InternetAddress(toEamilId) };
			msg.setRecipients(Message.RecipientType.TO, toAddresses);
			msg.setSubject(subject);
			msg.setSentDate(new Date());
			MimeMessageHelper helper = new MimeMessageHelper(msg, true);

			String message = "<b>Hi,</b><br><br>";
			message += "<b>Please find your login credentials for www.equacoin.com.</b><br>";
			message += "<font color=red> Your  password: " + content + "</font><br><br>";
			message += "<b>Thank you,</b><br>";
			message += "<b>EQC team.</b>";

			helper.setText(message);
			// set plain text message
			msg.setContent(message, "text/html");
			// sends the e-mail

			LOG.info("Attempting to send an email");

			Transport.send(msg);

			LOG.info("Email sent successfully !");
			LOG.info("In  sendEmailDEV  END: " + env);
			return true;
		} catch (MailException e) {
			LOG.info("Problem in sending mail sendEmailDEV  END: " + env);
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			LOG.info("Problem in sending mail sendEmailDEV  END: " + env);
			e.printStackTrace();
			return false;
		}
	}
}