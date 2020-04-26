package www.mys.com.ourtalkandroid.pojo;

import com.mayousheng.www.basepojo.BasePoJo;
import com.mayousheng.www.basepojo.FieldDesc;

public class ObjResult<T> extends BasePoJo {

    @FieldDesc(key = "code")
    public int code;
    @FieldDesc(key = "message")
    public String message;
    @FieldDesc(key = "data")
    public T data;

    public ObjResult(String jsonStr) {
        super(jsonStr);
    }

}
