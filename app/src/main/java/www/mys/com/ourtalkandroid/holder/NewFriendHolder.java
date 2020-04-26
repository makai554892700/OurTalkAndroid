package www.mys.com.ourtalkandroid.holder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.mayousheng.www.initview.ViewDesc;

import www.mys.com.ourtalkandroid.R;
import www.mys.com.ourtalkandroid.base.BaseRecyclerHolder;
import www.mys.com.ourtalkandroid.pojo.AddFriend;
import www.mys.com.ourtalkandroid.utils.LogUtils;
import www.mys.com.ourtalkandroid.utils.data.ConfigUtils;
import www.mys.com.ourtalkandroid.utils.data.DataUtils;

public class NewFriendHolder extends BaseRecyclerHolder<AddFriend> {

    @ViewDesc(viewId = R.id.card_view)
    public CardView rootView;
    @ViewDesc(viewId = R.id.title)
    public TextView title;
    @ViewDesc(viewId = R.id.item_img)
    public ImageView headImg;
    @ViewDesc(viewId = R.id.item_name)
    public TextView itemName;
    @ViewDesc(viewId = R.id.item_accept)
    public TextView accept;

    public NewFriendHolder(final Context context, View view) {
        super(context, view);
    }

    @Override
    public void inViewBind(final AddFriend addFriend) {
        itemName.setText(addFriend.nickName);
        LogUtils.log("addFriend=" + addFriend);
        accept.setVisibility(addFriend.accept ? View.GONE : View.VISIBLE);
        accept.setOnClickListener(v -> {
            accept.setVisibility(View.GONE);
            DataUtils.accessFriend(context, addFriend.id, new DataUtils.StringBack() {
                @Override
                public void onSuccess(String response) {
                    LogUtils.log("accessFriend success. response=" + response);
                    ConfigUtils.acceptAddFriend(context, addFriend.id);
                }

                @Override
                public void onFailed(Integer status, String message) {
                    LogUtils.log("accessFriend failed. status=" + status + ";message=" + message);
                }
            });
            LogUtils.log("接受");
        });
    }

}
