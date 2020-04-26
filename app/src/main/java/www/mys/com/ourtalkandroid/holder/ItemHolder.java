package www.mys.com.ourtalkandroid.holder;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.mayousheng.www.initview.ViewDesc;

import www.mys.com.ourtalkandroid.R;
import www.mys.com.ourtalkandroid.activity.SettingActivity;
import www.mys.com.ourtalkandroid.base.BaseRecyclerHolder;
import www.mys.com.ourtalkandroid.base.StaticParam;
import www.mys.com.ourtalkandroid.pojo.ItemList;
import www.mys.com.ourtalkandroid.utils.LogUtils;

public class ItemHolder extends BaseRecyclerHolder<ItemList> {

    @ViewDesc(viewId = R.id.card_view)
    public CardView cardView;
    @ViewDesc(viewId = R.id.item_img)
    public ImageView headImg;
    @ViewDesc(viewId = R.id.item_name)
    public TextView itemName;
    @ViewDesc(viewId = R.id.bottom)
    public View bottom;

    public ItemHolder(final Context context, View view) {
        super(context, view);
    }

    @Override
    public void inViewBind(final ItemList itemKey) {
        bottom.setVisibility(itemKey.haveBottom ? View.VISIBLE : View.GONE);
        itemName.setText(itemKey.itemName);
        if (itemKey.itemImg > 0) {
            headImg.setImageResource(itemKey.itemImg);
        }
        cardView.setOnClickListener(v -> {
            Intent intent = null;
            switch (itemKey.itemType) {
                case ItemList.Type.MOMENTS:
                    break;
                case ItemList.Type.SCAN:
                    break;
                case ItemList.Type.SHAKE:
                    break;
                case ItemList.Type.LOOK:
                    break;
                case ItemList.Type.SEARCH:
                    break;
                case ItemList.Type.AROUND:
                    break;
                case ItemList.Type.BUY:
                    break;
                case ItemList.Type.GAME:
                    break;
                case ItemList.Type.LITE_APP:
                    break;
                case ItemList.Type.PAY:
                    break;
                case ItemList.Type.COLLECT:
                    break;
                case ItemList.Type.PICTURE:
                    break;
                case ItemList.Type.CARD:
                    break;
                case ItemList.Type.FACE:
                    break;
                case ItemList.Type.SETTINGS:
                    intent = new Intent(context, SettingActivity.class);
                    intent.putExtra(StaticParam.DATA, itemKey.itemName);
                    break;
            }
            if (intent != null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                try {
                    context.startActivity(intent);
                } catch (Exception e) {
                    LogUtils.log("start activity error.e=" + e);
                }
            }
        });
    }

}
