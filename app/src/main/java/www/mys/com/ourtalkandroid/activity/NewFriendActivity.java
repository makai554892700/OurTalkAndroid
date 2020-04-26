package www.mys.com.ourtalkandroid.activity;

import android.os.Bundle;
import android.view.WindowManager;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mayousheng.www.initview.ViewDesc;

import java.util.ArrayList;

import www.mys.com.ourtalkandroid.R;
import www.mys.com.ourtalkandroid.adapter.NewFriendAdapter;
import www.mys.com.ourtalkandroid.base.BaseAppbarLoadingActivity;
import www.mys.com.ourtalkandroid.base.StaticParam;
import www.mys.com.ourtalkandroid.pojo.AddFriend;
import www.mys.com.ourtalkandroid.utils.LogUtils;
import www.mys.com.ourtalkandroid.utils.data.ConfigUtils;

public class NewFriendActivity extends BaseAppbarLoadingActivity {

    @ViewDesc(viewId = R.id.recycler_view)
    public RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private NewFriendAdapter newFriendAdapter;
    private ArrayList<AddFriend> addFriends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initEvent();
        addFriends = ConfigUtils.getAddFriend(getApplicationContext()
                , success -> {
                    if (success) {
                        addFriends = ConfigUtils.getAddFriend(getApplicationContext(), null);
                        onResult(addFriends, 0);
                    }
                });
        onResult(addFriends, 0);
    }

    private void initView() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(newFriendAdapter);
        recyclerView.addItemDecoration(StaticParam.ITEM_DECORATION);
    }

    private void initEvent() {
    }


    protected void onResult(ArrayList<AddFriend> responses, int page) {
        LogUtils.log("onResult responses=" + responses);
        if (newFriendAdapter == null) {
            return;
        }
        if (page == 0) {
            newFriendAdapter.setData(responses);
        } else {
            newFriendAdapter.addData(responses);
        }
        runOnUiThread(() -> {
            newFriendAdapter.notifyDataSetChanged();
        });
    }

    public int getLeftIcon() {
        return R.drawable.ic_back;
    }

    @Override
    public String getAppBarTitle() {
        return getString(R.string.item_new_friend);
    }

    @Override
    public int getLayout() {
        linearLayoutManager = new LinearLayoutManager(getApplicationContext()
                , LinearLayoutManager.VERTICAL, false);
        newFriendAdapter = new NewFriendAdapter(getApplicationContext());
        return R.layout.activity_common;
    }

}
