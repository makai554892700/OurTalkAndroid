package www.mys.com.ourtalkandroid.adapter;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import www.mys.com.ourtalkandroid.R;
import www.mys.com.ourtalkandroid.base.BaseRecyclerAdapter;
import www.mys.com.ourtalkandroid.base.BaseRecyclerHolder;
import www.mys.com.ourtalkandroid.holder.NewFriendHolder;
import www.mys.com.ourtalkandroid.pojo.AddFriend;

public class NewFriendAdapter extends BaseRecyclerAdapter<AddFriend> {

    public NewFriendAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public BaseRecyclerHolder<AddFriend> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        rootView = layoutInflater.inflate(R.layout.item_contact_list, parent, false);
        return new NewFriendHolder(context, rootView);
    }

}
