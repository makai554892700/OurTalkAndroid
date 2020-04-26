package www.mys.com.ourtalkandroid.adapter;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import www.mys.com.ourtalkandroid.R;
import www.mys.com.ourtalkandroid.base.BaseRecyclerAdapter;
import www.mys.com.ourtalkandroid.base.BaseRecyclerHolder;
import www.mys.com.ourtalkandroid.holder.ChatHolder;
import www.mys.com.ourtalkandroid.utils.db.model.DBChatList;

public class ChatAdapter extends BaseRecyclerAdapter<DBChatList> {

    public ChatAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public BaseRecyclerHolder<DBChatList> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        rootView = layoutInflater.inflate(R.layout.item_chat_list, parent, false);
        return new ChatHolder(context, rootView);
    }

}
