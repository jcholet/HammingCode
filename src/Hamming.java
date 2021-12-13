import java.util.ArrayList;
import java.util.Arrays;
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
        generateWord(mot);
        verifyWord(mot);
    }

    public static String toBinary(String input){
        StringBuilder stringBuilder = new StringBuilder();
        char[] chars = input.toCharArray();
        for(char character : chars){
            stringBuilder.append(String.format("%8s", Integer.toBinaryString(character)).replaceAll(" ", "0"));
        }
        System.out.println(stringBuilder.toString());
        return stringBuilder.toString();
    }

    public static void verifyWord(String word) throws Exception {
        // Vérifie la longueur du mot
        int n = 1;
        while (Math.pow(2, n) - 1 < word.length()) {
            n++;
        }

        System.out.println(Math.pow(2, n) - 1);
        System.out.println(word.length());

        if (Math.pow(2, n) - 1 != word.length()) {
            throw new Exception("Mot de longueur invalide.");
        }

        // Définition de la portée des bits de controle
        int[] control = new int[n];

        for (int c = 0; c < n; c++) {
            for (Integer i : bitsControled(c, n)) {
                int endroit = (int) Math.pow(2, n) - i - 1;
                if (word.charAt(endroit) != '0' && word.charAt(endroit) != '1') {
                    throw new Exception("Caractère invalide à l'indice " + endroit);
                }

                control[c] = (control[c] + Character.getNumericValue(word.charAt(endroit))) % 2;
            }
        }
        // Debug
        System.out.println(Arrays.toString(control));

        // Conversion de l'adresse du bit incriminé.
        int indiceRetour = 0;
        for (int i = 0; i < n; i++) {
            indiceRetour += control[i] * Math.pow(2, i);
        }

        System.out.println(indiceRetour);
        if (indiceRetour != 0) {
            throw new Exception(word + " : erreur index " + (word.length() - indiceRetour));
        }

    }

    public static String generateWord(String word) throws Exception {
        // Vérifie la longueur du mot
        int n = 1;
        while ((Math.pow(2, n) - 1 - n) < word.length()) n++;

        // S'il n'est pas à la bonne longueur, on l'étend de zéros afin qu'il fasse la bonne taille.
        if (Math.pow(2, n) - 1 - n != word.length())
            word = String.format("%" + (int)(Math.pow(2, n) - 1 - n) + "s", word).replace(' ', '0');

        StringBuilder mot = new StringBuilder(word);

        // On ajoute des bits de contrôle nuls aux bons endroits
        for (int c = 0; c < n; c++) {
            int endroit = mot.length() - (int) Math.pow(2, c) + 1;
            mot.insert(endroit, '0');
        }

        // Et on adapte la valeur de tous ces bits de controle.
        for (int c = 0; c < n; c++) {
            int valeur = 0;
            for (Integer i : bitsControled(c, n)) {
                int endroit = (int) Math.pow(2, n) - i - 1;
                if(mot.charAt(endroit) != '0' && mot.charAt(endroit) != '1')
                    throw new Exception("Caractère invalide à l'indice " + endroit + " : " + mot.charAt(endroit));

                valeur = (valeur + Character.getNumericValue(mot.charAt(endroit))) % 2;
            }

            int endroit = mot.length() - (int) Math.pow(2, c);
            System.out.println(endroit);
            mot.setCharAt(endroit, Character.forDigit(valeur, 10));
        }

        return mot.toString();
    }

    private static ArrayList<Integer> bitsControled(int c, int n) {
        ArrayList<Integer> retour = new ArrayList<>();

        // On parcourt tous les nombres à n bits.
        for (int i = 0; i < Math.pow(2, n); i++) {
            // On change l'entier actuelle en chaîne binaire
            String temp = String.format("%" + n + "s", Integer.toBinaryString(i)).replace(' ', '0');
            int endroit = n - c - 1;
            // Si le bit c est de 1, on ajoute le nombre parcouru dans les bits controlés à retourner
            if (temp.charAt(endroit) == '1') {
                retour.add(i);
            }
        }

        return retour;
    }

}
