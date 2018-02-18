package cryptopack;

import java.security.*;
import java.security.spec.*;
import java.util.Arrays;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;

/**
 * https://www.javacodegeeks.com/2012/05/secure-password-storage-donts-dos-and.html
 */
public class PasswordCrypto {
    public static boolean authenticate(String attemptedPassword, byte[] encryptedPassword, byte[] salt)
        throws NoSuchAlgorithmException, InvalidKeySpecException{

        byte[] encryptedAttemptPassword=getEncryptedPassword(attemptedPassword,salt);

        return Arrays.equals(encryptedAttemptPassword, encryptedPassword);
    }

    public static byte[] getEncryptedPassword(String password, byte[] salt)
        throws NoSuchAlgorithmException, InvalidKeySpecException{

        String algorithm="PBKDF2WithHmacSHA1";
        int derivedKeyLength=160;
        int iterations=20000;

        KeySpec spec=new PBEKeySpec(password.toCharArray(),salt,iterations,derivedKeyLength);
        SecretKeyFactory f=SecretKeyFactory.getInstance(algorithm);

        return f.generateSecret(spec).getEncoded();
    }

    public static byte[] generateSalt() throws NoSuchAlgorithmException{
        SecureRandom random=SecureRandom.getInstance("SHA1PRNG");
        byte[] salt=new byte[8];
        random.nextBytes(salt);

        return salt;
    }
}
