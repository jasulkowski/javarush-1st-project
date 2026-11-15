package com.javarush.caesar.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class FileManager {
    public boolean isRegularFile(String filePath) {
        return Files.isRegularFile(Path.of(filePath));
    }

    public String readFile(String filePath) throws IOException {
        return Files.readString(Path.of(filePath), StandardCharsets.UTF_8);
    }

    public String writeFile(String filePath, String content) throws IOException {
        Path path = Path.of(filePath);

        Files.writeString(
                path,
                content,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE_NEW
        );

        return path.toString();
    }
}
