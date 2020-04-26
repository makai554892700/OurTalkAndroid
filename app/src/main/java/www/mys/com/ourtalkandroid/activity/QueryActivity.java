package www.mys.com.ourtalkandroid.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mayousheng.www.initview.ViewDesc;

import java.util.ArrayList;

import www.mys.com.ourtalkandroid.R;
import www.mys.com.ourtalkandroid.adapter.ContactAdapter;
import www.mys.com.ourtalkandroid.base.BaseLoadingActivity;
import www.mys.com.ourtalkandroid.base.StaticParam;
import www.mys.com.ourtalkandroid.pojo.AddFriend;
import www.mys.com.ourtalkandroid.pojo.FriendShip;
import www.mys.com.ourtalkandroid.pojo.User;
import www.mys.com.ourtalkandroid.utils.LogUtils;
import www.mys.com.ourtalkandroid.utils.data.ConfigUtils;
import www.mys.com.ourtalkandroid.utils.data.DataUtils;

public class QueryActivity extends BaseLoadingActivity {

    @ViewDesc(viewId = R.id.search)
    public ImageView search;
    @ViewDesc(viewId = R.id.clear)
    public ImageView clear;
    @ViewDesc(viewId = R.id.query)
    public EditText queryText;
    @ViewDesc(viewId = R.id.cancel)
    public TextView cancel;
    @ViewDesc(viewId = R.id.recycler_view)
    public RecyclerView recyclerView;
    protected LinearLayoutManager linearLayoutManager;
    private ContactAdapter contactAdapter;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = ConfigUtils.getUserInfo(getApplicationContext()
                , success -> {
                    if (success) {
                        user = ConfigUtils.getUserInfo(getApplicationContext(), null);
                    }
                });
        initEvent();
        initView();
    }

    private void initView() {
        queryText.setFocusable(true);
        queryText.setFocusableInTouchMode(true);
        queryText.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(contactAdapter);
        recyclerView.addItemDecoration(StaticParam.ITEM_DECORATION);
    }

    private void initEvent() {
        cancel.setOnClickListener(v -> {
            finish();
        });
        search.setOnClickListener(v -> {
            queryFriend();
        });
        queryText.setOnEditorActionListener((v, actionId, event) -> {
            LogUtils.log("onEditorAction event=" + event);
            if (event != null && event.getAction() == KeyEvent.ACTION_DOWN) {
                queryFriend();
            }
            return false;
        });
        queryText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    clear.setVisibility(View.GONE);
                } else {
                    clear.setVisibility(View.VISIBLE);
                }
            }
        });
        clear.setOnClickListener(v -> {
            queryText.setText(StaticParam.EMPTY);
        });
    }

    private void queryFriend() {
        if (queryText.getText() == null) {
            LogUtils.log("queryText is null.");
            return;
        }
        LogUtils.log("queryFriend=" + queryText.getText());
        DataUtils.queryUser(getApplicationContext(), queryText.getText().toString()
                , new DataUtils.BodyBack<User>() {
                    @Override
                    public void onSuccess(User response) {
                        LogUtils.log("onSuccess response=" + response);
                        if (response != null) {
                            onResult(response, 0);
                        } else {
                            runOnUiThread(() -> {
                                Toast.makeText(getApplicationContext(), getString(R.string.no_such_user)
                                        , Toast.LENGTH_LONG).show();
                            });
                        }
                    }

                    @Override
                    public void onFailed(Integer status, final String message) {
                        LogUtils.log("onFailed status=" + status + ";message=" + message);
                        runOnUiThread(() -> {
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                        });
                    }
                });
    }


    protected void onResult(final User response, int page) {
        if (contactAdapter == null) {
            return;
        }
        if (page == 0) {
            contactAdapter.setData(new ArrayList<FriendShip>() {{
                add(new FriendShip(user.userName, response.userName, response.nickName
                        , response.imgUrl, AddFriend.Type.QUERY));
            }});
        } else {
            contactAdapter.addData(new ArrayList<FriendShip>() {{
                add(new FriendShip(user.userName, response.userName, response.nickName
                        , response.imgUrl, AddFriend.Type.QUERY));
            }});
        }
        runOnUiThread(() -> {
            contactAdapter.notifyDataSetChanged();
        });
    }

    @Override
    public int getLayout() {
        linearLayoutManager = new LinearLayoutManager(getApplicationContext()
                , LinearLayoutManager.VERTICAL, false);
        contactAdapter = new ContactAdapter(getApplicationContext());
        return R.layout.activity_query;
    }
}
