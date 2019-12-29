package com.cm_project.physio2go.DatabaseDrivers;

import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * This class holds the methods to perform password encryption, using the SHA 256 algorithm.
 */
public class EncriptionUtils {

    public static String encryptSHA(String password) {

        byte[] inputData = password.getBytes();
        byte[] outputData = new byte[0];

        try {
            outputData = EncriptionUtils.encryptSHA_256(inputData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        BigInteger shaData = new BigInteger(1, outputData);
        return shaData.toString();
    }

    /**
     * Encrypts with SHA256
     *
     * @param data   to be encrypted
     * @return encryptSha
     * @throws Exception
     */
    private static byte[] encryptSHA_256(byte[] data) throws Exception {
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        sha.update(data);
        return sha.digest();
    }
}
