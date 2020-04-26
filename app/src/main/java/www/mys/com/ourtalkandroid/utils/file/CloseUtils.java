package www.mys.com.ourtalkandroid.utils.file;

import java.io.Closeable;
import java.io.Reader;

import www.mys.com.ourtalkandroid.utils.LogUtils;

public class CloseUtils {

    public static void closeSilently(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
                LogUtils.log("e=" + e);
            }
        }
    }

    public static void closeReader(Reader reader) {
        if (reader != null) {
            try {
                reader.close();
            } catch (Exception e) {
                LogUtils.log("e=" + e);
            }
        }
    }

}