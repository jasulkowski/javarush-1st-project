import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        CaesarCipher cipher = new CaesarCipher();
        FileManager file = new FileManager();
        String inputPath = "input.txt";
        String outputPath = "output.txt";
        int key = 3;

        try {
            String originalText = file.readFile(inputPath);
            String encryptedText = cipher.encrypt(originalText, key);

            file.writeFile(outputPath, encryptedText);
            System.out.println("Файл успешно зашифрован!");

        } catch (IOException e) {
            System.err.println("Ошибка при работе с файлом!");
            System.err.println("Причина: " + e.getMessage());
        }


    }
}