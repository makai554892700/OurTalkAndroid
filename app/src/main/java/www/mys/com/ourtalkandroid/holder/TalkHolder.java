package www.mys.com.ourtalkandroid.holder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mayousheng.www.initview.ViewDesc;

import www.mys.com.ourtalkandroid.R;
import www.mys.com.ourtalkandroid.base.BaseRecyclerHolder;
import www.mys.com.ourtalkandroid.utils.db.model.DBTalkInfo;

public class TalkHolder extends BaseRecyclerHolder<DBTalkInfo> {

    @ViewDesc(viewId = R.id.head_img)
    public ImageView headImg;
    @ViewDesc(viewId = R.id.talk_text)
    public TextView talkText;

    public TalkHolder(final Context context, View view) {
        super(context, view);
    }

    @Override
    public void inViewBind(final DBTalkInfo dbTalkInfo) {
        talkText.setText(dbTalkInfo.data);
    }

}
