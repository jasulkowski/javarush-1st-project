package com.javarush.caesar;

import com.javarush.caesar.core.Alphabet;
import com.javarush.caesar.core.CaesarCipher;
import com.javarush.caesar.service.FileManager;
import com.javarush.caesar.service.Validator;
import com.javarush.caesar.core.BruteForce;
import java.util.Scanner;

import java.io.IOException;

public class MainApp {

    private final CaesarCipher cipher = new CaesarCipher(Alphabet.  getSymbols());
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

            switch (choice) {
                case "1" -> processFile(scanner, true);
                case "2" -> processFile(scanner, false);
                case "3" -> bruteForceFile(scanner);
                case "0" -> running = false;

                default ->
                        System.out.println("Неизвестный пункт меню");
            }
        }

        System.out.println("Программа завершена");
    }


    private void processFile(Scanner scanner, boolean encryptMode) {
        try {
            System.out.print("Введите путь к исходному файлу: ");
            String inputPath = scanner.nextLine().trim();

            System.out.print("Введите путь для сохранения результата: ");
            String outputPath = scanner.nextLine().trim();

            System.out.print("Введите ключ шифрования (0–42): ");
            String keyInput = scanner.nextLine().trim();
            int key = validator.parseKey(keyInput);

            if (!validator.isValidInputFile(inputPath)) {
                throw new IllegalArgumentException("Входной файл не существует или недоступен для чтения");
            }

            if (!validator.isValidOutputFile(inputPath, outputPath)) {
                throw new IllegalArgumentException("Входной и выходной файлы не должны совпадать");
            }

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

        } catch (IllegalArgumentException e) {
            System.err.println("Ошибка ввода: " + e.getMessage());

        } catch (IOException e) {
            System.err.println("Ошибка при работе с файлом: " + e.getMessage());
        }
    }


    private void bruteForceFile(Scanner scanner) {
        try {
            System.out.print("Введите путь к зашифрованному файлу: ");
            String inputPath = scanner.nextLine().trim();

            System.out.print("Введите путь для сохранения результата: ");
            String outputPath = scanner.nextLine().trim();

            if (!validator.isValidInputFile(inputPath)) {
                throw new IllegalArgumentException("Файл не существует или недоступен для чтения");
            }

            if (!validator.isValidOutputFile(inputPath, outputPath)) {
                throw new IllegalArgumentException("Входной и выходной файлы не должны совпадать");
            }

            String encryptedText = fileManager.readFile(inputPath);
            int foundKey = bruteForce.findKey(encryptedText);

            System.out.println("Наиболее вероятный ключ: " + foundKey);

            String decryptedText = cipher.decrypt(encryptedText, foundKey);
            String savedPath = fileManager.writeFile(outputPath, decryptedText);

            System.out.println("Файл успешно расшифрован: " + savedPath);

        } catch (IllegalArgumentException e) {
            System.err.println("Ошибка ввода: " + e.getMessage());

        } catch (IOException e) {
            System.err.println("Ошибка при работе с файлом: " + e.getMessage());
        }
    }


}
