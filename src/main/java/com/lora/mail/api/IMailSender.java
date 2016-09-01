package com.lora.mail.api;

import java.util.List;

/*
 * 邮件发送接口
 */
public interface IMailSender {


    /**
     * 以HTML格式发送邮件
     * @param toAddress 收件人列表
	 * @param subject 标题
	 * @param content 内容
	 * @param attachments 附件完整路径
     */
    public void sendHtmlMail(List<String> toAddress,String subject,String content,String[] attachments);


}
