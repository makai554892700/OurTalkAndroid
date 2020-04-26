package www.mys.com.ourtalkandroid.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mayousheng.www.initview.ViewDesc;

import java.util.ArrayList;

import www.mys.com.ourtalkandroid.R;
import www.mys.com.ourtalkandroid.activity.NewFriendActivity;
import www.mys.com.ourtalkandroid.adapter.ContactAdapter;
import www.mys.com.ourtalkandroid.base.BaseFragment;
import www.mys.com.ourtalkandroid.base.BaseRecyclerAdapter;
import www.mys.com.ourtalkandroid.base.StaticParam;
import www.mys.com.ourtalkandroid.pojo.AddFriend;
import www.mys.com.ourtalkandroid.pojo.FriendShip;
import www.mys.com.ourtalkandroid.utils.LogUtils;
import www.mys.com.ourtalkandroid.utils.NotificationUtils;
import www.mys.com.ourtalkandroid.utils.ResolverUtils;
import www.mys.com.ourtalkandroid.utils.data.ConfigUtils;

public class ContactFragment extends BaseFragment {

    @ViewDesc(viewId = R.id.recycler_view)
    public RecyclerView recyclerView;
    @ViewDesc(viewId = R.id.new_fiend_parent)
    public CardView newFriend;
    @ViewDesc(viewId = R.id.group_parent)
    public CardView group;
    @ViewDesc(viewId = R.id.flag_parent)
    public CardView flag;
    @ViewDesc(viewId = R.id.public_account_parent)
    public CardView publicAccount;
    public ImageView newFriendRedPoint;
    protected BaseRecyclerAdapter<FriendShip> recyclerAdapter;
    protected LinearLayoutManager linearLayoutManager;
    private ArrayList<FriendShip> friendShips;
    private int unReadCount;

    public int getUnReadCount() {
        return unReadCount;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container
            , @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        recyclerView.setLayoutManager(linearLayoutManager);
        newFriendRedPoint = newFriend.findViewById(R.id.item_red_point);
        friendShips = ConfigUtils.getFriendShip(getContext(), null);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.addItemDecoration(StaticParam.DEFAULT_ITEM_DECORATION);
        initView();
        initEvent();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshContactList();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            refreshContactList();
        }
    }

    public void refreshNewFriendCount(Back back) {
        ConfigUtils.getAddFriend(getContext()
                , success -> {
                    if (success) {
                        ArrayList<AddFriend> addFriends = ConfigUtils.getAddFriend(getContext(), null);
                        unReadCount = 0;
                        for (AddFriend addFriend : addFriends) {
                            if (!addFriend.read) {
                                unReadCount += 1;
                            }
                        }
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(() -> {
                                newFriendRedPoint.setVisibility(unReadCount > 0
                                        ? View.VISIBLE : View.INVISIBLE);
                                recyclerAdapter.notifyDataSetChanged();
                            });
                        }
                    }
                    if (back != null) {
                        back.onBack(success);
                    }
                });
    }

    private void refreshContactList() {
        ConfigUtils.getFriendShip(getContext(), success -> {
            if (success) {
                friendShips = ConfigUtils.getFriendShip(getContext(), null);
                onResult(friendShips, 0);
            }
        });
    }

    private void initEvent() {
        newFriend.setOnClickListener(v -> {
            NotificationUtils.clearNotification(NotificationUtils.Type.FRIEND_REQUEST);
            ArrayList<AddFriend> addFriends = ConfigUtils.getAddFriend(getContext(), null);
            for (AddFriend addFriend : addFriends) {
                addFriend.read = true;
            }
            ResolverUtils.getInstance().saveSetting(StaticParam.ADD_FRIEND, addFriends.toString());
            Intent intent = new Intent(getContext(), NewFriendActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                startActivity(intent);
            } catch (Exception e) {
                LogUtils.log("start activity error.e=" + e);
            }
        });
    }

    private void initView() {
        initCardView(newFriend, R.drawable.ic_add_friend, R.string.item_new_friend);
        initCardView(group, R.drawable.ic_contacts_base, R.string.item_group);
        initCardView(flag, R.drawable.ic_flag, R.string.item_flag);
        initCardView(publicAccount, R.drawable.ic_public_account, R.string.item_public_account);
    }

    private void initCardView(CardView cardView, int imgResource, int itemStr) {
        ImageView headImg = cardView.findViewById(R.id.item_img);
        headImg.setImageResource(imgResource);
        TextView itemTitle = cardView.findViewById(R.id.item_name);
        itemTitle.setText(itemStr);
    }

    protected void onResult(ArrayList<FriendShip> responses, int page) {
        if (recyclerAdapter == null) {
            return;
        }
        if (page == 0) {
            recyclerAdapter.setData(responses);
        } else {
            recyclerAdapter.addData(responses);
        }
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
        return R.layout.fragment_contact;
    }

    @Override
    protected void initView(View view) {
        recyclerAdapter = new ContactAdapter(getContext());
    }

    public static interface Back {
        public void onBack(boolean success);
    }

}
