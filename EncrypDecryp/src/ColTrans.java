
import java.io.BufferedWriter;
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
public class ColTrans {

    private int mode, fileExist, key;
    private String filePath;
    private boolean hasError = false;
    private Ulti ulti;
    char[] allChars;
    boolean overwriteFile = true;

    public static void main(String[] args) {
        ColTrans colTrans = new ColTrans();
        colTrans.runArgs(args);
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
                        // ColTrans e inputfilename key
                        case 1:
                            encryptWithKey(Integer.parseInt(args[2]));
                            break;
                        // mode decryp
                        // ColTrans d inputfilename key
                        case 2:
                            decryptWithKey(Integer.parseInt(args[2]), false);
                            break;
                    }
                } else if (args.length == 2) {
                    switch (mode) {
                        // mode encryp
                        // ColTrans e inputfilename
                        case 1:
                            System.out.println("Not supporting encryption without key");
                            break;
                        // mode decryp
                        // ColTrans d inputfilename
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

        System.out.print("Read from File: \n>");
        for (char c : readChars) {
            System.out.print(c);
        }
        System.out.print("<\n");

        // Check if user key input is larger than the amount of word
        if (readChars.size() <= key) {
            System.out.println("Key is larger than the amount of character");
        } else {

            // get lines
            int line;
            if (readChars.size() % key != 0) {
                line = (readChars.size() / key) + 1;
            } else {
                line = readChars.size() / key;
            }

            ArrayList<Character> encryptedArray = new ArrayList<Character>();
            for (int i = 0; i < key; i++) {
                for (int j = 0; j < line; j++) {
                    if (i + (key * j) >= readChars.size()) {
                        encryptedArray.add(' ');
                    } else {
                        encryptedArray.add(readChars.get(i + (key * j)));
                    }
                }

            }

            ulti.print(mode, encryptedArray);
        }
    }

    public void decryptWithKey(int key, boolean print) {
        // Read file from user input
        ArrayList<Character> readChars = ulti.readFile(filePath);

        if (!print) {
            System.out.print("Read from File: \n>");
            for (char c : readChars) {
                System.out.print(c);
            }
            System.out.print("<\n");
        }
        // Check if user key input is larger than the amount of word
        if (readChars.size() <= key) {
            System.out.println("Key is larger than the amount of character");
        } else if (readChars.size() % key != 0) {
            System.out.println("The amount of letter can't be divided to the key");
        } else {
            ArrayList<Character> decryptedArray = new ArrayList<Character>();

            int col = readChars.size() / key;

            for (int i = 0; i < col; i++) {
                for (int j = 0; j < key; j++) {
                    if (i + (col * j) < readChars.size()) {
                        decryptedArray.add(readChars.get(i + (col * j)));
                    }
                }
            }

            if (print) {
                writeAllPossibleDecrypt(key, decryptedArray);
            } else {
                ulti.print(mode, decryptedArray);
            }
        }
    }

    public void decryptWithoutKey() {
        // Read file from user input
        ArrayList<Character> readChars = ulti.readFile(filePath);

        System.out.print("Read from File: \n>");
        for (char c : readChars) {
            System.out.print(c);
        }
        System.out.print("<\n");

        for (int i = 2; i < readChars.size(); i++) {
            if (readChars.size() % i == 0) {
                decryptWithKey(i, true);
            }
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
