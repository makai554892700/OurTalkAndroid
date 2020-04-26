package www.mys.com.ourtalkandroid.adapter;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import www.mys.com.ourtalkandroid.R;
import www.mys.com.ourtalkandroid.base.BaseRecyclerAdapter;
import www.mys.com.ourtalkandroid.base.BaseRecyclerHolder;
import www.mys.com.ourtalkandroid.holder.TalkHolder;
import www.mys.com.ourtalkandroid.utils.db.model.DBTalkInfo;

public class TalkAdapter extends BaseRecyclerAdapter<DBTalkInfo> {

    public TalkAdapter(Context context) {
        super(context);
    }

    @Override
    public int getItemViewType(int position) {
        if (data.size() > position) {
            DBTalkInfo talkInfo = data.get(position);
            return talkInfo.talkType;
        }
        return -1;
    }

    @NonNull
    @Override
    public BaseRecyclerHolder<DBTalkInfo> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case DBTalkInfo.TalkType.FROM:
            case DBTalkInfo.TalkType.FROM_TIME:
            case DBTalkInfo.TalkType.FROM_RETURN:
                rootView = layoutInflater.inflate(R.layout.talk_from, parent, false);
                break;
            case DBTalkInfo.TalkType.TO:
            case DBTalkInfo.TalkType.TO_TIME:
            case DBTalkInfo.TalkType.TO_RETURN:
                rootView = layoutInflater.inflate(R.layout.talk_to, parent, false);
                break;
        }
        return new TalkHolder(context, rootView);
    }

}
