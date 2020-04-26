package www.mys.com.ourtalkandroid.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mayousheng.www.initview.ViewDesc;

import java.util.ArrayList;

import www.mys.com.ourtalkandroid.R;
import www.mys.com.ourtalkandroid.adapter.ItemAdapter;
import www.mys.com.ourtalkandroid.base.BaseFragment;
import www.mys.com.ourtalkandroid.base.BaseRecyclerAdapter;
import www.mys.com.ourtalkandroid.base.StaticParam;
import www.mys.com.ourtalkandroid.pojo.ItemList;
import www.mys.com.ourtalkandroid.pojo.User;
import www.mys.com.ourtalkandroid.utils.StringUtils;
import www.mys.com.ourtalkandroid.utils.data.ConfigUtils;

public class MineFragment extends BaseFragment {

    @ViewDesc(viewId = R.id.recycler_view)
    public RecyclerView recyclerView;
    @ViewDesc(viewId = R.id.head_img)
    public ImageView headImg;
    @ViewDesc(viewId = R.id.item_title)
    public TextView itemTitle;
    @ViewDesc(viewId = R.id.item_desc)
    public TextView itemDesc;
    private User user;
    protected BaseRecyclerAdapter<ItemList> recyclerAdapter;
    protected LinearLayoutManager linearLayoutManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = ConfigUtils.getUserInfo(getContext(), success -> {
            if (success) {
                user = ConfigUtils.getUserInfo(getContext(), null);
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container
            , @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        recyclerView.setLayoutManager(linearLayoutManager);
        initData(0);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.addItemDecoration(StaticParam.DEFAULT_ITEM_DECORATION);
        itemTitle.setText(user.nickName);
        itemDesc.setText(
                getString(R.string.nick_name, user.nickName)
                        + "\n"
                        + getString(R.string.user_name, user.userName)
        );
        return view;
    }

    protected void onResult(ArrayList<ItemList> responses, int page) {
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

    private void initData(int page) {
        if (page == 0) {
            ArrayList<ItemList> contactLists = new ArrayList<ItemList>() {{
                add(new ItemList(ItemList.Type.PAY, getString(R.string.item_pay), true, R.drawable.ic_pay));
                add(new ItemList(ItemList.Type.COLLECT, getString(R.string.item_collect), false, R.drawable.ic_collect));
                add(new ItemList(ItemList.Type.PICTURE, getString(R.string.item_picture), false, R.drawable.ic_picture));
                add(new ItemList(ItemList.Type.CARD, getString(R.string.item_card), false, R.drawable.ic_card));
                add(new ItemList(ItemList.Type.FACE, getString(R.string.item_face), true, R.drawable.ic_face_base));
                add(new ItemList(ItemList.Type.SETTINGS, getString(R.string.item_settings), false, R.drawable.ic_setting));
            }};
            onResult(contactLists, page);
        }
    }

    @Override
    protected int getLayoutId() {
        linearLayoutManager = new LinearLayoutManager(getContext()
                , LinearLayoutManager.VERTICAL, false);
        return R.layout.fragment_mine;
    }

    @Override
    protected void initView(View view) {
        recyclerAdapter = new ItemAdapter(getContext());
    }
}
