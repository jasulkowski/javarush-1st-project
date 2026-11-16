package com.javarush.caesar.service;

import com.javarush.caesar.exception.CipherException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class FileManager {

    private static final int BUFFER_SIZE = 8192;

    @FunctionalInterface
    public interface TextTransformer {
        String transform(String text) throws CipherException;
    }

    public String transformFile(String inputPath, String outputPath,
                                TextTransformer transform) throws CipherException {
        Path source = Path.of(inputPath);
        Path target = Path.of(outputPath);

        try (BufferedReader reader = Files.newBufferedReader(source, StandardCharsets.UTF_8);
             BufferedWriter writer = Files.newBufferedWriter(target, StandardCharsets.UTF_8,
                     StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE)) {

            char[] buffer = new char[BUFFER_SIZE];
            int count;

            while ((count = reader.read(buffer)) != -1) {
                writer.write(transform.transform(new String(buffer, 0, count)));
            }
        } catch (FileAlreadyExistsException e) {
            throw new CipherException(
                    "Файл '" + outputPath + "' уже существует. Укажите другое имя выходного файла.", e
            );
        } catch (IOException e) {
            throw new CipherException("Не удалось прочитать или записать файл: " + e.getMessage(), e);
        }

        return target.toString();
    }

    public String readSample(String filePath, int maxChars) throws CipherException {
        if (maxChars <= 0) {
            throw new IllegalArgumentException("Размер фрагмента должен быть положительным");
        }

        StringBuilder sample = new StringBuilder(maxChars);
        char[] buffer = new char[Math.min(BUFFER_SIZE, maxChars)];

        try (BufferedReader reader = Files.newBufferedReader(Path.of(filePath), StandardCharsets.UTF_8)) {
            int count;
            while (sample.length() < maxChars
                    && (count = reader.read(buffer, 0,
                    Math.min(buffer.length, maxChars - sample.length()))) != -1) {
                sample.append(buffer, 0, count);
            }
        } catch (IOException e) {
            throw new CipherException("Не удалось прочитать файл '" + filePath + "': " + e.getMessage(), e);
        }

        return sample.toString();
    }
}
