package www.mys.com.ourtalkandroid.utils;

public enum TimeEnum {
    FORMAT_DAY_MSEC("yyyy-MM-dd HH:mm:ss.SSS"),
    FORMAT_DAY_SECOND("yyyy-MM-dd HH:mm:ss"),
    FORMAT_INT_DAY("yyyyMMdd"),
    FORMAT_MINUTE("yyMMdd_HH:mm"),
    FORMAT_DAY("yyyy-MM-dd"),
    FORMAT_FILE_SECOND("yyyyMMdd_HH_mm_ss");
    private String str;

    TimeEnum(String str) {
        this.str = str;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }
}
