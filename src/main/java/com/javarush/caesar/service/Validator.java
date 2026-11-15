package com.javarush.caesar.service;
import java.nio.file.Files;
import java.nio.file.Path;

public class Validator {

    public boolean isValidKey(int key) {
        return key >= 0 && key < 43;
    }


    public int parseKey(String input) {
        try {
            int parsedKey = Integer.parseInt(input);

            if (!isValidKey(parsedKey)) {
                throw new IllegalArgumentException("Ключ должен быть от 0 до 42");
            }

            return parsedKey;

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Ключ должен быть целым числом", e);
        }
    }

    public boolean isValidInputFile(String path) {
        if (path == null || path.isBlank()) {
            return false;
        }
        Path filePath = Path.of(path);
        return Files.isRegularFile(filePath) && Files.isReadable(filePath);
    }

    public boolean isValidOutputFile(String inputPath, String outputPath) {

        if (inputPath == null || inputPath.isBlank()
                || outputPath == null || outputPath.isBlank()) {
            return false;
        }
        Path input = Path.of(inputPath).toAbsolutePath().normalize();
        Path output = Path.of(outputPath).toAbsolutePath().normalize();

        return !input.equals(output);

    }
}