package www.mys.com.ourtalkandroid.utils.db.model;

import com.mayousheng.www.basepojo.BasePoJo;
import com.mayousheng.www.basepojo.FieldDesc;

import java.io.Serializable;

public class DBChatList extends BasePoJo implements Serializable {

    public static final String ID = "_id";
    public static final String TYPE = "type";
    public static final String UNREAD_COUNT = "unread_count";
    public static final String CHAT_KEY = "chat_key";
    public static final String IMG_URL = "img_url";
    public static final String USER_NAME = "user_name";
    public static final String MSG_TITLE = "msg_title";
    public static final String MSG_DESC = "msg_desc";
    public static final String MSG_TIME = "msg_time";

    @FieldDesc(key = "id")
    public int id;
    @FieldDesc(key = "type")
    public int type;
    @FieldDesc(key = "unReadCount")
    public int unReadCount;
    @FieldDesc(key = "chatKey")
    public String chatKey;
    @FieldDesc(key = "imgUrl")
    public String imgUrl;
    @FieldDesc(key = "userName")
    public String userName;
    @FieldDesc(key = "msgTitle")
    public String msgTitle;
    @FieldDesc(key = "msgDesc")
    public String msgDesc;
    @FieldDesc(key = "msgTime")
    public Long msgTime;

    public DBChatList(String jsonStr) {
        super(jsonStr);
    }

    public DBChatList(int type, int unReadCount, String chatKey, String imgUrl, String userName, String msgTitle, String msgDesc, Long msgTime) {
        super(null);
        this.type = type;
        this.unReadCount = unReadCount;
        this.chatKey = chatKey;
        this.imgUrl = imgUrl;
        this.userName = userName;
        this.msgTitle = msgTitle;
        this.msgDesc = msgDesc;
        this.msgTime = msgTime;
    }
}
