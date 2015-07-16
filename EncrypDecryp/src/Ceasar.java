
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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
public class Ceasar {

    private int mode, fileExist, key;
    private String filePath;
    private boolean hasError = false;
    private Ulti ulti;
    char[] allChars;

    public static void main(String[] args) {
        Ceasar ceasar = new Ceasar();
        ceasar.runArgs(args);
    }

    public void runArgs(String[] args) {
        if (args.length < 2) {
            System.out.println("Not enough arguement");
        } else {
            ulti = new Ulti();
            checkArgs(args, args.length);
            checkError();

            if (!hasError) {
                // get char list from Ulti
                allChars = ulti.getAllChars();

                if (args.length == 3) {
                    switch (mode) {
                        // mode encryp
                        case 1:
                            encryptWithKey(Integer.parseInt(args[2]));
                            break;
                        // mode decryp
                        case 2:
                            decryptWithKey(Integer.parseInt(args[2]));
                            break;
                    }
                } else if (args.length == 2) {
                    switch (mode) {
                        // mode encryp
                        case 1:
                            System.out.println("Not supporting encryption without key");
                            break;
                        // mode decryp
                        case 2:
                            decryptWithoutKey();
                            break;
                    }

                }
            }
        }
    }

    public void checkArgs(String[] args, int argsLength) {

        mode = ulti.checkEorD(args[0]);

        filePath = args[1];
        fileExist = ulti.checkExistFile(filePath);

        if (argsLength == 3) {
            key = ulti.checkIsInteger(args[2]);
        } else {
            key = ulti.checkIsInteger(null);
        }
    }

    public void checkError() {
        // Check mode
        if (mode == 0) {
            System.out.println("Wrong mode");
            hasError = true;
        }

        // Check file exist
        if (fileExist == 0) {
            System.out.println("File not found");
            hasError = true;
        }

        // check Key
        if (key == 0) {
            System.out.println("Key is not a number");
            hasError = true;
        } else if (key == -1) {
            System.out.println("Key is lesser than 0");
            hasError = true;
        }
    }

    public void encryptWithKey(int key) {
        // Read file from user input
        ArrayList<Character> readChars = ulti.readFile(filePath);

        System.out.println("Read from File: ");
        for (char c : readChars) {
            System.out.print(c);
        }
        System.out.println("");

        // Check if user key input is larger than the amount of word
        if (allChars.length <= key) {
            System.out.println("Key is larger than the amount of character");
        } else {

            // Make set Char Array based on user key
            char[] newCharArray = new char[allChars.length];
            for (int i = 0; i < allChars.length; i++) {
                // Moving to the left
                // A B C D E
                // C D E A B : key = 2
                if (i - key < 0) {
                    newCharArray[i] = allChars[allChars.length + i - key];
                } else {
                    newCharArray[i] = allChars[i - key];
                }
            }
            replace(readChars, newCharArray);
            ulti.print(mode,readChars);
        }
    }

    public void decryptWithKey(int key) {
        // Read file from user input
        ArrayList<Character> readChars = ulti.readFile(filePath);

        System.out.println("Read from File: ");
        for (char c : readChars) {
            System.out.print(c);
        }

        // Not checking for key and file size
        // Make set CharArray based on user key
        char[] newCharArray = new char[allChars.length];
        for (int i = 0; i < allChars.length; i++) {
            // Moving to the right
            // A B C D E
            // D E A B C : Key = 2
            if (i + key < allChars.length) {
                newCharArray[i] = allChars[i + key];
            } else {
                newCharArray[i] = allChars[(i + key) - allChars.length];
            }
        }
        replace(readChars, newCharArray);
        ulti.print(mode,readChars);
    }

    public void decryptWithoutKey() {
        // Read file from user input
        ArrayList<Character> readChars = ulti.readFile(filePath);

        System.out.println("Read from File: ");
        for (char c : readChars) {
            System.out.print(c);
        }

        // get file name
        File file = new File(filePath);
        String name = file.getName();

        // Create clone Array to run all test without changing readChars Array
        ArrayList<Character> clone;

        // Not checking for key and file size
        // Make set CharArray based on user key
        for (int key = 1; key < allChars.length; key++) {
            clone = new ArrayList<Character>();
            for (char c : readChars) {
                clone.add(c);
            }

            char[] newCharArray = new char[allChars.length];

            // Moving to the right one by one still the end of allChars length
            // A B C D E
            // D E A B C : Key = 2
            for (int i = 0; i < allChars.length; i++) {
                if (i + key < allChars.length) {
                    newCharArray[i] = allChars[i + key];
                } else {
                    newCharArray[i] = allChars[(i + key) - allChars.length];
                }
            }

            replace(clone, newCharArray);
            writeToFile(key, clone, file.getPath(), newCharArray);
        }

        System.out.println("\nWrite to file: " + name + "PossibleDecrypt.txt");

        //replaceAndPrint(readChars, newCharArray);
    }

    public void replace(ArrayList<Character> readChars, char[] newCharArray) {
        // Replace character in readChars with new value
        for (int i = 0; i < readChars.size(); i++) {
            for (int j = 0; j < allChars.length; j++) {

                // Uppercase
                if (readChars.get(i) == allChars[j]) {
                    readChars.set(i, newCharArray[j]);
                    break;
                } // LowerCase
                else if (readChars.get(i) == Character.toLowerCase(allChars[j])) {
                    readChars.set(i, Character.toLowerCase(newCharArray[j]));
                    break;
                }
            }
        }
    }

    public void writeToFile(int key, ArrayList<Character> readChars, String name, char[] newCharArray) {
        // get file's name        
        try {
            // Write to File

            // Remove old file when starting test
            if (key == 1) {
                try (
                        PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(name + "PossibleDecrypt.txt", false)))) {
                    writer.println("Key: " + key);
                    for (char c : readChars) {
                        writer.print(c);
                    }
                    writer.println("\n");
                }
            } // Adding more key during test
            else {
                try (
                        PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(name + "PossibleDecrypt.txt", true)))) {
                    writer.println("Key: " + key);
                    for (char c : readChars) {
                        writer.print(c);
                    }
                    writer.println("\n");
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Ceasar.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Ceasar.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Ceasar.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
