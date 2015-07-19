
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
public class Ulti {

    //ABCDEFGHIJKLMNOPQRSTUVWXYZ .,:;()-!?$'"\n0123456789
    private char [] allChars = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'
                        , 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T'
                        , 'U', 'V', 'W', 'X', 'Y', 'Z',' ', '.', ',', ':'
                        , ';', '(', ')', '-', '!', '?', '$', '\'', '"', '\n'
                        , '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

//    private char [] allChars = {'A', 'B', 'C'};
    
    // getter for allChars
    public char[] getAllChars() {
        return allChars;
    }   
    
    // check 1st flag for Ceasar, Transposition
    public int checkEorD(String input) {
        switch (input) {
            case "e":
                return 1;
            case "d":
                return 2;
            default:
                return 0;
        }
    }

    // check 1st flag for Random, Vernam
    public int checkEorDorG(String input) {
        switch (input) {
            case "e":
                return 1;
            case "d":
                return 2;
            case "g":
                return 3;
            default:
                return 0;
        }
    }

    public int checkIsInteger(String input) {
        if(input != null){
            try {
                if (Integer.parseInt(input) > 0) {
                    return 1;
                } else {
                    return -1;
                }
            } catch (NumberFormatException ex) {
                return 0;
            }
        } else {
            return 2;
        }
    }

    // Check file exist based on file path
    public int checkExistFile(String filePath) {
        File file = new File(filePath);
        if (file.exists() && !file.isDirectory()) {
            return 1;
        }
        return 0;
    }

    public ArrayList<Character> readFile(String fileName) {
        ArrayList<Character> chars = new ArrayList<Character>();

        try {
            // read line by line from File
            BufferedReader lines = new BufferedReader(new FileReader(fileName));
            String line = lines.readLine();

            while (line != null) {
                // put char into array
                for (char c : line.toCharArray()) {
                        chars.add(c);
                }
                line = lines.readLine();
                
                if(line != null){
                    chars.add('\n');
                }
            }

        } catch (Exception e) {
            System.out.println("File not Found");
        }
        return chars;
    }
    
    public void swapCharOnArray(ArrayList<Character> charArray, int firstPos, int secPos){
        char temp = charArray.get(firstPos);
        charArray.set(firstPos, charArray.get(secPos));
        charArray.set(secPos, temp);
    }
    
    public void print(int mode ,ArrayList<Character> readChars) {
        // print Encrypted file
        switch (mode) {
            case 1:
                System.out.print("\nEncrypted:\n>");
                break;
            case 2:
                System.out.print("\nDecrypted:\n>");
                break;
        }
        for (char c : readChars) {
            System.out.print(c);
        }
        System.out.print("<\nDone.");
    }
}
