package www.mys.com.ourtalkandroid.adapter;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import www.mys.com.ourtalkandroid.R;
import www.mys.com.ourtalkandroid.base.BaseRecyclerAdapter;
import www.mys.com.ourtalkandroid.base.BaseRecyclerHolder;
import www.mys.com.ourtalkandroid.holder.ContactHolder;
import www.mys.com.ourtalkandroid.pojo.FriendShip;

public class ContactAdapter extends BaseRecyclerAdapter<FriendShip> {

    public ContactAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public BaseRecyclerHolder<FriendShip> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        rootView = layoutInflater.inflate(R.layout.item_contact_list, parent, false);
        return new ContactHolder(context, rootView);
    }

}
