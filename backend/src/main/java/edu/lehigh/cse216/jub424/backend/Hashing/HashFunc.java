package edu.lehigh.cse216.jub424.backend.Hashing;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

/**
 * Hash function
 */
public class HashFunc {

    /**
     * getSHA get instance SHA-256
     * @param get the string to be hashed
     * @return byte array
     * @throws NoSuchAlgorithmException throws NoSuchAlgorithmException
     */
    public static byte[] getSHA(String get) throws NoSuchAlgorithmException {
        MessageDigest messd = MessageDigest.getInstance("SHA-256");
        return messd.digest(get.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Make a byte array to hex string
     * @param hash a byte array
     * @return hex string
     */
    public static String toHexString(byte[] hash) {
        BigInteger number = new BigInteger(1, hash);
        StringBuilder hexString = new StringBuilder(number.toString(16));
        while (hexString.length() < 64) {
            hexString.insert(0, '0');
        }
        return hexString.toString();
    }

    /**
     * func used to hash
     * @param toHash the string need to be hashed
     * @return 64 hashed string
     */
    public static String hash(String toHash) {
        try {
            return toHexString(getSHA(toHash));
        } catch (Exception e) {
            System.out.println("FATAL HASHING ERROR, SHUTTING DOWN");
            System.exit(1);
        }
        return "";
    }
}
