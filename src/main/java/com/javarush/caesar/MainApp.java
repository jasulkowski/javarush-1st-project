package com.javarush.caesar;

import com.javarush.caesar.core.Alphabet;
import com.javarush.caesar.core.CaesarCipher;
import com.javarush.caesar.service.FileManager;
import com.javarush.caesar.service.Validator;


import java.io.IOException;

public class MainApp {
    private static final String INPUT_PATH = "input.txt";
    private static final String OUTPUT_PATH = "output.txt";
    private static final int KEY = 3;

    private final CaesarCipher cipher = new CaesarCipher(Alphabet.  getSymbols());
    private final FileManager fileManager = new FileManager();
    private final Validator validator = new Validator();
    public static void main(String[] args) {
        new MainApp().run();
    }


    private void run() {
        try {
            if (!validator.isValidInputFile(INPUT_PATH)) {
                throw new IllegalArgumentException("Входной файл не существует или недоступен для чтения");
            }

            if (!validator.isValidOutputFile(INPUT_PATH, OUTPUT_PATH)) {
                throw new IllegalArgumentException("Входной и выходной файлы не должны совпадать");
            }

            String originalText = fileManager.readFile(INPUT_PATH);
            String encryptedText = cipher.encrypt(originalText, KEY);
            String savedPath = fileManager.writeFile(OUTPUT_PATH, encryptedText);
            System.out.println("Файл успешно зашифрован: " + savedPath);

        } catch (IllegalArgumentException e) {
            System.err.println("Ошибка ввода: " + e.getMessage());

        } catch (IOException e) {
            System.err.println("Ошибка при работе с файлом: " + e.getMessage());
        }
    }

}
