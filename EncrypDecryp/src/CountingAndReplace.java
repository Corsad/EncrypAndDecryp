
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author <S3372771>
 */
public class CountingAndReplace {

    private Scanner scanner;
    private Byte choice;
    private Ulti ulti;
    private String fileName;
    private ArrayList<Character> currentArray;

    public static void main(String[] args) {
        CountingAndReplace main = new CountingAndReplace();
        main.run();
    }

    public void run() {
        scanner = new Scanner(System.in);
        ulti = new Ulti();

        System.out.print("Enter your encrypted file's name in the src folder: ");
        fileName = System.getProperty("user.dir") + "\\src\\" + scanner.nextLine();

        int fileExist = ulti.checkExistFile(fileName);

        switch (fileExist) {
            case 1:
                currentArray = ulti.readFile(fileName);
                do {
                    menu();

                    switch (choice) {
                        case 1:
                            printCurrentArray();
                            break;
                        case 2:
                            replaceChar();
                            break;
                        case 3:
                            countChar();
                            break;
                        case 4:
                            writeToFile();
                            break;
                        case 5:
                            System.out.println("Thanks for using the program");
                            break;
                        default:
                            System.out.println("There is no choice like that.");
                            break;
                    }
                } while (choice != 5);
                break;
            default:
                System.out.println("File not exists.");
                break;
        }
    }

    public void menu() {
        System.out.println("1. Print current char Array");
        System.out.println("2. Replace char");
        System.out.println("3. Counting char");
        System.out.println("4. Save Array to file");
        System.out.println("5. Quit");
        System.out.print("\nEnter your choice: ");

        try{
        choice = scanner.nextByte();
        }catch(Exception e){
            System.out.println("Not a choice");
            choice = 0;
            scanner.nextLine();
        }
    }

    public void printCurrentArray() {
        System.out.print("Current Array: \n>");
        for (char c : currentArray) {
            System.out.print(c);
        }
        System.out.print("<\n\n");
    }

    public void replaceChar() {
        scanner.nextLine();
        char replacedChar, charReplacedWith;

        System.out.print("Replace char ('n' for new line): ");
        replacedChar = getStringAndTurnToChar();

        System.out.print("Replaced with char ('n' for new line): ");
        charReplacedWith = getStringAndTurnToChar();

        if (replacedChar == 'n') {
            for (int i = 0; i < currentArray.size(); i++) {
                if (currentArray.get(i) == '\n') {
                    currentArray.set(i, charReplacedWith);
                } else if (currentArray.get(i) == charReplacedWith) {
                    currentArray.set(i, '\n');
                }
            }
        } else if (charReplacedWith == 'n') {
            for (int i = 0; i < currentArray.size(); i++) {
                if (currentArray.get(i) == replacedChar) {
                    currentArray.set(i, '\n');
                } else if (currentArray.get(i) == '\n') {
                    currentArray.set(i, replacedChar);
                }
            }
        } else {
            for (int i = 0; i < currentArray.size(); i++) {
                if (Character.toUpperCase(currentArray.get(i)) == replacedChar || Character.toLowerCase(currentArray.get(i)) == replacedChar) {
                    currentArray.set(i, charReplacedWith);
                } else if (Character.toUpperCase(currentArray.get(i)) == charReplacedWith || Character.toLowerCase(currentArray.get(i)) == charReplacedWith) {
                    currentArray.set(i, replacedChar);
                }
            }
        }

        System.out.println("Replace char '" + replacedChar + "' with char '" + charReplacedWith + "'");
    }

    public char getStringAndTurnToChar() {
        String getChar = scanner.nextLine();

        return getChar.charAt(0);
    }

    public void countChar() {
        HashMap<Character, Integer> map = new HashMap<Character, Integer>();

        for (char c : ulti.getAllChars()) {
            int count = 0;
            for (char readc : currentArray) {
                if (Character.toUpperCase(c) == readc || Character.toLowerCase(c) == readc) {
                    count++;
                }
            }
            map.put(c, count);

        }

        TreeMap<Character, Integer> sortedMap = SortByValue(map);

        for (Map.Entry<Character, Integer> entry : sortedMap.entrySet()) {
            System.out.println("'" + entry.getKey() + "' : " + entry.getValue());
        }
        System.out.println("");
    }

    public static TreeMap<Character, Integer> SortByValue(HashMap<Character, Integer> map) {
        ValueComparator vc = new ValueComparator(map);
        TreeMap<Character, Integer> sortedMap = new TreeMap<Character, Integer>(vc);
        sortedMap.putAll(map);
        return sortedMap;
    }

    public void writeToFile() {
        scanner.nextLine();
        System.out.print("Enter the save name: ");

        String saveName = System.getProperty("user.dir") + "\\src\\" + scanner.nextLine();

        int fileExist = ulti.checkExistFile(saveName);

        switch (fileExist) {
            case 1:
                System.out.println("File Exists");
                break;
            case 0:
                try {
            // Write to File

                    // Remove old file when starting test
                    try (
                            PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(saveName, false)))) {
                        for (char c : currentArray) {
                            writer.print(c);
                        }
                    }
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(Ceasar.class.getName()).log(Level.SEVERE, null, ex);
                } catch (UnsupportedEncodingException ex) {
                    Logger.getLogger(Ceasar.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Ceasar.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
        }
    }

}

class ValueComparator implements Comparator<Character> {

    Map<Character, Integer> map;

    public ValueComparator(Map<Character, Integer> base) {
        this.map = base;
    }

    public int compare(Character a, Character b) {
        if (map.get(a) >= map.get(b)) {
            return -1;
        } else {
            return 1;
        } // returning 0 would merge keys 
    }
}
