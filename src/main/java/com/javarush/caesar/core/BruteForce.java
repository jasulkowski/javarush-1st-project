package com.javarush.caesar.core;

public class BruteForce {

    private final CaesarCipher cipher;

    public BruteForce(CaesarCipher cipher) {
        this.cipher = cipher;
    }

    public int findKey(String encryptedText) {
        int bestKey = 0;
        int bestScore = Integer.MIN_VALUE;

        for (int key = 0; key < 43; key++) {
            String candidate = cipher.decrypt(encryptedText, key);
            int score = calculateScore(candidate);

            if (score > bestScore) {
                bestScore = score;
                bestKey = key;
            }
        }
        return bestKey;
    }

    private int calculateScore(String text) {
        int score = 0;

        for (int i = 0; i < text.length(); i++) {
            char symbol = text.charAt(i);

            if (symbol == ' ') {score += 5;}
        }

        if (text.contains(", ")) {score += 10;}
        if (text.contains(". ")) {score += 10;}
        if (text.contains("! ")) {score += 10;}
        if (text.contains("? ")) {score += 10;}

        String lowerText = text.toLowerCase();

        score += countOccurrences(lowerText, "ст") * 3;
        score += countOccurrences(lowerText, "но") * 3;
        score += countOccurrences(lowerText, "ен") * 3;
        score += countOccurrences(lowerText, "ов") * 3;
        score += countOccurrences(lowerText, "пр") * 3;
        score += countWords(text, "и") * 8;
        score += countWords(text, "в") * 8;
        score += countWords(text, "на") * 8;
        score += countWords(text, "не") * 8;
        score += countWords(text, "что") * 10;
        score += countWords(text, "это") * 10;
        score += countWords(text, "как") * 10;

        return score;
    }

    private int countOccurrences(String text, String fragment) {
        int count = 0;

        for (int i = 0; i <= text.length() - fragment.length(); i++) {
            if (text.startsWith(fragment, i)) {
                count++;
            }
        }

        return count;
    }

    private int countWords(String text, String word) {
        int count = 0;

        String[] words = text.toLowerCase().split("[^а-яё]+");

        for (String currentWord : words) {
            if (currentWord.equals(word)) {count++;}
        }

        return count;
    }


}