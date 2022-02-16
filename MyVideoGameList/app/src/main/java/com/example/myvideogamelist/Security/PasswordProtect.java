package com.example.myvideogamelist.Security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;

public class PasswordProtect {

    private static String key = "maClefgeniale";

    /**
     * Compare a hashed password with a password just entered by the user
     * @param userPassword the password the user entered
     * @param dbUserPassword user's password in the db
     * @return true if passwords matches
     */
    public static boolean comparePassword(String userPassword, String dbUserPassword){
        String uPassword = hashPassword(userPassword);
        return uPassword.compareTo(dbUserPassword) == 0 ? true : false;
    }

    /**
     * Hash a password with a key to compare it later
     * @param password password to hash
     * @return String the password hashed
     */
    public static String hashPassword(String password){
        try {
            byte[] salt = key.getBytes();
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt);
            return new String( md.digest(password.getBytes(StandardCharsets.UTF_8)));
        }
        catch (Exception e){ e.printStackTrace(); }
        return null;
    }

    public static void setKey(String key) {
        PasswordProtect.key = key;
    }
}
