package com.javarush.caesar.core;

import com.javarush.caesar.exception.CipherException;

public class CaesarCipher {

    private static final int RUSSIAN_LETTERS_COUNT = 33;
    private static final int ALPHABET_SIZE = 43;

    private final char[] alphabet;

    public CaesarCipher(char[] alphabet) {
        if (alphabet == null || alphabet.length != ALPHABET_SIZE) {
            throw new IllegalArgumentException("Алфавит должен содержать 43 символа");
        }

        this.alphabet = alphabet.clone();

        for (int i = 0; i < this.alphabet.length; i++) {
            for (int j = 0; j < i; j++) {
                if (this.alphabet[i] == this.alphabet[j]) {
                    throw new IllegalArgumentException("Алфавит содержит повторяющиеся символы");
                }
            }
        }
    }

    public char encryptChar(char symbol, int key) {
        return shiftChar(symbol, key);
    }

    public String encrypt(String text, int key) {
        if (text == null) {
            throw new IllegalArgumentException("Текст не должен быть null");
        }

        StringBuilder result = new StringBuilder(text.length());
        for (int i = 0; i < text.length(); i++) {
            char symbol = text.charAt(i);
            if (isSupported(symbol)) {
                result.append(shiftChar(symbol, key));
            }
        }
        return result.toString();
    }

    public String decrypt(String encryptedText, int key) throws CipherException {
        if (encryptedText == null) {
            throw new CipherException("Зашифрованный текст не должен быть null");
        }

        StringBuilder result = new StringBuilder(encryptedText.length());
        for (int i = 0; i < encryptedText.length(); i++) {
            char symbol = encryptedText.charAt(i);
            if (!isSupported(symbol)) {
                throw new CipherException(
                        "Недопустимый символ в зашифрованном тексте на позиции "
                                + (i + 1) + ": " + describeSymbol(symbol)
                );
            }
            result.append(shiftChar(symbol, -(long) key));
        }
        return result.toString();
    }

    private boolean isSupported(char symbol) {
        if (symbol == '\r' || symbol == '\n') {
            return true;
        }
        if (indexOf(symbol, alphabet.length) >= 0) {
            return true;
        }
        if (Character.isUpperCase(symbol)) {
            char lower = Character.toLowerCase(symbol);
            return indexOf(lower, RUSSIAN_LETTERS_COUNT) >= 0;
        }
        return false;
    }

    private char shiftChar(char symbol, long key) {
        if (symbol == '\r' || symbol == '\n') {
            return symbol;
        }

        if (Character.isUpperCase(symbol)) {
            int index = indexOf(Character.toLowerCase(symbol), RUSSIAN_LETTERS_COUNT);
            if (index >= 0) {
                int newIndex = Math.floorMod(index + key, RUSSIAN_LETTERS_COUNT);
                return Character.toUpperCase(alphabet[newIndex]);
            }
        }

        int index = indexOf(symbol, alphabet.length);
        if (index >= 0) {
            int newIndex = Math.floorMod(index + key, alphabet.length);
            return alphabet[newIndex];
        }
        return symbol;
    }

    private int indexOf(char symbol, int length) {
        for (int i = 0; i < length; i++) {
            if (alphabet[i] == symbol) {
                return i;
            }
        }
        return -1;
    }

    private String describeSymbol(char symbol) {
        if (Character.isISOControl(symbol) || Character.isSurrogate(symbol)) {
            return String.format("U+%04X", (int) symbol);
        }
        return "'" + symbol + "'";
    }
}
