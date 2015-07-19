
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
public class Vernam {

    private int mode, fileExist, keyExist, key;
    private String filePath, keyPath;
    private boolean hasError = false;
    private Ulti ulti;
    char[] allChars;

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

                if (args.length == 3) {
                    switch (mode) {
                        // mode encryp
                        case 1:
                            break;
                        // mode decryp
                        case 2:
                            break;
                        // generate
                        case 3:
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
        

        if (mode != 3) {
            keyPath = args[2];
            keyExist = ulti.checkExistFile(filePath);
        } else {
            keyExist = ulti.checkIsInteger(args[2]);
            if(keyExist == 1){
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
        if (key == 0) {
            System.out.println("Key is not a number");
            hasError = true;
        } else if (key == -1) {
            System.out.println("Key is lesser than 0");
            hasError = true;
        }
    }
    
    public ArrayList<Integer> turnChartoNumber(ArrayList<Character> charArray){
        ArrayList<Integer> intArray = new ArrayList<Integer>();
        for( char c : charArray){
            for(int i = 0; i < allChars.length; i++){
                if(c == allChars[i] || c == Character.toLowerCase(allChars[i])){
                    intArray.add(i);
                }
            }
        }
        
        return intArray;
    }
}
