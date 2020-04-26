package www.mys.com.ourtalkandroid.utils.db.model;

import com.mayousheng.www.basepojo.BasePoJo;
import com.mayousheng.www.basepojo.FieldDesc;

import java.io.Serializable;

public class DBOperateInfo extends BasePoJo implements Serializable {
    public static final String ID = "_id";
    public static final String DATA_KEY = "data_key";
    public static final String DATA = "data";

    @FieldDesc(key = "id")
    public int id;
    @FieldDesc(key = "data_key")
    public String data_key;
    @FieldDesc(key = "data")
    public String data;

    public DBOperateInfo(String jsonStr) {
        super(jsonStr);
    }

    public DBOperateInfo(String key, String data) {
        super(null);
        this.data_key = key;
        this.data = data;
    }

}
