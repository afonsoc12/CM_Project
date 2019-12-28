package com.cm_project.physio2go.DatabaseDrivers;

import java.math.BigInteger;
import java.security.MessageDigest;

public class EncriptionUtils {

    public static String encryptSHA(String password) {

        byte[] inputData = password.getBytes();
        byte[] outputData = new byte[0];

        try {
            outputData = EncriptionUtils.encryptSHA_256(inputData, "SHA-256");
        } catch (Exception e) {
            e.printStackTrace();
        }
        BigInteger shaData = new BigInteger(1, outputData);
        String shaEncrypted = shaData.toString();
        return shaEncrypted;
    }

    /**
     * @param data   to be encrypted
     * @param sha256 method
     * @return encryptSha
     * @trhow Exception
     */

    private static byte[] encryptSHA_256(byte[] data, String sha256) throws Exception {
        MessageDigest sha = MessageDigest.getInstance(sha256);
        sha.update(data);
        return sha.digest();
    }
}
