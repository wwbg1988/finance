package com.blackbeard.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.apache.commons.lang.StringUtils;
import com.sun.mail.util.MailSSLSocketFactory;

/**
 * @ClassName: Sendmail
 * @Description: 发送Email
 * @author: 刘博
 * @date: 2016-5-6 下午2:53:56
 *
 */
public class SendMailUtils {
	private static String host;
	private static String sendAccount;
	private static String mailKey;
	// private static String htmlPath = "f:\\Partner2.html";
	private static String htmlPath = "resources/xml/Partner2.html";

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		Map<String, String> taskMap = new HashMap<String, String>();
		taskMap.put("env", "dev");
		sendMail(taskMap);

	}

	public static void sendMail(Map<String, String> taskMap) throws Exception {

		Properties prop = new Properties();
		String env = taskMap.get("env");
		if (StringUtils.isEmpty(env))
			return;
		// 获取配置文件名称
		String propName ="dev.properties";
		InputStream in = Object.class.getResourceAsStream(propName);
		try {

			prop.load(in);
			host = prop.getProperty("mail.smtp.host").trim();
			sendAccount = prop.getProperty("mail.sendAccount").trim();
			mailKey = prop.getProperty("mail.key").trim();

			// 使用JavaMail发送邮件的5个步骤
			// 1、创建session

			/*
			 * prop.put("mail.smtp.socketFactory.fallback", "false");
			 * prop.put("mail.smtp.starttls.enable", "true");
			 * prop.put("mail.mime.address.strict", "false");
			 */
			// 下面2句很关键 解决了不能持续发送邮件的问题
			MailSSLSocketFactory sf = new MailSSLSocketFactory();
			sf.setTrustAllHosts(true);
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
			Message message = createSimpleMail(session, taskMap, htmlPath);
			// 5、发送邮件
			ts.sendMessage(message, message.getAllRecipients());
			System.out.println("---发送成功---");
			ts.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
	public static MimeMessage createSimpleMail(Session session,
			Map<String, String> taskMap, String htmlPath) throws Exception {
		// 创建邮件对象
		MimeMessage message = new MimeMessage(session);
		// 指明邮件的发件人
		message.setFrom(new InternetAddress(session
				.getProperty("mail.sendAccount")));
		// 指明邮件的收件人，现在发件人和收件人是一样的，那就是自己给自己发
		// String acceptAccount = taskMap.get("to");正式使用时候用，先用测试账号
		message.setRecipient(Message.RecipientType.TO, new InternetAddress(
				session.getProperty("mail.acceptAccount")));
		// 邮件的标题
		message.setSubject("发送邮件");

		// 向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
		Multipart multipart = new MimeMultipart();
		// 设置邮件的文本内容
		BodyPart contentPart = new MimeBodyPart();
		String cont = readHTML(htmlPath, taskMap);
		// contentPart.setText(cont);
		contentPart.setContent("恭喜您,注册成功", "text/html; charset=gbk");
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

	public static String readHTML(String spath, Map<String, String> taskMap)
			throws IOException {

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
				data = convertData(data, taskMap);
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

	private static String convertData(String data, Map<String, String> taskMap) {
		String pwd = taskMap.get("password");
		String userName = taskMap.get("username");
		String role = taskMap.get("role");
		String currency = taskMap.get("currency");
		String companyName = taskMap.get("name");
		if (data.contains("Your account: admin@XXXXX")) {
			data = "<p>Your account:" + userName + "</p>";
		} else if (data.contains("Your default password: 123456")) {
			data = "<p>Your default password:" + pwd + "</p>";
		} else if (data.contains("用户名：admin@XXXXX")) {
			data = "<p>用户名：" + userName + "</p>";
		} else if (data.contains("初始密码：123456")) {
			data = "<p>初始密码：" + pwd + "</p>";
		} else if (data.contains("Your role on Linktour")) {
			data = "<p>Your role on Linktour：" + role + "</p>";
		} else if (data.contains("Your preferred currency")) {
			data = "<p>Your preferred currency:" + currency + "</p>";
		} else if (data.contains("Company Name")) {
			data = "<p>Congratulations tojoin Linktour!The Exclusive account for "
					+ companyName
					+ " has been created successfully on Linktourand is ready to use.</p>";
		} else if (data.contains("您目前在领拓平台的角色")) {
			data = "<p>您目前在领拓平台的角色：" + role + "</p>";
		} else if (data.contains("您所绑定的本币")) {
			data = "<p>您所绑定的本币：" + currency + "</p>";
		} else if (data.contains("公司名字")) {
			data = "<p>恭喜您成功加入领拓！我们已经为" + companyName + "成功创建领拓专属账户。</p>";
		}
		return data;
	}

}