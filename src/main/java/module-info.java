/**
 * Модуль com.example.emailapp
 * Этот модуль предназначен для приложения электронной почты, использующего JavaFX и Java Mail API.
 *
 * Зависимости модуля:
 * - javafx.controls: Модуль JavaFX, содержащий классы для элементов управления пользовательского интерфейса.
 * - javafx.fxml: Модуль JavaFX, обеспечивающий поддержку FXML. FXML используется для определения пользовательского интерфейса.
 * - java.mail: Модуль, предоставляющий API для работы с электронной почтой. Используется для создания, отправки и получения электронных сообщений.
 * - org.apache.logging.log4j: Модуль Apache Log4j 2 для логирования. Используется для записи логов работы приложения.
 *
 * Детали модуля:
 * - opens com.example.emailapp to javafx.fxml: Открывает пакет com.example.emailapp для модуля javafx.fxml, что позволяет FXML загружать классы из этого пакета.
 * - exports com.example.emailapp: Экспортирует пакет com.example.emailapp, делая его доступным для других модулей.
 */
module com.example.emailapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.mail;
    requires org.apache.logging.log4j;

    opens com.example.appemail to javafx.fxml;
    exports com.example.appemail;
}
