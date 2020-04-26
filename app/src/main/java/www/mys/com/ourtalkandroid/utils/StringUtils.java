package www.mys.com.ourtalkandroid.utils;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    private static final String[] NUM_STR = new String[]{"0", "1", "2"
            , "3", "4", "5", "6", "7", "8", "9"};
    private static final String[] WORD_STR = new String[]{"0", "1", "2"
            , "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E"
            , "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q"
            , "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "a", "b", "c"
            , "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o"
            , "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
    private static final String[] HEX_STR = new String[]{"0", "1", "2"
            , "3", "4", "5", "6", "7", "8", "9", "A", "B"
            , "C", "D", "E", "F"};
    private static final Random random = new Random();

    public static class StrType {
        public static final int NUM = 0;
        public static final int WORD = 1;
        public static final int HEX = 2;
    }

    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static String matchImgName(String str) {
        if (isEmpty(str)) {
            return null;
        }
        return str.substring(str.lastIndexOf("/") + 1);
    }

    public static String getRandomStr(int len) {
        if (len < 1) {
            return "";
        }
        StringBuilder stringBuffer = new StringBuilder();
        for (int i = 0; i < len; i++) {
            stringBuffer.append(HEX_STR[random.nextInt(HEX_STR.length)]);
        }
        return stringBuffer.toString();
    }

    public static String getRandomStr(int len, int type) {
        if (len < 1) {
            return null;
        }
        StringBuilder stringBuffer = new StringBuilder();
        for (int i = 0; i < len; i++) {
            switch (type) {
                case StrType.HEX:
                    stringBuffer.append(HEX_STR[random.nextInt(HEX_STR.length)]);
                    break;
                case StrType.NUM:
                    stringBuffer.append(NUM_STR[random.nextInt(NUM_STR.length)]);
                    break;
                default:
                    stringBuffer.append(WORD_STR[random.nextInt(WORD_STR.length)]);
                    break;
            }
        }
        return stringBuffer.toString();
    }

    public static String matchIP(String str) {
        if (str == null) {
            return null;
        }
        Pattern pattern = Pattern.compile("\\d{1,3}[.\\d]{1,3}[.\\d]{1,3}[.\\d]{1,3}");
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            String result = matcher.group();
            LogUtils.log("matchIP success.result=" + result + ";str=" + str);
            return result;
        } else {
            LogUtils.log("matchIP error.str=" + str);
            return null;
        }
    }

}
