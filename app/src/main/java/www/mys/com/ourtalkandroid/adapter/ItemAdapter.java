package www.mys.com.ourtalkandroid.adapter;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import www.mys.com.ourtalkandroid.R;
import www.mys.com.ourtalkandroid.base.BaseRecyclerAdapter;
import www.mys.com.ourtalkandroid.base.BaseRecyclerHolder;
import www.mys.com.ourtalkandroid.holder.ItemHolder;
import www.mys.com.ourtalkandroid.pojo.ItemList;

public class ItemAdapter extends BaseRecyclerAdapter<ItemList> {

    public ItemAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public BaseRecyclerHolder<ItemList> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        rootView = layoutInflater.inflate(R.layout.item_normal_list, parent, false);
        return new ItemHolder(context, rootView);
    }

}
