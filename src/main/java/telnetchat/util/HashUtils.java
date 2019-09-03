package telnetchat.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtils {
    public static String md5(String str) {
        try {
            StringBuilder hexStringBuilder = new StringBuilder();
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] hash = digest.digest(str.getBytes());
            for (int i = 0; i < hash.length; i++) {
                if ((0xff & hash[i]) < 0x10) {
                    hexStringBuilder.append("0")
                            .append(Integer.toHexString((0xFF & hash[i])));
                } else {
                    hexStringBuilder.append(Integer.toHexString(0xFF & hash[i]));
                }
            }
            return hexStringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return str;
    }
}
