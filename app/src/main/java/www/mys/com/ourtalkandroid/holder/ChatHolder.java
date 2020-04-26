package www.mys.com.ourtalkandroid.holder;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.mayousheng.www.initview.ViewDesc;

import www.mys.com.ourtalkandroid.R;
import www.mys.com.ourtalkandroid.activity.TalkActivity;
import www.mys.com.ourtalkandroid.base.BaseRecyclerHolder;
import www.mys.com.ourtalkandroid.base.StaticParam;
import www.mys.com.ourtalkandroid.pojo.FriendShip;
import www.mys.com.ourtalkandroid.pojo.User;
import www.mys.com.ourtalkandroid.utils.NotificationUtils;
import www.mys.com.ourtalkandroid.utils.data.ConfigUtils;
import www.mys.com.ourtalkandroid.utils.db.DBChatListUtils;
import www.mys.com.ourtalkandroid.utils.db.model.DBChatList;
import www.mys.com.ourtalkandroid.utils.LogUtils;
import www.mys.com.ourtalkandroid.utils.TimeUtils;

public class ChatHolder extends BaseRecyclerHolder<DBChatList> {

    @ViewDesc(viewId = R.id.card_view)
    public CardView rootView;
    @ViewDesc(viewId = R.id.item_img)
    public ImageView leftImg;
    @ViewDesc(viewId = R.id.item_red_point)
    public ImageView redPoint;
    @ViewDesc(viewId = R.id.item_mark)
    public ImageView rightMark;
    @ViewDesc(viewId = R.id.title)
    public TextView title;
    @ViewDesc(viewId = R.id.time)
    public TextView time;
    @ViewDesc(viewId = R.id.desc)
    public TextView desc;
    private User user;
    private DBChatListUtils dbChatListUtils;

    public ChatHolder(final Context context, View view) {
        super(context, view);
        user = ConfigUtils.getUserInfo(context, null);
        dbChatListUtils = new DBChatListUtils(context);
    }

    @Override
    public void inViewBind(final DBChatList dbChatList) {
        title.setText(dbChatList.msgTitle);
        time.setText(TimeUtils.getTimeStr(dbChatList.msgTime));
        desc.setText(dbChatList.msgDesc);
        redPoint.setVisibility(dbChatList.unReadCount > 0
                ? View.VISIBLE : View.INVISIBLE);
        rootView.setOnClickListener(v -> {
            if (dbChatList.id > 0) {
                dbChatList.unReadCount = 0;
                dbChatListUtils.saveDBChatList(dbChatList);
            }
            Intent intent = new Intent(context, TalkActivity.class);
            FriendShip friendShip = new FriendShip(
                    user.userName, dbChatList.userName, dbChatList.msgTitle
                    , dbChatList.imgUrl, -1
            );
            intent.putExtra(StaticParam.DATA, friendShip.toString());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                context.startActivity(intent);
                NotificationUtils.clearNotification(NotificationUtils.Type.MESSAGE);
            } catch (Exception e) {
                LogUtils.log("start activity error.e=" + e);
            }
        });
    }

}
