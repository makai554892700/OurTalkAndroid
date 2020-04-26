package www.mys.com.ourtalkandroid.pojo;

import com.mayousheng.www.basepojo.BasePoJo;
import com.mayousheng.www.basepojo.FieldDesc;

public class FriendShip extends BasePoJo {

    @FieldDesc(key = "id")
    public Integer id;
    @FieldDesc(key = "fromUser")
    public String fromUser;                    //发起账户
    @FieldDesc(key = "toUser")
    public String toUser;                      //目标账户
    @FieldDesc(key = "descName")
    public String descName;                    //备注名
    @FieldDesc(key = "headImg")
    public String headImg;                     //用户头像(toUser)
    @FieldDesc(key = "block")
    public boolean block;                      //是否拉黑
    @FieldDesc(key = "addWay")
    public Integer addWay;                      //添加方式
    @FieldDesc(key = "ruleDesc")
    public String ruleDesc;                    //权限相关
    @FieldDesc(key = "unReadCount")
    public int unReadCount;                     //未读消息条数
    @FieldDesc(key = "createdAt")
    public long createdAt;
    @FieldDesc(key = "updatedAt")
    public long updatedAt;
    public String itemKey;

    public FriendShip(String jsonStr) {
        super(jsonStr);
    }

    public FriendShip(String fromUser, String toUser, String descName, String headImg, Integer addWay) {
        super(null);
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.descName = descName;
        this.headImg = headImg;
        this.addWay = addWay;
    }

    public FriendShip(String toUser, String descName, String headImg, Integer addWay) {
        super(null);
        this.toUser = toUser;
        this.descName = descName;
        this.headImg = headImg;
        this.addWay = addWay;
    }
}
