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
import www.mys.com.ourtalkandroid.adapter.ItemAdapter;
import www.mys.com.ourtalkandroid.base.BaseFragment;
import www.mys.com.ourtalkandroid.base.BaseRecyclerAdapter;
import www.mys.com.ourtalkandroid.base.StaticParam;
import www.mys.com.ourtalkandroid.pojo.ItemList;

public class FindFragment extends BaseFragment {

    @ViewDesc(viewId = R.id.recycler_view)
    public RecyclerView recyclerView;
    protected BaseRecyclerAdapter<ItemList> recyclerAdapter;
    protected LinearLayoutManager linearLayoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container
            , @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        recyclerView.setLayoutManager(linearLayoutManager);
        initData(0);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.addItemDecoration(StaticParam.DEFAULT_ITEM_DECORATION);
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

    public void initData(int page) {
        if (page == 0) {
            ArrayList<ItemList> contactLists = new ArrayList<ItemList>() {{
                add(new ItemList(ItemList.Type.MOMENTS, getString(R.string.item_moments), true, R.drawable.ic_moments));
                add(new ItemList(ItemList.Type.SCAN, getString(R.string.item_scan), false, R.drawable.ic_search));
                add(new ItemList(ItemList.Type.SHAKE, getString(R.string.item_shake), true, R.drawable.ic_shark));
//                add(new ItemList(ItemList.Type.LOOK, getString(R.string.item_look), false, R.drawable.ic_add));
                add(new ItemList(ItemList.Type.SEARCH, getString(R.string.item_search), true, R.drawable.ic_search_base));
//                add(new ItemList(ItemList.Type.AROUND, getString(R.string.item_around), true, R.drawable.ic_add));
                add(new ItemList(ItemList.Type.BUY, getString(R.string.item_buy), false, R.drawable.ic_shop));
                add(new ItemList(ItemList.Type.GAME, getString(R.string.item_game), true, R.drawable.ic_game));
                add(new ItemList(ItemList.Type.LITE_APP, getString(R.string.item_lite_app), false, R.drawable.ic_lite_app));
            }};
            onResult(contactLists, page);
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
        recyclerAdapter = new ItemAdapter(getContext());
    }
}
