package com.example.appemail;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import javafx.scene.control.TextArea;

/**
 * Класс EmailReader предназначен для чтения и обработки адресов электронной почты из файла.
 */
public class EmailReader {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w-_.+]*[\\w-_.]@([\\w]+\\.)+[\\w]+[\\w]$");

    /**
     * Читает адреса электронной почты из файла и возвращает список адресов.
     *
     * @param filePath Путь к файлу с адресами электронной почты.
     * @param outputArea Область для вывода сообщений об ошибках и информации.
     * @return Список массивов строк, где каждый массив содержит адрес электронной почты и связанную информацию.
     */
    public List<String[]> readEmails(String filePath, TextArea outputArea) {
        List<String[]> emailList = new ArrayList<>();
        try (InputStream is = EmailReader.class.getClassLoader().getResourceAsStream(filePath);
             BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            processFile(br, emailList, outputArea);
        } catch (IOException e) {
            outputArea.appendText("Ошибка чтения файла адресов: " + e.getMessage() + "\n");
        }

        if (emailList.isEmpty()) {
            outputArea.appendText("Нет корректных адресов электронной почты в файле.\n");
        }
        return emailList;
    }

    /**
     * Обрабатывает каждую строку файла.
     *
     * @param reader BufferedReader для чтения файла.
     * @param emailList Список для сохранения обработанных адресов электронной почты.
     * @param outputArea Область для вывода информации.
     * @throws IOException Если происходит ошибка чтения файла.
     */
    private void processFile(BufferedReader reader, List<String[]> emailList, TextArea outputArea) throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            processLine(line, emailList, outputArea);
        }
    }

    /**
     * Обрабатывает отдельную строку из файла.
     *
     * @param line Строка для обработки.
     * @param emailList Список для добавления обработанной информации.
     * @param outputArea Область для вывода информации об ошибках.
     */
    private void processLine(String line, List<String[]> emailList, TextArea outputArea) {
        String[] parts = line.split(",", 2);
        if (isValidLine(parts)) {
            if (!isValidEmailAddress(parts[0].trim())) {
                outputArea.appendText("Неверный формат адреса электронной почты:" + " " + parts[0] + "\n");
                return;
            }
            emailList.add(new String[]{parts[0].trim(), parts[1].trim()});
        } else {
            outputArea.appendText("Неверный формат строки:" + " " + line + "\n");
        }
    }

    /**
     * Проверяет, является ли строка валидной для обработки.
     *
     * @param parts Массив строк, полученный из разделенной строки.
     * @return true, если строка валидна, иначе false.
     */
    private boolean isValidLine(String[] parts) {
        return parts.length == 2 && !parts[0].trim().isEmpty() && !parts[1].trim().isEmpty();
    }

    /**
     * Подсчитывает количество валидных адресов электронной почты в файле.
     *
     * @param filePath Путь к файлу с адресами электронной почты.
     * @param outputArea Область для вывода информации.
     * @return Количество валидных адресов электронной почты.
     */
    public int countValidEmails(String filePath, TextArea outputArea) {
        int validEmailCount = 0;
        try (InputStream is = EmailReader.class.getClassLoader().getResourceAsStream(filePath);
             BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (isValidEmailAddress(line.split(",", 2)[0].trim())) {
                    validEmailCount++;
                }
            }
        } catch (IOException e) {
            outputArea.appendText("Ошибка чтения файла адресов:" + " " + e.getMessage() + "\n");
        }
        return validEmailCount;
    }

    /**
     * Проверяет, соответствует ли адрес электронной почты заданному шаблону.
     *
     * @param email Адрес электронной почты для проверки.
     * @return true, если адрес соответствует шаблону, иначе false.
     */
    private boolean isValidEmailAddress(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * Выводит содержимое файла в TextArea.
     *
     * @param filePath Путь к файлу для чтения.
     * @param outputArea Область для вывода содержимого файла.
     */
    public void printFileContent(String filePath, TextArea outputArea) {
        try (InputStream is = EmailReader.class.getClassLoader().getResourceAsStream(filePath);
             BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            outputArea.appendText("Содержимое файла" + " " + filePath + ":\n");
            while ((line = br.readLine()) != null) {
                outputArea.appendText(line + "\n");
            }
        } catch (IOException e) {
            outputArea.appendText("Ошибка чтения файла:" + " " + e.getMessage() + "\n");
        }
    }
}
