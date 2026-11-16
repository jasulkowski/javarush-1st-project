
package com.javarush.caesar;

import com.javarush.caesar.core.Alphabet;
import com.javarush.caesar.core.BruteForce;
import com.javarush.caesar.core.CaesarCipher;
import com.javarush.caesar.exception.CipherException;
import com.javarush.caesar.service.FileManager;
import com.javarush.caesar.service.Validator;

import java.util.Scanner;

public class MainApp {

    private static final int BRUTE_FORCE_SAMPLE_SIZE = 65_536;
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
                    case "0", "exit" -> running = false;
                    default -> System.out.println("Неизвестный пункт меню");
                }

            } catch (CipherException e) {
                System.err.println("Ошибка: " + e.getMessage());

            } catch (IllegalArgumentException e) {
                System.err.println("Ошибка ввода: " + e.getMessage());

            }
        }

        System.out.println("Программа завершена");
    }

    private void processFile(Scanner scanner, boolean encryptMode)
            throws CipherException {

        System.out.print("Введите путь к исходному файлу: ");
        String inputPath = scanner.nextLine().trim();

        System.out.print("Введите путь для сохранения результата: ");
        String outputPath = scanner.nextLine().trim();

        System.out.print("Введите ключ (0–42): ");
        String keyInput = scanner.nextLine().trim();
        int key = validator.parseKey(keyInput);

        validateFilePaths(inputPath, outputPath);

        String savedPath;

        if (encryptMode) {
            savedPath = fileManager.transformFile(inputPath, outputPath,
                    chunk -> cipher.encrypt(chunk, key));
        } else {
            savedPath = fileManager.transformFile(inputPath, outputPath,
                    chunk -> cipher.decrypt(chunk, key));
        }

        if (encryptMode) {
            System.out.println("Файл успешно зашифрован: " + savedPath);
        } else {
            System.out.println("Файл успешно расшифрован: " + savedPath);
        }
    }

    private void bruteForceFile(Scanner scanner) throws CipherException {

        System.out.print("Введите путь к зашифрованному файлу: ");
        String inputPath = scanner.nextLine().trim();

        System.out.print("Введите путь для сохранения результата: ");
        String outputPath = scanner.nextLine().trim();

        validateFilePaths(inputPath, outputPath);

        String sample = fileManager.readSample(inputPath, BRUTE_FORCE_SAMPLE_SIZE);
        int foundKey = bruteForce.findKey(sample);

        System.out.println("Наиболее вероятный ключ: " + foundKey);

        String savedPath = fileManager.transformFile(inputPath, outputPath,
                chunk -> cipher.decrypt(chunk, foundKey));

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
