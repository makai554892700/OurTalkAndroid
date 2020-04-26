package www.mys.com.ourtalkandroid.utils.db.model;

import com.mayousheng.www.basepojo.BasePoJo;
import com.mayousheng.www.basepojo.FieldDesc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DBTalkInfo extends BasePoJo implements Serializable {

    public static final class Type {
        public static final int HANDSHAKE = 1;
        public static final int TALK_USER = 2;
        public static final int TALK_GROUP = 3;
        public static final int SEND_BACK = 4;
        public static final int RECEIVE_BACK = 5;
    }

    public static final class TalkType {
        public static final int FROM = 1;
        public static final int TO = 2;
        public static final int FROM_RETURN = 3;
        public static final int TO_RETURN = 4;
        public static final int FROM_TIME = 5;
        public static final int TO_TIME = 6;
    }

    public static final String ID = "_id";
    public static final String TYPE = "type";
    public static final String TALK_TYPE = "talk_type";
    public static final String CHAT_KEY = "chat_key";
    public static final String FROM_KEY = "from_key";
    public static final String TO_KEY = "to_key";
    public static final String AT_USERS = "at_users";
    public static final String DATA = "data";
    public static final String MSG_TIME = "msg_time";


    @FieldDesc(key = "id")
    public int id;
    @FieldDesc(key = "type")
    public int type;
    @FieldDesc(key = "talkType")
    public int talkType;
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
    @FieldDesc(key = "msgTime")
    public Long msgTime;

    public DBTalkInfo(String jsonStr) {
        super(jsonStr);
    }

    public DBTalkInfo(int type, int talkType, String chatKey, String fromKey
            , String toKey, ArrayList<String> atUsers, String data, Long msgTime) {
        super(null);
        this.type = type;
        this.talkType = talkType;
        this.chatKey = chatKey;
        this.fromKey = fromKey;
        this.toKey = toKey;
        this.atUsers = atUsers;
        this.data = data;
        this.msgTime = msgTime;
    }

    public DBTalkInfo(int id, int type, int talkType, String chatKey, String fromKey
            , String toKey, ArrayList<String> atUsers, String data, Long msgTime) {
        super(null);
        this.id = id;
        this.type = type;
        this.talkType = talkType;
        this.chatKey = chatKey;
        this.fromKey = fromKey;
        this.toKey = toKey;
        this.atUsers = atUsers;
        this.data = data;
        this.msgTime = msgTime;
    }
}
