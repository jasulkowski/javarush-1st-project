
package com.javarush.caesar;

import com.javarush.caesar.core.Alphabet;
import com.javarush.caesar.core.BruteForce;
import com.javarush.caesar.core.CaesarCipher;
import com.javarush.caesar.exception.CipherException;
import com.javarush.caesar.service.FileManager;
import com.javarush.caesar.service.Validator;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.Scanner;

public class MainApp {

    private final CaesarCipher cipher = new CaesarCipher(Alphabet.getSymbols());
    private final FileManager fileManager = new FileManager();
    private final BruteForce bruteForce = new BruteForce(cipher);
    private final Validator validator = new Validator();

    public static void main(String[] args) {
        new MainApp().run();
    }

    private void run() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n===== ШИФР ЦЕЗАРЯ =====");
            System.out.println("1. Зашифровать файл \uD83D\uDD12");
            System.out.println("2. Расшифровать файл \uD83D\uDD13");
            System.out.println("3. Brute Force \uD83D\uDC7E");
            System.out.println("0. Выход \uD83D\uDEAA");
            System.out.print("Выберите действие: ");

            String choice = scanner.nextLine();

            try {
                switch (choice) {
                    case "1" -> processFile(scanner, true);
                    case "2" -> processFile(scanner, false);
                    case "3" -> bruteForceFile(scanner);
                    case "0" -> running = false;
                    default -> System.out.println("Неизвестный пункт меню");
                }

            } catch (CipherException e) {
                System.err.println("Ошибка: " + e.getMessage());

            } catch (IllegalArgumentException e) {
                System.err.println("Ошибка ввода: " + e.getMessage());

            } catch (FileAlreadyExistsException e) {
                System.err.println("Ошибка: файл " + e.getFile() + " уже существует.");
                System.err.println("Укажите другое имя выходного файла.");

            } catch (IOException e) {
                System.err.println("Ошибка при работе с файлом: " + e.getMessage());
            }
        }

        System.out.println("Программа завершена");
    }

    private void processFile(Scanner scanner, boolean encryptMode)
            throws IOException, CipherException {

        System.out.print("Введите путь к исходному файлу: ");
        String inputPath = scanner.nextLine().trim();

        System.out.print("Введите путь для сохранения результата: ");
        String outputPath = scanner.nextLine().trim();

        System.out.print("Введите ключ (0–42): ");
        String keyInput = scanner.nextLine().trim();
        int key = validator.parseKey(keyInput);

        validateFilePaths(inputPath, outputPath);

        String originalText = fileManager.readFile(inputPath);
        String resultText;

        if (encryptMode) {
            resultText = cipher.encrypt(originalText, key);
        } else {
            resultText = cipher.decrypt(originalText, key);
        }

        String savedPath = fileManager.writeFile(outputPath, resultText);

        if (encryptMode) {
            System.out.println("Файл успешно зашифрован: " + savedPath);
        } else {
            System.out.println("Файл успешно расшифрован: " + savedPath);
        }
    }

    private void bruteForceFile(Scanner scanner) throws IOException {

        System.out.print("Введите путь к зашифрованному файлу: ");
        String inputPath = scanner.nextLine().trim();

        System.out.print("Введите путь для сохранения результата: ");
        String outputPath = scanner.nextLine().trim();

        validateFilePaths(inputPath, outputPath);

        String encryptedText = fileManager.readFile(inputPath);
        int foundKey = bruteForce.findKey(encryptedText);

        System.out.println("Наиболее вероятный ключ: " + foundKey);

        String decryptedText = cipher.decrypt(encryptedText, foundKey);
        String savedPath = fileManager.writeFile(outputPath, decryptedText);

        System.out.println("Файл успешно расшифрован: " + savedPath);
    }

    private void validateFilePaths(String inputPath, String outputPath) {

        if (!validator.isValidInputFile(inputPath)) {
            throw new IllegalArgumentException(
                    "Входной файл не существует или недоступен для чтения"
            );
        }

        if (!validator.isValidOutputFile(inputPath, outputPath)) {
            throw new IllegalArgumentException(
                    "Входной и выходной файлы не должны совпадать"
            );
        }
    }
}
