package www.mys.com.ourtalkandroid.holder;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.mayousheng.www.initview.ViewDesc;

import www.mys.com.ourtalkandroid.R;
import www.mys.com.ourtalkandroid.activity.ProfileActivity;
import www.mys.com.ourtalkandroid.base.BaseRecyclerHolder;
import www.mys.com.ourtalkandroid.base.StaticParam;
import www.mys.com.ourtalkandroid.pojo.FriendShip;
import www.mys.com.ourtalkandroid.utils.LogUtils;
import www.mys.com.ourtalkandroid.utils.StringUtils;
import www.mys.com.ourtalkandroid.utils.data.ConfigUtils;

public class ContactHolder extends BaseRecyclerHolder<FriendShip> {

    @ViewDesc(viewId = R.id.card_view)
    public CardView rootView;
    @ViewDesc(viewId = R.id.title)
    public TextView title;
    @ViewDesc(viewId = R.id.item_img)
    public ImageView headImg;
    @ViewDesc(viewId = R.id.item_name)
    public TextView itemName;

    public ContactHolder(final Context context, View view) {
        super(context, view);
    }

    @Override
    public void inViewBind(final FriendShip friendShip) {
        if (StringUtils.isEmpty(friendShip.itemKey)) {
            title.setVisibility(View.GONE);
        } else {
            title.setVisibility(View.VISIBLE);
            title.setText(friendShip.itemKey);
        }
        headImg.setImageResource(R.drawable.ic_head_2);
        itemName.setText(friendShip.descName);
        rootView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProfileActivity.class);
            intent.putExtra(StaticParam.DATA, friendShip.toString());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                context.startActivity(intent);
            } catch (Exception e) {
                LogUtils.log("start activity error.e=" + e);
            }
        });
    }

}
