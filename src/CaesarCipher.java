public class CaesarCipher {
    char[][] alphabet = {
            {'А', 'а'}, {'Б', 'б'}, {'В', 'в'}, {'Г', 'г'}, {'Д', 'д'},
            {'Е', 'е'}, {'Ё', 'ё'}, {'Ж', 'ж'}, {'З', 'з'}, {'И', 'и'},
            {'Й', 'й'}, {'К', 'к'}, {'Л', 'л'}, {'М', 'м'}, {'Н', 'н'},
            {'О', 'о'}, {'П', 'п'}, {'Р', 'р'}, {'С', 'с'}, {'Т', 'т'},
            {'У', 'у'}, {'Ф', 'ф'}, {'Х', 'х'}, {'Ц', 'ц'}, {'Ч', 'ч'},
            {'Ш', 'ш'}, {'Щ', 'щ'}, {'Ъ', 'ъ'}, {'Ы', 'ы'}, {'Ь', 'ь'},
            {'Э', 'э'}, {'Ю', 'ю'}, {'Я', 'я'}
    };

    public char encryptChar(char letter, int key) {
        int foundIndex = -1;
        int caseIndex = 0;

        for (int i = 0; i < alphabet.length; i++) {
            if (alphabet[i][0] == letter) {
                foundIndex = i;
                caseIndex = 0;
                break;
            }

            if (alphabet[i][1] == letter) {
                foundIndex = i;
                caseIndex = 1;
                break;
            }
        }

        if (foundIndex != -1) {
            int newIndex = (foundIndex + key) % alphabet.length;

            if (newIndex < 0) {
                newIndex += alphabet.length;
            }
            letter = alphabet[newIndex][caseIndex];
        }

        return letter;
    }

    public String encrypt(String text, int key){
        StringBuilder builder = new StringBuilder();

        for(int i = 0; i < text.length(); i++){
            builder.append(encryptChar(text.charAt(i), key));
        }

        return builder.toString();
    }

    public String decrypt(String text, int key) {
        return encrypt(text, -key);
    }
}