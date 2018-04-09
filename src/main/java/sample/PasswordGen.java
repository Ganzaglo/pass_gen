package sample;

import java.util.ArrayList;
import java.util.Collections;

public class PasswordGen {
    private static ArrayList<String> password = new ArrayList<>();

    private PasswordGen() {
    }

    public static String getPassword(int passLength, boolean upperCase, boolean lowerCase, boolean numbers, boolean symbols) {
        int count = 0;
        if (upperCase) count += 1;
        if (lowerCase) count += 1;
        if (numbers) count += 1;
        if (symbols) {
            count += 1;
        }
        int temp = (passLength / count);
        if (passLength<8){temp+=1;}else temp+=2;


        if (upperCase) {
            for (int i = 0; i < temp; i++) {
                password.add(randomU());
            }
        }
        if (lowerCase) {
            for (int i = 0; i < temp; i++) {
                password.add(randomL());
            }
        }
        if (numbers) {
            for (int i = 0; i < temp; i++) {
                password.add(randomN());
            }
        }
        if (symbols) {
            for (int i = 0; i < temp; i++) {
                password.add(randomS());
            }
        }
        Collections.shuffle(password);


        String outPassword = "";
        for (int i = 0; i < passLength; i++) {
            outPassword += password.get(i);
        }
        password.clear();
        return outPassword;
    }

    private static String randomU() {
        return randomL().toUpperCase();
    }

    private static String randomN() {
        return "" + ((int) (Math.random() * 10));
    }

    private static String randomS() {
        byte b = (byte) (64 - (Math.random() * 30));
        if (b >= 48 && b <= 57) {
            b = (byte) (96 - (Math.random() * 5));
        }
        byte[] bytes = {b};
        return new String(bytes);
    }

    private static String randomL() {
        byte b = (byte) (122 - (Math.random() * 25));
        byte[] bytes = {b};
        return new String(bytes);
    }
}
