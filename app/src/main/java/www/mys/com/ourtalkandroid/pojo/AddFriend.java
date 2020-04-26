package www.mys.com.ourtalkandroid.pojo;

import com.mayousheng.www.basepojo.BasePoJo;
import com.mayousheng.www.basepojo.FieldDesc;

public class AddFriend extends BasePoJo {

    public static class Type {
        public static final int QUERY = 1;      //搜索
    }

    @FieldDesc(key = "id")
    public Integer id;
    @FieldDesc(key = "nickName")
    public String nickName;                    //用户昵称
    @FieldDesc(key = "fromUser")
    public String fromUser;                    //发起账户
    @FieldDesc(key = "toUser")
    public String toUser;                      //目标账户
    @FieldDesc(key = "addMsg")
    public String addMsg;                      //请求数据
    @FieldDesc(key = "backMsg")
    public String backMsg;                     //回复数据
    @FieldDesc(key = "lastAddId")
    public Integer lastAddId;                  //上个请求Id
    @FieldDesc(key = "addWay")
    public Integer addWay;                     //添加方式
    @FieldDesc(key = "send")
    public boolean send;                       //是否送达
    @FieldDesc(key = "backSend")
    public boolean backSend;                   //回复是否送达
    @FieldDesc(key = "createdAt")
    public long createdAt;
    @FieldDesc(key = "updatedAt")
    public long updatedAt;
    @FieldDesc(key = "accept")
    public boolean accept;
    @FieldDesc(key = "read")
    public boolean read;

    public AddFriend(String jsonStr) {
        super(jsonStr);
    }

}
