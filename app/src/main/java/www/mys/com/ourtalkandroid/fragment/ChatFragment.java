package www.mys.com.ourtalkandroid.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mayousheng.www.initview.ViewDesc;

import java.util.ArrayList;

import www.mys.com.ourtalkandroid.R;
import www.mys.com.ourtalkandroid.adapter.ChatAdapter;
import www.mys.com.ourtalkandroid.base.BaseFragment;
import www.mys.com.ourtalkandroid.base.BaseRecyclerAdapter;
import www.mys.com.ourtalkandroid.base.StaticParam;
import www.mys.com.ourtalkandroid.utils.LogUtils;
import www.mys.com.ourtalkandroid.utils.db.DBChatListUtils;
import www.mys.com.ourtalkandroid.utils.db.model.DBChatList;

public class ChatFragment extends BaseFragment {

    @ViewDesc(viewId = R.id.recycler_view)
    public RecyclerView recyclerView;
    protected BaseRecyclerAdapter<DBChatList> recyclerAdapter;
    protected LinearLayoutManager linearLayoutManager;
    public DBChatListUtils dbChatListUtils;
    private int unReadCount;

    public int getUnReadCount() {
        return unReadCount;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbChatListUtils = new DBChatListUtils(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container
            , @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.addItemDecoration(StaticParam.DEFAULT_ITEM_DECORATION);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        onResult(dbChatListUtils.getDBChatLists());
    }

    public void onResult(ArrayList<DBChatList> responses) {
        if (recyclerAdapter == null) {
            return;
        }
        unReadCount = 0;
        for (DBChatList dbChatList : responses) {
            unReadCount += dbChatList.unReadCount;
        }
        recyclerAdapter.setData(responses);
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                recyclerAdapter.notifyDataSetChanged();
            });
        }
    }

    @Override
    protected int getLayoutId() {
        linearLayoutManager = new LinearLayoutManager(getContext()
                , LinearLayoutManager.VERTICAL, false);
        return R.layout.fragment_common_item;
    }

    @Override
    protected void initView(View view) {
        recyclerAdapter = new ChatAdapter(getContext());
    }

}
