package www.mys.com.ourtalkandroid.utils.chat;

import com.mayousheng.www.basepojo.BasePoJo;
import com.mayousheng.www.basepojo.FieldDesc;

import java.util.ArrayList;
import java.util.List;

public class BaseChatPOJO extends BasePoJo {

    public static final class Type {
        public static final int HANDSHAKE = 1;
        public static final int TALK_USER = 2;
        public static final int TALK_GROUP = 3;
        public static final int SEND_BACK = 4;
        public static final int RECEIVE_BACK = 5;
        public static final int ADD_FRIENDS = 6;
    }

    @FieldDesc(key = "type")
    public int type;
    @FieldDesc(key = "chatKey")
    public String chatKey;
    @FieldDesc(key = "fromKey")
    public String fromKey;
    @FieldDesc(key = "toKey")
    public String toKey;
    @FieldDesc(key = "atUsers")
    public ArrayList<String> atUsers;
    @FieldDesc(key = "data")
    public String data;

    public BaseChatPOJO(String jsonStr) {
        super(jsonStr);
    }

    public BaseChatPOJO(int type, String chatKey, String fromKey, String toKey
            , ArrayList<String> atUsers, String data) {
        super(null);
        this.type = type;
        this.chatKey = chatKey;
        this.fromKey = fromKey;
        this.toKey = toKey;
        this.atUsers = atUsers;
        this.data = data;
    }

}
