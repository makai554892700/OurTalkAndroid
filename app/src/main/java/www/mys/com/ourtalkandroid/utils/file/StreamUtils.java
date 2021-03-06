package www.mys.com.ourtalkandroid.utils.file;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.SequenceInputStream;

import www.mys.com.ourtalkandroid.utils.LogUtils;

public class StreamUtils {

    public static String sequenceInputStream2Str(SequenceInputStream inputStream) {
        return inputStream2Str(inputStream);
    }

    public static String inputStream2Str(InputStream inputStream) {
        if (inputStream == null) {
            return null;
        }
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line;
        StringBuilder stringBuilder = new StringBuilder();
        do {
            try {
                line = bufferedReader.readLine();
            } catch (Exception e) {
                LogUtils.log("e=" + e);
                return stringBuilder.toString();
            }
            if (line != null) {
                stringBuilder.append(line);
                stringBuilder.append("\n");
            }
        } while (line != null);
        CloseUtils.closeSilently(bufferedReader);
        CloseUtils.closeSilently(inputStreamReader);
        return stringBuilder.toString();
    }

    public static boolean inputStream2OutputStream(InputStream inputStream, OutputStream outputStream) {
        if (inputStream != null && outputStream != null) {
            byte[] tempByte = new byte[1024];
            int len;
            try {
                while ((len = inputStream.read(tempByte)) != -1) {
                    outputStream.write(tempByte, 0, len);
                }
                return true;
            } catch (Exception e) {
                LogUtils.log("e=" + e);
            }
        }
        return false;
    }

}
