package com.example.appemail;

import java.io.*;
import java.nio.charset.StandardCharsets;

import javafx.scene.control.TextArea;

/**
 * Класс LetterReader используется для чтения и обработки содержимого письма из файла.
 */
public class LetterReader {

    /**
     * Читает содержимое письма из файла и проверяет его на валидность.
     *
     * @param filePath Путь к файлу с содержимым письма.
     * @param outputArea Область для вывода информации и ошибок.
     * @return Строка, содержащая содержимое письма.
     */
    public String readLetterContent(String filePath, TextArea outputArea) {
        String letterContent = readFile(filePath, outputArea);
        if (!letterContent.isEmpty()) {
            validateLetterContent(letterContent, filePath, outputArea);
        }
        return letterContent;
    }

    /**
     * Читает содержимое файла и возвращает его в виде строки.
     *
     * @param filePath Путь к файлу для чтения.
     * @param outputArea Область для вывода информации об ошибках.
     * @return Строка, содержащая прочитанное содержимое файла.
     */
    private String readFile(String filePath, TextArea outputArea) {
        StringBuilder contentBuilder = new StringBuilder();
        try (InputStream is = LetterReader.class.getClassLoader().getResourceAsStream(filePath);
             BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                contentBuilder.append(line).append(System.lineSeparator());
            }
        } catch (IOException e) {
            outputArea.appendText("Ошибка чтения файла: " + e.getMessage() + "\n");
            return "";
        }
        return contentBuilder.toString();
    }

    /**
     * Проверяет содержимое письма на наличие определенных шаблонов или ошибок.
     *
     * @param content Содержимое письма для проверки.
     * @param filePath Путь к файлу, из которого было прочитано содержимое.
     * @param outputArea Область для вывода предупреждений или замечаний.
     */
    private void validateLetterContent(String content, String filePath, TextArea outputArea) {
        if (content.isEmpty()) {
            outputArea.appendText("Файл" + filePath + " пуст.\n");
        } else if (content.trim().equals("{name}")) {
            outputArea.appendText("Примечание: письмо содержит только плейсхолдер.\n");
        } else if (content.trim().equals("Добрый день, {name}!")) {
            outputArea.appendText("Примечание: письмосодержит только приветствие, дополнительный текст отсутствует.\n");
        }
    }

    /**
     * Заменяет плейсхолдер имени на заданное имя в содержимом письма.
     *
     * @param letterContent Содержимое письма, в котором нужно выполнить замену.
     * @param name мя, на которое нужно заменить плейсхолдер.
     * @param outputArea Область для вывода информации.
     * @return Содержимое письма с замененным плейсхолдером.
     */
    public String replaceNamePlaceholder(String letterContent, String name, TextArea outputArea) {
        if (!letterContent.contains("{name}")) {
            outputArea.appendText("Плейсхолдер {name} не найден в содержимом письма.\n");
            return letterContent;
        }
        return letterContent.replace("{name}", name);
    }

    /**
     * Выводит содержимое файла письма в TextArea.
     *
     * @param filePath Путь к файлу с письмом.
     * @param outputArea Область для вывода содержимого письма.
     */
    public void printLetterContent(String filePath, TextArea outputArea) {
        String letterContent = readFile(filePath, outputArea);
        if (!letterContent.isEmpty()) {
            outputArea.appendText("Содержимое файла " + filePath + ":\n" + letterContent + "\n");
        }
    }

}

