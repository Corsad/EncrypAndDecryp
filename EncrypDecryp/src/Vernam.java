
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
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
public class Vernam {

    private int mode, fileExist, keyExist, key;
    private String filePath, keyPath;
    private boolean hasError = false;
    private Ulti ulti;
    char[] allChars;
    boolean overwriteFile = true;

    public static void main(String[] args) {
        Vernam vernam = new Vernam();
        vernam.runArgs(args);
    }

    public void runArgs(String[] args) {
        if (args.length < 3) {
            System.out.println("Not enough arguement");
        } else {
            ulti = new Ulti();
            checkArgs(args, args.length);
            checkError();

            if (!hasError) {
                // get char list from Ulti
                allChars = ulti.getAllChars();

                switch (mode) {
                    // mode encryp
                    case 1:
                        encrypt();
                        break;
                    // mode decryp
                    case 2:
                        if (args.length == 4) {
                            decrypt(false);
                        } else {
                            decryptWithoutKey();
                        }
                        break;
                    // generate
                    case 3:
                        generateKey();
                        break;
                }

            }
        }
    }

    public void checkArgs(String[] args, int argsLength) {

        mode = ulti.checkEorDorG(args[0]);

        filePath = args[1];

        if (mode != 3) {
            fileExist = ulti.checkExistFile(filePath);

            keyPath = args[2];
            keyExist = ulti.checkExistFile(filePath);

            if (argsLength == 4) {
                keyExist = ulti.checkIsInteger(args[3]);
                key = Integer.parseInt(args[3]);
            } else {
                key = 0;
            }
        } else {
            fileExist = 1;

            keyExist = ulti.checkIsInteger(args[2]);
            if (keyExist == 1) {
                key = Integer.parseInt(args[2]);
            }
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
        if (keyExist == 0) {
            System.out.println("Key is not a number");
            hasError = true;
        } else if (key == -1) {
            System.out.println("Key is lesser than 0");
            hasError = true;
        }
    }

    public ArrayList<Integer> turnChartoIntArray(ArrayList<Character> charArray) {
        ArrayList<Integer> intArray = new ArrayList<Integer>();
        for (char c : charArray) {
            for (int i = 0; i < allChars.length; i++) {
                if (c == allChars[i] || c == Character.toLowerCase(allChars[i])) {
                    intArray.add(i);
                    break;
                }
            }
        }

        return intArray;
    }

    public ArrayList<Character> turnIntToCharArray(ArrayList<Integer> intArray) {
        ArrayList<Character> charArray = new ArrayList<Character>();
        for (int i : intArray) {
            charArray.add(allChars[i]);
        }

        return charArray;
    }

    public void encrypt() {
        // Read file from user input
        ArrayList<Character> readChars = ulti.readFile(filePath);
        ArrayList<Character> keyChars = ulti.readFile(keyPath);

        System.out.print("Read from File: \n>");
        for (char c : readChars) {
            System.out.print(c);
        }
        System.out.print("<\n");

        // Key should be equal or bigger than file
        // (keyChars.size() - key) : get key with offset
        if (readChars.size() <= (keyChars.size() - key)) {

            ArrayList<Integer> inputAsIntArray = turnChartoIntArray(readChars);
            ArrayList<Integer> keyAsIntArray = turnChartoIntArray(keyChars);

            ArrayList<Integer> encryptedArray = new ArrayList<Integer>();
            for (int c : inputAsIntArray) {
                encryptedArray.add(c);
            }

            for (int i = 0; i < inputAsIntArray.size(); i++) {
                encryptedArray.set(i, inputAsIntArray.get(i) + keyAsIntArray.get(i + key));

                // 
                if (encryptedArray.get(i) >= allChars.length) {
                    encryptedArray.set(i, encryptedArray.get(i) - allChars.length);
                }
            }

            ulti.print(mode, turnIntToCharArray(encryptedArray));
        } else {
            System.out.println("Key is too short.");
        }
    }

    public void decrypt(boolean print) {
        // Read file from user input
        ArrayList<Character> readChars = ulti.readFile(filePath);
        ArrayList<Character> keyChars = ulti.readFile(keyPath);

        if (!print) {
            System.out.print("Read from File: \n>");
            for (char c : readChars) {
                System.out.print(c);
            }
            System.out.print("<\n");
        }
        
        // Key should be equal or bigger than file
        // (keyChars.size() - key) : get key with offset
        if (readChars.size() <= (keyChars.size() - key)) {

            ArrayList<Integer> inputAsIntArray = turnChartoIntArray(readChars);
            ArrayList<Integer> keyAsIntArray = turnChartoIntArray(keyChars);

            ArrayList<Integer> encryptedArray = new ArrayList<Integer>();
            for (int c : inputAsIntArray) {
                encryptedArray.add(c);
            }

            for (int i = 0; i < inputAsIntArray.size(); i++) {
                encryptedArray.set(i, inputAsIntArray.get(i) - keyAsIntArray.get(i + key));

                if (encryptedArray.get(i) < 0) {
                    encryptedArray.set(i, encryptedArray.get(i) + allChars.length);
                }

            }
            
            if(!print){
                ulti.print(mode, turnIntToCharArray(encryptedArray));
            } else {
                writeAllPossibleDecrypt(key, turnIntToCharArray(encryptedArray));
            }
            
        } else {
            System.out.println("Key is too short.");
        }
    }

    public void generateKey() {
        ArrayList<Character> keyArray = new ArrayList<Character>();

        ArrayList<Character> clone = new ArrayList<Character>();
        for (int i = 0; i < allChars.length; i++) {
            clone.add(allChars[i]);
        }

        for (int i = 0; i < key; i++) {
            long seed = System.nanoTime();
            Collections.shuffle(clone, new Random(seed));
            keyArray.add(clone.get(0));
        }

        writeGeneratekey(keyArray);
        System.out.println("\nWrite to file: " + filePath);
    }

    public void writeGeneratekey(ArrayList<Character> keyArray) {
        // get file's name        
        try {
            // Write to File
            try (
                    PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(filePath, false)))) {
                for (char c : keyArray) {
                    writer.print(c);
                }
            }
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Ceasar.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Ceasar.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void decryptWithoutKey() {
        ArrayList<Character> readChars = ulti.readFile(filePath);
        ArrayList<Character> keyChars = ulti.readFile(keyPath);
        if (readChars.size() <= (keyChars.size() - key)) {
            for (int i = 0; i < keyChars.size() - readChars.size(); i++) {
                key = i;
                decrypt(true);
            }
        } else {
            System.out.println("Key is too short.");
        }

        System.out.println("\nWrite to file: " + filePath + "PossibleDecrypt.txt");
    }

    public void writeAllPossibleDecrypt(int key, ArrayList<Character> readArray) {
        // get file's name        
        try {
            if (overwriteFile) {
                try (
                        PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(filePath + "PossibleDecrypt.txt", false)))) {
                    writer.println("Key: " + key);
                    writer.print("\nDecrypt: \n>");
                    for (char c : readArray) {
                        writer.print(c);
                    }

                    writer.print("<\n\n");
                }

                overwriteFile = false;
            } else {
                try (
                        PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(filePath + "PossibleDecrypt.txt", true)))) {
                    writer.println("Key: " + key);
                    writer.print("\nDecrypt: \n>");
                    for (char c : readArray) {
                        writer.print(c);
                    }

                    writer.print("<\n\n");
                }
            }
            // Write to File

        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Ceasar.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Ceasar.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
