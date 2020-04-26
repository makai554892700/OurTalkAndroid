package www.mys.com.ourtalkandroid.pojo;

import com.mayousheng.www.basepojo.BasePoJo;
import com.mayousheng.www.basepojo.FieldDesc;

import java.util.Date;

import www.mys.com.ourtalkandroid.base.StaticParam;

public class User extends BasePoJo {

    @FieldDesc(key = "id")
    public Integer id;
    @FieldDesc(key = "userName")
    public String userName;        //账户	String(unique)
    @FieldDesc(key = "msg")
    public String msg;             //个性签名	String
    @FieldDesc(key = "passWord")
    public String passWord;        //密码	String
    @FieldDesc(key = "imgUrl")
    public String imgUrl;          //头像图片url	String
    @FieldDesc(key = "backImgUrl")
    public String backImgUrl;          //背景图片url	String
    @FieldDesc(key = "deviceType")
    public Integer deviceType = 1;     //设备类型 0:web/1:Android/2:IOS
    @FieldDesc(key = "packageName")
    public String packageName = StaticParam.PACKAGE_NAME;          //包名   String
    @FieldDesc(key = "deviceId")
    public Integer deviceId = -1;            //设备id	int
    @FieldDesc(key = "nickName")
    public String nickName;        //昵称	String
    @FieldDesc(key = "sex")
    public Integer sex;            //性别	int
    @FieldDesc(key = "userDesc")
    public String userDesc;             //个性签名	String
    @FieldDesc(key = "email")
    public String email;           //邮箱	String
    @FieldDesc(key = "phone")
    public String phone;           //电话号码	String
    @FieldDesc(key = "photo")
    public String photo;          //头像图片url	String
    @FieldDesc(key = "backPhoto")
    public String backPhoto;          //背景图片url	String
    @FieldDesc(key = "zone")
    public Integer zone;            //地区	int
    @FieldDesc(key = "lastLoginIP")
    public String lastLoginIP;          //最后登录ip	String
    @FieldDesc(key = "lastLoginTime")
    public Date lastLoginTime;          //最后登录时间 date
    @FieldDesc(key = "available")
    public boolean available;          //账号是否可用
    @FieldDesc(key = "createdAt")
    public Date createdAt;
    @FieldDesc(key = "updatedAt")
    public Date updatedAt;

    public User(String jsonStr) {
        super(jsonStr);
    }

    public User(String userName, String passWord, String nickName) {
        super(null);
        this.userName = userName;
        this.passWord = passWord;
        this.nickName = nickName;
    }
}
