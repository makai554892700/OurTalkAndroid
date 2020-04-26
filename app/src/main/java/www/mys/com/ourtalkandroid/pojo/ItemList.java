package www.mys.com.ourtalkandroid.pojo;

import com.mayousheng.www.basepojo.BasePoJo;
import com.mayousheng.www.basepojo.FieldDesc;

public class ItemList extends BasePoJo {

    public static final class Type {
        public static final int MOMENTS = 1;
        public static final int SCAN = 2;
        public static final int SHAKE = 3;
        public static final int LOOK = 4;
        public static final int SEARCH = 5;
        public static final int AROUND = 6;
        public static final int BUY = 7;
        public static final int GAME = 8;
        public static final int LITE_APP = 9;
        public static final int PAY = 10;
        public static final int COLLECT = 11;
        public static final int PICTURE = 12;
        public static final int CARD = 13;
        public static final int FACE = 14;
        public static final int SETTINGS = 15;
    }

    @FieldDesc(key = "itemType")
    public int itemType;
    @FieldDesc(key = "itemName")
    public String itemName;
    @FieldDesc(key = "haveBottom")
    public boolean haveBottom;
    @FieldDesc(key = "itemImg")
    public int itemImg;

    public ItemList(String jsonStr) {
        super(jsonStr);
    }

    public ItemList(int itemType, String itemName, boolean haveBottom, int itemImg) {
        super(null);
        this.itemType = itemType;
        this.itemName = itemName;
        this.haveBottom = haveBottom;
        this.itemImg = itemImg;
    }
}
