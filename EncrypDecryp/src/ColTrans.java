
import java.util.ArrayList;

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
                            break;
                        // mode decryp
                        // ColTrans d inputfilename key
                        case 2:
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

    public void encryptWithKey() {
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

        }
    }
}
