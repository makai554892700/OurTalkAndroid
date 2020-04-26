package www.mys.com.ourtalkandroid.utils;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {

    public static String MD5(String sSecret, boolean is16) {
        String result = "";
        try {
            MessageDigest bmd5 = MessageDigest.getInstance("MD5");
            bmd5.update(sSecret.getBytes(Charset.forName("utf-8")));
            StringBuilder buf = new StringBuilder();
            byte[] b = bmd5.digest();// 加密
            for (int i : b) {
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString();
            result = is16 ? result.substring(8, 24) : result;
        } catch (NoSuchAlgorithmException e) {
            System.out.println("e=" + e);
        }
        return result.toUpperCase();
    }

}
