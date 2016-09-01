package com.lora.mail.api;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.CommandMap;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.activation.MailcapCommandMap;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import com.lora.mail.util.AES256;
import com.lora.mail.util.Configuration;
import com.lora.mail.util.StringUtilsExtends;

/**
 * 简单邮件（以HTML带附件的邮件）发送器
 */
public class SimpleMailSender implements IMailSender {

	private final static String EMAIL_HOST = Configuration
			.getConfigValue("mail.serverHost");

	private final static String EMAIL_PORT = Configuration
			.getConfigValue("mail.serverPort");

	private final static String EMAIL_USERNAME = Configuration
			.getConfigValue("mail.userName");

	private final static String EMAIL_PASSWORD = Configuration
			.getConfigValue("mail.password");

	private final static String EMAIL_FROM_ADDRESS = Configuration
			.getConfigValue("mail.fromAddress");
	

	/**
	 * 以文本格式发送邮件
	 * 
	 * @param toAddress
	 *            收件人列表
	 * @param subject
	 *            标题
	 * @param content
	 *            内容
	 */
	public void sendTextMail(List<String> toAddress, String subject,
			String content) {
		this.sendMail(toAddress, subject, content, true, null);
	}

	/**
	 * 以HTML格式发送邮件
	 * 
	 * @param toAddress
	 *            收件人列表
	 * @param subject
	 *            标题
	 * @param content
	 *            内容
	 */
	public void sendHtmlMail(List<String> toAddress, String subject,
			String content) {
		this.sendMail(toAddress, subject, content, false, null);
	}

	/**
	 * 发邮件
	 * 
	 * @param toAddress
	 *            收件人列表
	 * @param subject
	 *            标题
	 * @param content
	 *            内容
	 * @param textType
	 *            ：true是text类型发送，false是html类型发送
	 */
	public void sendMail(List<String> toAddress, String subject,
			String content, boolean textType, String[] attachFileNames) {
		String strAddress = "";
		try {
			MailSenderInfo mailInfo = new MailSenderInfo();
			mailInfo.setMailServerHost(EMAIL_HOST);
			mailInfo.setMailServerPort(EMAIL_PORT);
			mailInfo.setFromAddress(EMAIL_FROM_ADDRESS);
			mailInfo.setUserName(EMAIL_USERNAME);
			mailInfo.setPassword(new AES256().decrypt(EMAIL_PASSWORD));

			mailInfo.setValidate(true);

			mailInfo.setToAddress(toAddress);
			mailInfo.setSubject(subject);
			mailInfo.setContent(content);

			if (null != toAddress && !toAddress.isEmpty()) {
				strAddress = StringUtilsExtends.join(toAddress, ",");
			}
			
			if (textType) {
				this.sendTextMail(mailInfo);
			} else {
				mailInfo.setAttachFileNames(attachFileNames);
				this.sendHtmlMail(mailInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 以文本格式发送邮件
	 * 
	 * @param mailInfo
	 *            待发送的邮件的信息
	 */
	public void sendTextMail(MailSenderInfo mailInfo) {
		// 判断是否需要身份认证
		MyAuthenticator authenticator = null;
		Properties pro = mailInfo.getProperties();
		if (mailInfo.isValidate()) {
			// 如果需要身份认证，则创建一个密码验证器
			authenticator = new MyAuthenticator(mailInfo.getUserName(),
					mailInfo.getPassword());
		}
		// 根据邮件会话属性和密码验证器构造一个发送邮件的session
	/*	Session sendMailSession = Session
				.getDefaultInstance(pro, authenticator);*/
		Session sendMailSession = Session.getInstance(pro, authenticator);
		try {
			// 根据session创建一个邮件消息
			Message mailMessage = new MimeMessage(sendMailSession);
			// 创建邮件发送者地址
			Address from = new InternetAddress(mailInfo.getFromAddress());
			// 设置邮件消息的发送者
			mailMessage.setFrom(from);
			// 创建邮件的接收者地址，并设置到邮件消息中
			List<String> toList = mailInfo.getToAddress();
			if (null != toList && !toList.isEmpty()) {
				Address[] tos = new Address[toList.size()];
				Address to = null;
				int count = 0;
				for (String str : toList) {
					to = new InternetAddress(str);
					tos[count] = to;
					count++;
				}
				mailMessage.setRecipients(Message.RecipientType.TO, tos);
				// 设置邮件消息的主题
				mailMessage.setSubject(mailInfo.getSubject());
				// 设置邮件消息发送的时间
				mailMessage.setSentDate(new Date());
				// 设置邮件消息的主要内容
				String mailContent = mailInfo.getContent();
				mailMessage.setText(mailContent);
				MailcapCommandMap mc = (MailcapCommandMap) CommandMap
						.getDefaultCommandMap();
				mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
				mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
				mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
				mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
				mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
				CommandMap.setDefaultCommandMap(mc);
				// 发送邮件
				Transport.send(mailMessage);
			} else {
				throw new RuntimeException("邮件接收人不能为空！");
			}
		} catch (MessagingException ex) {
			throw new RuntimeException(ex.getMessage(), ex);
		} catch (Exception ex) {
			throw new RuntimeException(ex.getMessage(), ex);
		}
	}

	/**
	 * 以HTML格式发送邮件
	 * 
	 * @param mailInfo
	 *            待发送的邮件信息
	 */
	public void sendHtmlMail(MailSenderInfo mailInfo) {
		// 判断是否需要身份认证
		MyAuthenticator authenticator = null;
		Properties pro = mailInfo.getProperties();
		// 如果需要身份认证，则创建一个密码验证器
		if (mailInfo.isValidate()) {
			authenticator = new MyAuthenticator(mailInfo.getUserName(),
					mailInfo.getPassword());
		}
		// 根据邮件会话属性和密码验证器构造一个发送邮件的session
		/*Session sendMailSession = Session
				.getDefaultInstance(pro, authenticator);//会报 Access to default session denied错*/
		Session sendMailSession = Session.getInstance(pro, authenticator);
		try {
			// 根据session创建一个邮件消息
			Message mailMessage = new MimeMessage(sendMailSession);
			// 创建邮件发送者地址
			Address from = new InternetAddress(mailInfo.getFromAddress());
			// 设置邮件消息的发送者
			mailMessage.setFrom(from);
			// 创建邮件的接收者地址，并设置到邮件消息中
			List<String> toList = mailInfo.getToAddress();
			if (null != toList && !toList.isEmpty()) {
				Address[] tos = new Address[toList.size()];
				Address to = null;
				int count = 0;
				for (String str : toList) {
					to = new InternetAddress(str);
					tos[count] = to;
					count++;
				}
				// Message.RecipientType.TO属性表示接收者的类型为TO
				// mailMessage.setRecipient(Message.RecipientType.TO, to);
				mailMessage.setRecipients(Message.RecipientType.TO, tos);
				// 设置邮件消息的主题
				mailMessage.setSubject(mailInfo.getSubject());
				// 设置邮件消息发送的时间
				mailMessage.setSentDate(new Date());
				// MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
				Multipart mainPart = new MimeMultipart();
				// 创建一个包含HTML内容的MimeBodyPart
				BodyPart html = new MimeBodyPart();
				// 设置HTML内容
				html.setContent(mailInfo.getContent(),
						"text/html; charset=utf-8");
				mainPart.addBodyPart(html);

				// 向Multipart添加附件
				String[] attachFileNames = mailInfo.getAttachFileNames();
				if (null != attachFileNames && attachFileNames.length > 0) {
					for (String fileName : attachFileNames) {
						if (StringUtilsExtends.isBlank(fileName)) {
							continue;
						}
						MimeBodyPart mbpFile = new MimeBodyPart();
						FileDataSource fds = new FileDataSource(fileName);
						mbpFile.setDataHandler(new DataHandler(fds));
						try {
							// 乱码
							mbpFile.setFileName(MimeUtility.encodeWord(fds
									.getName()));
							/**
							 * 乱码 String filename= new
							 * String(fds.getName().getBytes(),"ISO-8859-1");
							 * mbpFile.setFileName(filename);
							 */
							// 向MimeMessage添加（Multipart代表附件）
							mainPart.addBodyPart(mbpFile);
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}catch (Exception e) {
							e.printStackTrace();
						}
					}
				}

				mailInfo.setAttachFileNames(null);

				// 将MiniMultipart对象设置为邮件内容
				mailMessage.setContent(mainPart);
				MailcapCommandMap mc = (MailcapCommandMap) CommandMap
						.getDefaultCommandMap();
				mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
				mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
				mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
				mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
				mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
				CommandMap.setDefaultCommandMap(mc);
				// 发送邮件
				Transport.send(mailMessage);
			} else {
				throw new RuntimeException("邮件接收人不能为空！");
			}
		} catch (MessagingException ex) {
			throw new RuntimeException(ex.getMessage(), ex);
		} catch (Exception ex) {
			throw new RuntimeException(ex.getMessage(), ex);
		}
	}

	public void sendHtmlMail(List<String> toAddress, String subject,
			String content, String[] attachFileNames) {
		this.sendMail(toAddress, subject, content, false, attachFileNames);
	}

	public void sendTextMailAsyn(MailSenderInfo mailInfo) {
		new AsynTextMailSender(mailInfo).start();
	}

	public void sendHtmlMailAsyn(MailSenderInfo mailInfo) {
		new AsynHtmlMailSender(mailInfo).start();
	}

	class AsynTextMailSender extends Thread {
		private MailSenderInfo mailInfo;

		AsynTextMailSender(MailSenderInfo mailInfo) {
			this.mailInfo = mailInfo;
		}

		@Override
		public void run() {
			SimpleMailSender sender = new SimpleMailSender();
			sender.sendTextMail(mailInfo);
		}
	}

	class AsynHtmlMailSender extends Thread {
		private MailSenderInfo mailInfo;

		AsynHtmlMailSender(MailSenderInfo mailInfo) {
			this.mailInfo = mailInfo;
		}

		@Override
		public void run() {
			SimpleMailSender sender = new SimpleMailSender();
			sender.sendHtmlMail(mailInfo);
		}
	}

}
