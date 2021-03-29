package com.example.mnnu.service;

import javax.mail.MessagingException;

public interface IMailService {

    void sendMsg(String phone, String random);

    void sendSimpleMail(String to, String subject, String content);

    void sendHtmlMail(String to, String subject, String content) throws MessagingException;

    void sendAttachmentsMail(String to, String subject, String content, String filePath) throws MessagingException;

}
