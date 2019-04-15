package com.fhzm.run.test;

import java.util.Calendar;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message.RecipientType;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class mail {
	public static void main(String[] args) throws Exception{
		
		String email = "1124732508@qq.com";
		int random = (int)(Math.random()*1000000.0);
		String code = String.format("%06d", random);
		String emailSmtpHost = "smtp.163.com";
		String emailFromPeo = "bjbyja@163.com";
		String emailFromPassword = "bjbyja163";
		String emailFromCode = "text/html;charset=UTF-8";
		String emailSubject = "bjbyja163";
		
		// 第一步：配置javax.mail.Session对象  
		System.out.println("为" + emailSmtpHost + "配置mail session对象");  
		Properties props = new Properties();  
		props.put("mail.smtp.host", emailSmtpHost);  
		props.put("mail.smtp.starttls.enable","false");//使用 STARTTLS安全连接  
		props.put("mail.smtp.auth", "true");
		props.put("mail.debug", "false");
		// 设置发送邮件的邮件服务器的属性（这里使用网易的smtp服务器）
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        // 需要经过授权，也就是有户名和密码的校验，这样才能通过验证（一定要有这一条）
        props.setProperty("mail.smtp.port", "465");
        props.setProperty("mail.smtp.socketFactory.port", "465");
		// 使用验证  
		//props.put("mail.debug", "true");
		//props.setProperty("mail.smtp.ssl.enable", "true");  //支持ssl
		Session mailSession = Session.getInstance(props,new MyAuthenticator(emailFromPeo,emailFromPassword));  
		// 第二步：编写消息  

		InternetAddress fromAddress = new InternetAddress(emailFromPeo);  
		InternetAddress toAddress = new InternetAddress(email);  

		MimeMessage message = new MimeMessage(mailSession);  

		message.setFrom(fromAddress);  
		message.addRecipient(RecipientType.TO, toAddress);  

		message.setSentDate(Calendar.getInstance().getTime());  
		message.setSubject(emailSubject);  
		message.setContent("神州云创验证码："+code, emailFromCode);  

		// 第三步：发送消息  
		Transport transport = mailSession.getTransport("smtp");  
 //  transport.connect(smtpHost,from, fromUserPassword);  
		transport.send(message, message.getRecipients(RecipientType.TO));  
		
	}
}

class MyAuthenticator extends Authenticator{  
    String userName="";  
    String password="";  
    public MyAuthenticator(){  
          
    }  
    public MyAuthenticator(String userName,String password){  
        this.userName=userName;  
        this.password=password;  
    }  
     protected PasswordAuthentication getPasswordAuthentication(){     
        return new PasswordAuthentication(userName, password);     
     }   
}
