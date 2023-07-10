package Clinimate.gui;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import org.mindrot.jbcrypt.BCrypt;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordGenerator {
    
    public static String generatePassword(String password) {
    return BCrypt.hashpw(password, BCrypt.gensalt(10));
    }
    
    public static boolean verifyPassword(String password, String hash) {
        return BCrypt.checkpw(password, hash);
    }
}


