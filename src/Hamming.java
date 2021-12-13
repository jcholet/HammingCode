import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Hamming {

    public static void main(String[] args) throws Exception {
        String mot;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Entrez votre mot");
        mot = scanner.nextLine();
        if(Pattern.matches("[a-zA-Z]+", mot)){
            mot = toBinary(mot);
        }
        genererMot(mot);
        verificationCode(mot);
    }

    /**
     * A function that permits to change a string into bits
     * @param input the string that you want to convert
     * @return the string in bits
     */
    public static String toBinary(String input){
        StringBuilder stringBuilder = new StringBuilder();
        char[] chars = input.toCharArray();
        for(char character : chars){
            stringBuilder.append(String.format("%8s", Integer.toBinaryString(character)).replaceAll(" ", "0"));
        }
        System.out.println(stringBuilder.toString());
        return stringBuilder.toString();
    }

    /**
     * A function that verify if the string is good or not
     * @param str the bits you want to verify
     */
    public static void verificationCode(String str) {
        int size = 1;
        while (Math.pow(2, size) - 1 < str.length()) {
            size++;
        }

        System.out.println(Math.pow(2, size) - 1);
        System.out.println(str.length());

        if (Math.pow(2, size) - 1 != str.length()) {
            throw new IllegalStateException("La longueur est invalide");
        }

        int[] bitsDeControle = new int[size];

        for (int c = 0; c < size; c++) {
            for (Integer integer : controleDesBits(c, size)) {
                int index = (int) Math.pow(2, size) - integer - 1;
                if (str.charAt(index) != '0' && str.charAt(index) != '1') {
                    throw new IllegalStateException(index + " : caractère invalide.");
                }

                bitsDeControle[c] = (bitsDeControle[c] + Character.getNumericValue(str.charAt(index))) % 2;
            }
        }

        int index = 0;
        for (int i = 0; i < size; i++) {
            index += bitsDeControle[i] * Math.pow(2, i);
        }

        System.out.println(index);
        if (index != 0) {
            throw new IllegalStateException((str.length() - index) + " : erreur");
        }

    }

    /**
     * A function that generate an hamming code
     * @param str The string you want to generate as an hamming code
     * @return the string as an hamming code
     */
    public static String genererMot(String str) {
        //On vérifie que le mot est de bonne longueur
        int size = 1;
        while ((Math.pow(2, size) - 1 - size) < str.length()){
            size++;
        }

        if (Math.pow(2, size) - 1 - size != str.length()){
            str = String.format("%" + (int)(Math.pow(2, size) - 1 - size) + "s", str).replace(' ', '0');
        }

        StringBuilder stringBuilder = new StringBuilder(str);

        for (int c = 0; c < size; c++) {
            int endroit = stringBuilder.length() - (int) Math.pow(2, c) + 1;
            stringBuilder.insert(endroit, '0');
        }

        for (int i = 0; i < size; i++) {
            int value = 0;
            for (Integer integer : controleDesBits(i, size)) {
                int place = (int) Math.pow(2, size) - integer - 1;
                if(stringBuilder.charAt(place) != '0' && stringBuilder.charAt(place) != '1'){
                    throw new IllegalStateException(stringBuilder.charAt(place) + " : erreur à index " + place);
                }
                value = (value + Character.getNumericValue(stringBuilder.charAt(place))) % 2;
            }

            int place = stringBuilder.length() - (int) Math.pow(2, i);
            stringBuilder.setCharAt(place, Character.forDigit(value, 10));
        }

        return stringBuilder.toString();
    }

    private static ArrayList<Integer> controleDesBits(int i, int n) {
        ArrayList<Integer> integers = new ArrayList<>();

        for (int j = 0; j < Math.pow(2, n); j++) {
            String str = String.format("%" + n + "s", Integer.toBinaryString(j)).replace(' ', '0');
            int endroit = n - i - 1;
            if (str.charAt(endroit) == '1') {
                integers.add(j);
            }
        }

        return integers;
    }

}
