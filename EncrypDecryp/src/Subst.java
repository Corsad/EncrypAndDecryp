
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
public class Subst {

    private int mode, fileExist, keyExist;
    private String filePath, keyPath;
    private boolean hasError = false;
    private Ulti ulti;
    char[] allChars;
    boolean overwriteFile = true;

    public static void main(String[] args) {
        Subst subst = new Subst();
        subst.run(args);
    }

    public void run(String[] args) {
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
                        // mode encryp with both input file and input key
                        // Subst e input key
                        case 1:
                            encryptWithKey();
                            break;
                        // mode decryp with both input file and input key
                        // Subst d input key
                        case 2:
                            decryptWithKey();
                            break;
                        // Subst g input key
                        case 3:
                            System.out.println("Too many arguments for generate mode.");
                            break;
                    }
                } else if (args.length == 2) {
                    switch (mode) {
                        // mode encryp
                        // Subst e input
                        case 1:
                            System.out.println("Not supporting encryption without key.");
                            break;
                        // mode decryp without key
                        // Subst d input 
                        case 2:
                            decryptWithoutKey();
                            break;

                        // // Subst g key
                        case 3:
                            generate();
                            break;

                    }

                }
            }
        }
    }

    public void checkArgs(String[] args, int argsLength) {

        mode = ulti.checkEorDorG(args[0]);

        filePath = args[1];
        fileExist = ulti.checkExistFile(filePath);

        // Subt g keyPath
        if (args.length == 2 && mode == 3) {
            keyPath = args[1];
            keyExist = 1;
        } // Decrypt or encrypt with key
        // Subt e/d inputfilename keyfilename
        else if (args.length == 3) {
            keyPath = args[2];
            keyExist = ulti.checkExistFile(filePath);
        } // Incase Encryp without key
        // Subt e input
        else {
            keyPath = null;
            keyExist = 0;
        }
    }

    public void checkError() {
        // Check mode
        if (mode == 0) {
            System.out.println("Wrong mode");
            hasError = true;
        }

        // Check file exist
        if (fileExist == 0 && mode != 3) {
            System.out.println("File not found");
            hasError = true;
        }

        if (mode == 1 && keyExist == 0) {
            System.out.println("Please input key file to encryp");
            hasError = true;
        }
    }

    public void generate() {
        ArrayList<Character> clone = new ArrayList<Character>();
        for (int i = 0; i < allChars.length; i++) {
            clone.add(allChars[i]);
        }

        long seed = System.nanoTime();
        Collections.shuffle(clone, new Random(seed));

        writeGeneratekey(clone);

        System.out.println("Done generate to file: " + keyPath);
    }

    public void encryptWithKey() {
        ArrayList<Character> readChars = ulti.readFile(filePath);
        ArrayList<Character> keyArray = ulti.readFile(keyPath);

        System.out.print("Read from File: \n>");
        for (char c : readChars) {
            System.out.print(c);
        }
        System.out.print("<\n");

        if (keyArray.size() != allChars.length) {
            System.out.println("The amount of char inside key file is not equal with the total given chars");
        } else {
            char[] newCharArray = new char[allChars.length];
            for (int i = 0; i < readChars.size(); i++) {
                System.out.println(readChars.get(i));

                readChars.set(i, getCharForEncrypt(readChars.get(i), keyArray));
            }
        }

        ulti.print(mode, readChars);
    }

    public void decryptWithKey() {
        ArrayList<Character> readChars = ulti.readFile(filePath);
        ArrayList<Character> keyArray = ulti.readFile(keyPath);

        System.out.print("Read from File: \n>");
        for (char c : readChars) {
            System.out.print(c);
        }
        System.out.print("<\n");
        
        if (keyArray.size() != allChars.length) {
            System.out.println("The amount of char inside key file is not equal with the total given chars");
        } else {
            char[] newCharArray = new char[allChars.length];
            for (int i = 0; i < readChars.size(); i++) {
                readChars.set(i, getCharForDecrypt(readChars.get(i), keyArray));
            }
        }

        ulti.print(mode, readChars);
    }

    public void decryptWithoutKey() {
        ArrayList<Character> readChars = ulti.readFile(filePath);

        runRecursive(readChars);

        System.out.println("\nWrite to file: " + filePath + "PossibleDecrypt.txt");
    }

    public void runRecursive(ArrayList<Character> readChars) {
        // 'A' 'B' 'C'
        ArrayList<Character> allCharsArrayList = new ArrayList<Character>();

        for (char c : allChars) {
            allCharsArrayList.add(c);
        }

        ArrayList<Character> emptyBuildArray = new ArrayList<Character>();
        
        generateRandomChar(0, allCharsArrayList, emptyBuildArray);
    }

    // A| B C
    // B| C
    // C|
    // 
    public void generateRandomChar(int position, ArrayList<Character> availableChars, ArrayList<Character> currentBuildKeyArray) {
        if (availableChars.size() != 0) {
            ArrayList<Character> cloneAvailableChars;
            ArrayList<Character> cloneCurrentBuildArray;
            for (int i = 0; i < availableChars.size(); i++) {

                cloneAvailableChars = new ArrayList<Character>();

                for (char c : availableChars) {
                    cloneAvailableChars.add(c);
                }

                cloneCurrentBuildArray = new ArrayList<Character>();

                for (char c : currentBuildKeyArray) {
                    cloneCurrentBuildArray.add(c);
                }

                cloneCurrentBuildArray.add(availableChars.get(i));
                cloneAvailableChars.remove(i);
                generateRandomChar(position + 1, cloneAvailableChars, cloneCurrentBuildArray);
            }
        } else {
            ArrayList<Character> readChars = ulti.readFile(filePath);

            char[] newCharArray = new char[allChars.length];
            for (int i = 0; i < readChars.size(); i++) {
                readChars.set(i, getCharForDecrypt(readChars.get(i), currentBuildKeyArray));
            }
            
            writeAllPossibleDecrypt(readChars, currentBuildKeyArray);

        }
    }

    public char getCharForEncrypt(char c, ArrayList<Character> keyArray) {
        for (int i = 0; i < keyArray.size(); i++) {
            if (allChars[i] == c || Character.toLowerCase(allChars[i]) == c) {
                System.out.println(allChars[i] + " = " + c + " = " + keyArray.get(i));
                return keyArray.get(i);
            }
        }
        return ' ';
    }

    public char getCharForDecrypt(char c, ArrayList<Character> keyArray) {
        for (int i = 0; i < keyArray.size(); i++) {
            if (keyArray.get(i) == c || Character.toLowerCase(allChars[i]) == c) {
                return allChars[i];
            }
        }
        return ' ';
    }

    public void writeGeneratekey(ArrayList<Character> clone) {
        // get file's name        
        try {
            // Write to File
            try (
                    PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(keyPath, false)))) {
                for (char c : clone) {
                    writer.print(c);
                }
            }
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Ceasar.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Ceasar.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void writeAllPossibleKey(ArrayList<Character> keyArray) {
        // get file's name        
        try {
            if (overwriteFile) {
                try (
                        PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(filePath + "PossibleDecrypt.txt", false)))) {
                    writer.print("Key: \n");
                    for (char c : keyArray) {
                        writer.print(c);
                    }
                    writer.print("\n\n");
                }

                overwriteFile = false;
            } else {
                try (
                        PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(filePath + "PossibleDecrypt.txt", true)))) {
                    writer.print("Key: \n");
                    for (char c : keyArray) {
                        writer.print(c);
                    }

                    writer.print("\n\n");
                }
            }
            // Write to File

        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Ceasar.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Ceasar.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void writeAllPossibleDecrypt(ArrayList<Character> readArray, ArrayList<Character> keyArray) {
        // get file's name        
        try {
            if (overwriteFile) {
                try (
                        PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(filePath + "PossibleDecrypt.txt", false)))) {
                    writer.print("Key: \n");
                    for (char c : keyArray) {
                        writer.print(c);
                    }
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
                    writer.print("Key: \n");
                    for (char c : keyArray) {
                        writer.print(c);
                    }
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
