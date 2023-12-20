package com.example.appemail;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;
import javafx.scene.control.TextArea;

/**
 * Класс EmailSender используется для отправки электронных писем через SMTP.
 */
public class EmailSender {

    private String username;
    private String password;

    /**
     * Конструктор класса EmailSender.
     *
     * @param username Имя пользователя для SMTP сервера.
     * @param password Пароль для SMTP сервера.
     */

    public EmailSender(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Отправляет электронное письмо.
     *
     * @param toEmail Адрес электронной почты получателя.
     * @param subject Тема письма.
     * @param body Текст письма.
     * @param outputArea TextArea для вывода информации об отправке.
     */
    public void sendEmail(String toEmail, String subject, String body, TextArea outputArea) {
        Properties properties = setupMailProperties();
        Session session = createSession(properties);
        try {
            Message message = createEmailMessage(session, toEmail, subject, body);
            Transport.send(message);
            outputArea.appendText("Письмо успешно отправлено на почту:" + " " + toEmail + "\n" );
        } catch (MessagingException e) {
            handleMessagingException(e, outputArea);
        }
    }

    /**
     * Обрабатывает исключения, связанные с отправкой сообщений.
     *
     * @param e Исключение MessagingException.
     * @param outputArea TextArea для вывода сообщения об ошибке.
     */
    private void handleMessagingException(MessagingException e, TextArea outputArea) {
        if (e.getMessage().contains("535-5.7.8")) {
            outputArea.appendText("Ошибка аутентификации:Проверьте логин и пароль для SMTP-сервера.");
        } else {
            outputArea.appendText("Ошибка отправки сообщения:" + " " + e.getMessage());
        }
    }

    /**
     * Настраивает свойства для подключения к SMTP серверу.
     *
     * @return Объект Properties с настройками.
     */
    private Properties setupMailProperties() {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        return properties;
    }

    /**
     * Создает сессию для отправки электронных писем.
     *
     * @param properties Настройки для создания сессии.
     * @return Сессия для отправки электронных писем.
     */
    private Session createSession(Properties properties) {
        return Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }

    /**
     * Создает объект сообщения электронной почты.
     *
     * @param session Сессия для отправки сообщения.
     * @param toEmail Адрес электронной почты получателя.
     * @param subject Тема письма.
     * @param body Текст письма.
     * @return Объект Message, готовый к отправке.
     * @throws MessagingException Если происходит ошибка при создании сообщения.
     */
    private Message createEmailMessage(Session session, String toEmail, String subject, String body)
            throws MessagingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        message.setSubject(subject);
        message.setText(body);
        return message;
    }
}
