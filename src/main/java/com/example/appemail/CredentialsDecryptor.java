package com.example.appemail;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.TextArea;

/**
 * Класс CredentialsDecryptor используется для декодирования зашифрованных учетных данных.
 * Использует простой XOR-алгоритм для декодирования.
 */
public class CredentialsDecryptor {

    private static final String DECRYPTION_KEY = "kursovayarabota";

    /**
     * Читает и декодирует зашифрованные учетные данные из файла.
     *
     * @param filePath Путь к файлу с зашифрованными учетными данными.
     * @param outputArea TextArea для вывода сообщений об ошибках.
     * @return Список строк с декодированными учетными данными.
     */
    public static List<String> readEncryptedCredentials(String filePath, TextArea outputArea) {
        try {
            List<String> credentials = readLinesFromFile(filePath);
            validateCredentialsFormat(credentials);
            return decryptCredentials(credentials);
        } catch (IOException e) {
            outputArea.appendText("Ошибка чтения файла:" + e.getMessage() + "\n");
        } catch (IllegalArgumentException e) {
            outputArea.appendText(e.getMessage() + "\n");
        }
        return new ArrayList<>();
    }

    /**
     * Читает строки из файла и возвращает их в виде списка.
     *
     * @param filePath Путь к файлу для чтения.
     * @return Список прочитанных строк.
     * @throws IOException Если происходит ошибка чтения файла.
     */
    private static List<String> readLinesFromFile(String filePath) throws IOException {
        List<String> lines = new ArrayList<>();
        try (InputStream is = CredentialsDecryptor.class.getClassLoader().getResourceAsStream(filePath);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }

    /**
     * Проверяет формат списка учетных данных.
     *
     * @param credentials Список строк с учетными данными для проверки.
     * @throws IllegalArgumentException Если формат учетных данных неверен.
     */
    private static void validateCredentialsFormat(List<String> credentials) {
        if (credentials.isEmpty()) {
            throw new IllegalArgumentException("Файл пуст.");
        } else if (credentials.size() != 2) {
            throw new IllegalArgumentException("Неверный формат данных логина и пароля.Ожидаются две строки.");
        }
    }

    /**
     * Декодирует список зашифрованных учетных данных.
     *
     * @param encryptedCredentials Список зашифрованных учетных данных.
     * @return Список декодированных учетных данных.
     */
    private static List<String> decryptCredentials(List<String> encryptedCredentials) {
        List<String> decryptedCredentials = new ArrayList<>();
        for (String encryptedCredential : encryptedCredentials) {
            decryptedCredentials.add(decrypt(encryptedCredential));
        }
        return decryptedCredentials;
    }

    /**
     * Декодирует зашифрованную строку.
     *
     * @param encryptedData Зашифрованная строка для декодирования.
     * @return Декодированная строка.
     */
    private static String decrypt(String encryptedData) {
        byte[] inputBytes = convertHexStringToByteArray(encryptedData);
        return xorDecrypt(inputBytes);
    }

    /**
     * Преобразует шестнадцатеричную строку в массив байтов.
     *
     * @param hexString Шестнадцатеричная строка для преобразования.
     * @return Массив байтов, представляющий шестнадцатеричную строку.
     * @throws IllegalArgumentException Если строка имеет неверный формат.
     */
    private static byte[] convertHexStringToByteArray(String hexString) {
        if (hexString == null || hexString.length() % 2 != 0) {
            throw new IllegalArgumentException("Неверный формат строки для шестнадцатеричного преобразования:" + hexString);
        }
        byte[]data = new byte[hexString.length() / 2];
        for (int i = 0; i < hexString.length(); i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4) + Character.digit(hexString.charAt(i+1), 16));
        }
        return data;
    }

    /**
     * Выполняет XOR-декодирование массива байтов.
     *
     * @param data Массив байтов для декодирования.
     * @return Декодированная строка.
     */
    private static String xorDecrypt(byte[] data) {
        StringBuilder output = new StringBuilder();
        for (byte b : data) {
            output.append((char) (b ^ DECRYPTION_KEY.charAt(output.length() % DECRYPTION_KEY.length())));
        }
        return output.toString();
    }
}
