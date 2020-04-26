package www.mys.com.ourtalkandroid.utils.encrypt;

import www.mys.com.ourtalkandroid.base.StaticParam;

public class EncryptUtils {

    public static String encrypt(String data) {
        return RC4Utils.enCode(StaticParam.ENCRYPT_CODE, data);
    }

    public static String decrypt(String data) {
        return RC4Utils.deCode(StaticParam.ENCRYPT_CODE, data);
    }

    private static boolean testEd = false;

    public static String encrypt(String key, String data) {
        return RC4Utils.enCode(key, data);
    }

    public static String decrypt(String key, String data) {
        return RC4Utils.deCode(key, data);
    }

}
