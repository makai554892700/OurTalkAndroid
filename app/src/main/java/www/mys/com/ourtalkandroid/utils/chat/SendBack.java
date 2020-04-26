package www.mys.com.ourtalkandroid.utils.chat;

public class SendBack {
    public static final class STATE {
        public static final int SUCCESS = 200;
        public static final int FAILED = -1;
    }

    public int code;
    public String errorMsg;
    public String data;

    public SendBack() {
    }

    public SendBack(int code, String errorMsg, String data) {
        this.code = code;
        this.errorMsg = errorMsg;
        this.data = data;
    }

    @Override
    public String toString() {
        return "SendBack{" +
                "code=" + code +
                ", errorMsg='" + errorMsg + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}