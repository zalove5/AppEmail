package com.example.appemail;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * Класс EmailApplication представляет собой приложение для отправки электронной почты.
 * Он использует JavaFX для создания пользовательского интерфейса.
 */

public class EmailApplication extends Application {

    private static final Logger logger = LogManager.getLogger(EmailApplication.class);

    /**
     * Запускает приложение. Этот метод является точкой входа для приложения JavaFX.
     *
     * @param primaryStage Основная сцена приложения.
     */
    public void start(Stage primaryStage) {
        logger.info("Launching the EmailApplication application");
        setupStage(primaryStage);
        GridPane grid = createLayout();
        TextArea outputArea = createOutputArea();
        addUIComponents(grid, outputArea);
        primaryStage.setScene(new Scene(grid, 550, 300));
        primaryStage.show();
        logger.info("EmailApplication started successfully");
    }

    /**
     * Настраивает основное окно приложения.
     *
     * @param stage Основная сцена, которую необходимо настроить.
     */
    private void setupStage(Stage stage) {
        stage.setTitle("Рассылка");
        logger.debug("The Stage setup is complete.");
    }

    /**
     * Создает и возвращает основной макет приложения.
     *
     * @return GridPane, являющийся основным макетом приложения.
     */
    private GridPane createLayout() {
        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));
        logger.debug("GridPane for the interface created.");
        return grid;
    }

    /**
     * Создает и возвращает область вывода информации.
     *
     * @return TextArea, предназначенная для вывода информации.
     */
    private TextArea createOutputArea() {
        TextArea outputArea = new TextArea();
        outputArea.setEditable(false);
        logger.debug("Output TextArea created.");
        return outputArea;
    }

    /**
     * Добавляет элементы пользовательского интерфейса в основной макет.
     *
     * @param grid Основной макет приложения, в который добавляются компоненты.
     * @param outputArea Область вывода, используемая для отображения результатов.
     */
    private void addUIComponents(GridPane grid, TextArea outputArea) {
        Button displayEmailsButton = new Button("Показать информацию о письмах");
        Button displayLetterButton = new Button("Показать содержимое письма");
        Button sendEmailsButton = new Button("Отправить письма");
        TextField subjectField = new TextField();
        subjectField.setPromptText("Введите тему сообщения");

        displayEmailsButton.setOnAction(e -> displayEmailInfo(outputArea));
        displayLetterButton.setOnAction(e -> displayLetterInfo(outputArea));
        sendEmailsButton.setOnAction(e -> sendEmailsWithUserInput(subjectField.getText(), outputArea));

        grid.add(displayEmailsButton, 0, 0);
        grid.add(displayLetterButton, 1, 0);
        grid.add(new Label("Тема сообщения:"), 0, 1);
        grid.add(subjectField, 1, 1);
        grid.add(sendEmailsButton, 2, 1);
        grid.add(outputArea, 0, 2, 3, 1);
        logger.debug("UI components added to the GridPane.");
    }
    /**
     * Отображает информацию о письмах.
     *
     * @param outputArea Область вывода, в которую выводится информация о письмах.
     */
    private void displayEmailInfo(TextArea outputArea) {
        EmailReader emailReader = new EmailReader();
        emailReader.printFileContent("emails.txt", outputArea);
        int count = emailReader.countValidEmails("emails.txt", outputArea);
        outputArea.appendText("Количество валидных адресов электронной почты:" + " " + emailReader.countValidEmails("emails.txt", outputArea) + "\n");
        logger.info("Displayed email information. Valid email addresses: {}\", count");
    }

    /**
     * Отображает содержимое письма.
     *
     * @param outputArea Область вывода, в которую выводится содержимое письма.
     */
    private void displayLetterInfo(TextArea outputArea) {
        LetterReader letterReader = new LetterReader();
        letterReader.printLetterContent("message.txt", outputArea);
        logger.info("Displayed letter content.");
    }

    /**
     * Отправляет письма на основе пользовательского ввода.
     *
     * @param subject Тема письма.
     * @param outputArea Область вывода для отображения результатов отправки.
     */
    private void sendEmailsWithUserInput(String subject, TextArea outputArea) {
        outputArea.appendText("Ожидайте, отправка займет некоторое время.\n");
        logger.debug("The beginning of the process of sending emails.");
        List<String> credentials = CredentialsDecryptor.readEncryptedCredentials("logpass.txt", outputArea);
        if (credentials == null || credentials.size() != 2) {
            outputArea.appendText("Не удалось получить учетные данные для отправки сообщений.\n");
            return;
        }

        EmailReader emailReader = new EmailReader();
        List<String[]> emailList = emailReader.readEmails("emails.txt", outputArea);
        LetterReader letterReader = new LetterReader();
        String messageTemplate = letterReader.readLetterContent("message.txt", outputArea);

        sendEmails(credentials.get(0), credentials.get(1), emailList, messageTemplate, subject, outputArea, letterReader);
    }

    /**
     * Отправляет письма.
     *
     * @param username имя пользователя для авторизации на почтовом сервере.
     * @param password Пароль пользователя.
     * @param emailList Список адресов электронной почты для отправки.
     * @param messageTemplate Шаблон сообщения.
     * @param subject Тема письма.
     * @param outputArea Область вывода для отображения результатов отправки.
     * @param letterReader Объект для чтения содержимого письма.
     */
    private void sendEmails(String username, String password, List<String[]> emailList, String messageTemplate, String subject, TextArea outputArea, LetterReader letterReader) {
        EmailSender emailSender = new EmailSender(username, password);
        logger.info("Beginning to send emails. User: {}", username);
        for (String[] emailInfo : emailList) {
            String toEmail = emailInfo[0];
            String name = emailInfo[1];
            String personalizedMessage = letterReader.replaceNamePlaceholder(messageTemplate, name, outputArea);
            emailSender.sendEmail(toEmail, subject, personalizedMessage, outputArea);
            logger.info("Email sent to: {}", toEmail);
        }
        outputArea.appendText("\nПисьма отправлены.\n");
        logger.info("All emails sent successfully.");
    }

    /**
     * Точка входа в приложение.
     *
     * @param args Аргументы командной строки.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
