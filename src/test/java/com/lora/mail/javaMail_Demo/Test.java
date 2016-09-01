package com.lora.mail.javaMail_Demo;

import java.util.Arrays;

import com.lora.mail.api.IMailSender;
import com.lora.mail.api.SimpleMailSender;

public class Test {

	public static void main(String[] args) {
		 String[] applyEmail = new String[]{"xxx@126.com"};
		 String subjuect = "JavaMail测试";
		 String content = "JavaMail测试";
		 IMailSender mailSender = new SimpleMailSender();
		 mailSender.sendHtmlMail(Arrays.asList(applyEmail), subjuect, content, null);
	}

	

}
