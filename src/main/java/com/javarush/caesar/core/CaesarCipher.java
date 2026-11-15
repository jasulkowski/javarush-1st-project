
package com.javarush.caesar.core;

public class CaesarCipher {

    private static final int RUSSIAN_LETTERS_COUNT = 33;
    private static final int ALPHABET_SIZE = 43;

    private final char[] alphabet;

    public CaesarCipher(char[] alphabet) {
        if (alphabet == null || alphabet.length != ALPHABET_SIZE) {
            throw new IllegalArgumentException(
                    "Алфавит должен содержать 43 символа"
            );
        }

        this.alphabet = alphabet.clone();

        for (int i = 0; i < this.alphabet.length; i++) {
            for (int j = 0; j < i; j++) {
                if (this.alphabet[i] == this.alphabet[j]) {
                    throw new IllegalArgumentException(
                            "Алфавит содержит повторяющиеся символы"
                    );
                }
            }
        }
    }

    public char encryptChar(char symbol, int key) {
        return shiftChar(symbol, key);
    }

    public String encrypt(String text, int key) {
        return shift(text, key);
    }

    public String decrypt(String text, int key) {
        return shift(text, -(long) key);
    }

    private String shift(String text, long key) {
        if (text == null) {
            throw new IllegalArgumentException(
                    "Текст не должен быть null"
            );
        }

        StringBuilder result = new StringBuilder(text.length());

        for (int i = 0; i < text.length(); i++) {
            char symbol = text.charAt(i);
            result.append(shiftChar(symbol, key));
        }

        return result.toString();
    }

    private char shiftChar(char symbol, long key) {

        if (Character.isUpperCase(symbol)) {

            char lower = Character.toLowerCase(symbol);

            for (int i = 0; i < RUSSIAN_LETTERS_COUNT; i++) {

                if (alphabet[i] == lower) {

                    int newIndex = Math.floorMod(
                            i + key,
                            RUSSIAN_LETTERS_COUNT
                    );

                    return Character.toUpperCase(alphabet[newIndex]);
                }
            }
        }

        for (int i = 0; i < alphabet.length; i++) {

            if (alphabet[i] == symbol) {

                int newIndex = Math.floorMod(
                        i + key,
                        alphabet.length
                );

                return alphabet[newIndex];
            }
        }

        return symbol;
    }
}
