package com.blackbeard.admin.util.mail;

/**   
 * 简单邮件（不带附件的邮件）发送器   
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.blackbeard.web.controller.BaseController;
import com.sun.mail.util.MailSSLSocketFactory;

public class SimpleMailSender extends BaseController {
	private static String htmlPath = "resources/xml/Partner2.html";

	/**
	 * 以文本格式发送邮件
	 * 
	 * @param mailInfo
	 *            待发送的邮件的信息
	 */
	public boolean sendTextMail(MailSenderInfo mailInfo) throws Exception {
		// 判断是否需要身份认证
		MyAuthenticator authenticator = null;
		Properties pro = mailInfo.getProperties();
		if (mailInfo.isValidate()) {
			// 如果需要身份认证，则创建一个密码验证器
			authenticator = new MyAuthenticator(mailInfo.getUserName(),
					mailInfo.getPassword());
		}
		// 根据邮件会话属性和密码验证器构造一个发送邮件的session
		Session sendMailSession = Session
				.getDefaultInstance(pro, authenticator);
		logBefore(logger, "构造一个发送邮件的session");
		String host = mailInfo.getMailServerHost();
		String sendAccount = mailInfo.getFromAddress();
		String acceptAccount = mailInfo.getToAddress();
		String mailKey = mailInfo.getPassword();
		Properties prop = new Properties();
		// 下面2句很关键 解决了不能持续发送邮件的问题
		MailSSLSocketFactory sf = new MailSSLSocketFactory();
		sf.setTrustAllHosts(true);
		prop.put("mail.smtp.host", mailInfo.getMailServerHost());
		prop.put("mail.smtp.port", mailInfo.getMailServerPort());
		prop.put("mail.transport.protocol", "smtp");
		prop.put("mail.smtp.auth", true);
		prop.put("mail.smtp.ssl.enable", "true");
		prop.put("mail.smtp.ssl.socketFactory", sf);
		Session session = Session.getInstance(prop);
		// 开启Session的debug模式，这样就可以查看到程序发送Email的运行状态
		session.setDebug(true);
		// 2、通过session得到transport对象
		Transport ts = session.getTransport();
		// 3、使用邮箱的用户名和密码连上邮件服务器，发送邮件时，发件人需要提交邮箱的用户名和密码给smtp服务器，用户名和密码都通过验证之后才能够正常发送邮件给收件人。
		ts.connect(host, sendAccount, mailKey);

		// 4、创建邮件
		Message message = createSimpleMail(session, mailInfo);
		// 5、发送邮件
		ts.sendMessage(message, message.getAllRecipients());
		System.out.println("---发送成功---");
		ts.close();

		/*
		 * // 根据session创建一个邮件消息 Message mailMessage = new
		 * MimeMessage(sendMailSession); // 创建邮件发送者地址 Address from = new
		 * InternetAddress(mailInfo.getFromAddress()); // 设置邮件消息的发送者
		 * mailMessage.setFrom(from); // 创建邮件的接收者地址，并设置到邮件消息中 Address to = new
		 * InternetAddress(mailInfo.getToAddress());
		 * mailMessage.setRecipient(Message.RecipientType.TO,to); // 设置邮件消息的主题
		 * mailMessage.setSubject(mailInfo.getSubject()); // 设置邮件消息发送的时间
		 * mailMessage.setSentDate(new Date()); // 设置邮件消息的主要内容 String
		 * mailContent = mailInfo.getContent();
		 * mailMessage.setText(mailContent); // 发送邮件
		 * Transport.send(mailMessage);
		 */
		logBefore(logger, "发送成功！");
		return true;
	}

	/**
	 * @Method: createSimpleMail
	 * @Description: 创建一封只包含文本的邮件
	 * @Anthor:刘博
	 *
	 * @param session
	 * @return
	 * @throws Exception
	 */
	public static MimeMessage createSimpleMail(Session session,MailSenderInfo mailInfo)
			throws Exception {
		// 创建邮件对象
		MimeMessage message = new MimeMessage(session);
		// 指明邮件的发件人
		message.setFrom(new InternetAddress(mailInfo.getFromAddress()));
		// 指明邮件的收件人，现在发件人和收件人是一样的，那就是自己给自己发
		// String acceptAccount = taskMap.get("to");正式使用时候用，先用测试账号
		message.setRecipient(Message.RecipientType.TO, new InternetAddress(
				mailInfo.getToAddress()));
		// 邮件的标题
		message.setSubject(mailInfo.getSubject());

		// 向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
		Multipart multipart = new MimeMultipart();
		// 设置邮件的文本内容
		BodyPart contentPart = new MimeBodyPart();
		// String cont = readHTML(htmlPath);
		// contentPart.setText(cont);
		contentPart.setContent(mailInfo.getContent(), "text/html; charset=gbk");
		multipart.addBodyPart(contentPart);

		// ---------------------附件开始------------------------
		/*
		 * BodyPart messageBodyPart = new MimeBodyPart(); DataSource source =
		 * new FileDataSource("f:\\Partner.pdf"); // 添加附件的内容
		 * messageBodyPart.setDataHandler(new DataHandler(source)); //
		 * 保证你的中文附件标题名在发送时不会变成乱码:MimeUtility.encodeText
		 * messageBodyPart.setFileName(MimeUtility.encodeText("Partner.pdf"));
		 * multipart.addBodyPart(messageBodyPart);
		 */
		// ---------------------附件结束------------------------

		// 将multipart对象放到message中
		message.setContent(multipart);
		// 返回创建好的邮件对象
		return message;
	}

	// 从html模板中读取邮件内容

	public static String readHTML(String spath) throws IOException {

		InputStreamReader isReader = null;

		BufferedReader bufReader = null;

		StringBuffer buf = new StringBuffer();

		try {

			File file = new File(spath);

			isReader = new InputStreamReader(new FileInputStream(file), "gbk");

			bufReader = new BufferedReader(isReader, 1);

			String data;

			while (bufReader.readLine() != null) {
				data = bufReader.readLine();
				buf.append(data);
			}

		} catch (Exception e) {

			// TODO 处理异常

		} finally {

			// TODO 关闭流

			isReader.close();

			bufReader.close();

		}

		return buf.toString();

	}

	/**
	 * 以HTML格式发送邮件
	 * 
	 * @param mailInfo
	 *            待发送的邮件信息
	 */
	public boolean sendHtmlMail(MailSenderInfo mailInfo) throws Exception {
		// 判断是否需要身份认证
		MyAuthenticator authenticator = null;
		Properties pro = mailInfo.getProperties();
		// 如果需要身份认证，则创建一个密码验证器
		if (mailInfo.isValidate()) {
			authenticator = new MyAuthenticator(mailInfo.getUserName(),
					mailInfo.getPassword());
		}
		// 根据邮件会话属性和密码验证器构造一个发送邮件的session
		Session sendMailSession = Session
				.getDefaultInstance(pro, authenticator);

		// 根据session创建一个邮件消息
		Message mailMessage = new MimeMessage(sendMailSession);
		// 创建邮件发送者地址
		Address from = new InternetAddress(mailInfo.getFromAddress());
		// 设置邮件消息的发送者
		mailMessage.setFrom(from);
		// 创建邮件的接收者地址，并设置到邮件消息中
		Address to = new InternetAddress(mailInfo.getToAddress());
		// Message.RecipientType.TO属性表示接收者的类型为TO
		mailMessage.setRecipient(Message.RecipientType.TO, to);
		// 设置邮件消息的主题
		mailMessage.setSubject(mailInfo.getSubject());
		// 设置邮件消息发送的时间
		mailMessage.setSentDate(new Date());
		// MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
		Multipart mainPart = new MimeMultipart();
		// 创建一个包含HTML内容的MimeBodyPart
		BodyPart html = new MimeBodyPart();
		// 设置HTML内容
		html.setContent(mailInfo.getContent(), "text/html; charset=utf-8");
		mainPart.addBodyPart(html);
		// 将MiniMultipart对象设置为邮件内容
		mailMessage.setContent(mainPart);
		// 发送邮件
		Transport.send(mailMessage);
		return true;
	}

	/*
	 * @title:标题
	 * 
	 * @content:内容
	 * 
	 * @type:类型,1:文本格式;2:html格式
	 * 
	 * @tomail:接收的邮箱
	 */
	public boolean sendMail(String title, String content, String type,
			String tomail) throws Exception {

		// 这个类主要是设置邮件
		MailSenderInfo mailInfo = new MailSenderInfo();
		mailInfo.setMailServerHost("smtp.qq.com");
		mailInfo.setMailServerPort("25");
		mailInfo.setValidate(true);
		mailInfo.setUserName("itfather@1b23.com");
		mailInfo.setPassword("tttt");// 您的邮箱密码
		mailInfo.setFromAddress("itfather@1b23.com");
		mailInfo.setToAddress(tomail);
		mailInfo.setSubject(title);
		mailInfo.setContent(content);
		// 这个类主要来发送邮件

		SimpleMailSender sms = new SimpleMailSender();

		if ("1".equals(type)) {
			return sms.sendTextMail(mailInfo);// 发送文体格式
		} else if ("2".equals(type)) {
			return sms.sendHtmlMail(mailInfo);// 发送html格式
		}
		return false;
	}

	/**
	 * @param SMTP
	 *            邮件服务器
	 * @param PORT
	 *            端口
	 * @param EMAIL
	 *            本邮箱账号
	 * @param PAW
	 *            本邮箱密码
	 * @param toEMAIL
	 *            对方箱账号
	 * @param TITLE
	 *            标题
	 * @param CONTENT
	 *            内容
	 * @param TYPE
	 *            1：文本格式;2：HTML格式
	 */
	public static void sendEmail(String SMTP, String PORT, String EMAIL,
			String PAW, String toEMAIL, String TITLE, String CONTENT,
			String TYPE) throws Exception {

		// 这个类主要是设置邮件
		MailSenderInfo mailInfo = new MailSenderInfo();

		mailInfo.setMailServerHost(SMTP);
		mailInfo.setMailServerPort(PORT);
		mailInfo.setValidate(true);
		mailInfo.setUserName(EMAIL);
		mailInfo.setPassword(PAW);
		mailInfo.setFromAddress(EMAIL);
		mailInfo.setToAddress(toEMAIL);
		mailInfo.setSubject(TITLE);
		mailInfo.setContent(CONTENT);
		// 这个类主要来发送邮件

		SimpleMailSender sms = new SimpleMailSender();

		if ("1".equals(TYPE)) {
			sms.sendTextMail(mailInfo);
		} else {
			sms.sendHtmlMail(mailInfo);
		}

	}

}
