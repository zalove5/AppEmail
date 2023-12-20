package com.example.appemail;

/**
 * Точка входа в JavaFX приложение для рассылки электронных писем.
 * Этот класс запускает EmailApplication, предоставляющее графический интерфейс пользователя.
 *
 * Класс EmailLauncher используется только для инициализации и запуска главного приложения
 * и не включает в себя другой функциональности.
 *
 *
 * Аргументы командной строки могут использоваться для настройки приложения.
 */
public class EmailLauncher {
    /**
     * Главный метод для запуска приложения.
     *
     * @param args Аргументы командной строки, передаваемые при запуске приложения.
     *             Могут включать различные параметры для настройки приложения.
     */
    public static void main(String[] args) {
        EmailApplication.main(args);
    }
}