package com.zzh.lib.utils.encrypt;

import java.security.MessageDigest;

public class HShaUtils {
    private HShaUtils() {
    }

    public static String SHA1(String content) {
        try {
            final MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            messageDigest.update(content.getBytes("utf-8"));
            final byte[] bytes = messageDigest.digest();
            return String.valueOf(HHexUtils.encodeHex(bytes, true));
        } catch (Exception e) {
            return content;
        }
    }
}
