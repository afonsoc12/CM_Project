package com.cm_project.physio2go.databaseDrivers;

import java.security.MessageDigest;

public class sha {
    /**
     * @param data   to be encrypted
     * @param sha256 method
     * @return encryptSha
     * @trhow Exception
     */

    public static byte[] encryptSHA_256(byte[] data, String sha256) throws Exception {
        MessageDigest sha = MessageDigest.getInstance(sha256);
        sha.update(data);
        return sha.digest();
    }
}
