package www.mys.com.ourtalkandroid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.mayousheng.www.initview.ViewDesc;

import java.util.ArrayList;

import www.mys.com.ourtalkandroid.ChatCallBack;
import www.mys.com.ourtalkandroid.R;
import www.mys.com.ourtalkandroid.adapter.TalkAdapter;
import www.mys.com.ourtalkandroid.base.BaseAppbarLoadingActivity;
import www.mys.com.ourtalkandroid.base.StaticParam;
import www.mys.com.ourtalkandroid.pojo.FriendShip;
import www.mys.com.ourtalkandroid.pojo.User;
import www.mys.com.ourtalkandroid.utils.LogUtils;
import www.mys.com.ourtalkandroid.utils.chat.BaseChatPOJO;
import www.mys.com.ourtalkandroid.utils.chat.ChatServiceUtils;
import www.mys.com.ourtalkandroid.utils.data.ConfigUtils;
import www.mys.com.ourtalkandroid.utils.db.DBChatListUtils;
import www.mys.com.ourtalkandroid.utils.db.DBTalkInfoUtils;
import www.mys.com.ourtalkandroid.utils.db.model.DBChatList;
import www.mys.com.ourtalkandroid.utils.db.model.DBTalkInfo;

public class TalkActivity extends BaseAppbarLoadingActivity {

    @ViewDesc(viewId = R.id.swipe_refresh_layout)
    public SwipeRefreshLayout swipeRefreshLayout;
    @ViewDesc(viewId = R.id.recycler_view)
    public RecyclerView recyclerView;
    @ViewDesc(viewId = R.id.talk_add)
    public ImageView talkAdd;
    @ViewDesc(viewId = R.id.talk_send)
    public Button talkSend;
    @ViewDesc(viewId = R.id.talk_text)
    public EditText talkText;
    protected TalkAdapter talkAdapter;
    protected LinearLayoutManager linearLayoutManager;
    private FriendShip friendShip;
    private User user;
    private DBTalkInfoUtils dbTalkInfoUtils;
    private DBChatListUtils dbChatListUtils;
    private int currentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        String data = intent.getStringExtra(StaticParam.DATA);
        friendShip = new FriendShip(data);
        LogUtils.log("friendShip=" + friendShip);
        super.onCreate(savedInstanceState);
        dbTalkInfoUtils = new DBTalkInfoUtils(getApplicationContext());
        dbChatListUtils = new DBChatListUtils(getApplicationContext());
        user = ConfigUtils.getUserInfo(getApplicationContext(), success -> {
            if (success) {
                user = ConfigUtils.getUserInfo(getApplicationContext(), null);
            }
        });
        ChatServiceUtils.getInstance().bindService(this, new ChatCallBack.Stub() {
            @Override
            public void onBack(int type, String data) throws RemoteException {
                switch (type) {
                    case ChatServiceUtils.RECEIVE_MESSAGE:
                        initData(0);
                        break;
                }
                LogUtils.log("onBack type=" + type + ";data=" + data);
            }
        });
        initView();
        initEvent();
        DBChatList dbChatList = dbChatListUtils.getDBChatListByKey(
                ConfigUtils.getChatKey(friendShip.fromUser, friendShip.toUser));
        if (dbChatList != null && dbChatList.unReadCount > 0) {
            dbChatList.unReadCount = 0;
            dbChatListUtils.saveDBChatList(dbChatList);
        }
    }

    private void initView() {
        recyclerView.setLayoutManager(linearLayoutManager);
        initData(0);
        recyclerView.setAdapter(talkAdapter);
        recyclerView.addItemDecoration(StaticParam.ITEM_DECORATION);
        recyclerView.scrollToPosition(0);
        recyclerView.smoothScrollToPosition(0);
    }

    private void initEvent() {
        talkText.addTextChangedListener(new TextWatcher() {

            private String lastStr;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                lastStr = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    talkSend.setVisibility(View.GONE);
                    talkAdd.setVisibility(View.VISIBLE);
                } else {
                    talkSend.setVisibility(View.VISIBLE);
                    talkAdd.setVisibility(View.GONE);
                }
                if (StaticParam.ENTER.equals(s.toString()
                        .replace(lastStr, StaticParam.EMPTY))) {
                    sendText(true);
                }
            }
        });
        talkSend.setOnClickListener(v -> {
            sendText(false);
        });
        swipeRefreshLayout.setOnRefreshListener(() -> {
            StaticParam.executor.execute(() -> {
                initData(++currentPage);
                runOnUiThread(() -> {
                    swipeRefreshLayout.setRefreshing(false);
                });
            });
        });
    }

    protected void onResult(ArrayList<DBTalkInfo> responses, final int page) {
        if (talkAdapter == null) {
            return;
        }
        if (page == 0) {
            talkAdapter.setData(responses);
        } else {
            talkAdapter.addData(responses);
        }
        runOnUiThread(() -> {
            talkAdapter.notifyDataSetChanged();
            if (page == 0) {
                recyclerView.scrollToPosition(0);
                recyclerView.smoothScrollToPosition(0);
            }
        });
    }

    public void initData(int page) {
        currentPage = page;
        ArrayList<DBTalkInfo> talkInfos = dbTalkInfoUtils.getDBTalkInfoByKey(
                ConfigUtils.getChatKey(friendShip.fromUser, friendShip.toUser)
                , page, StaticParam.PAGE_COUNT
        );
        onResult(talkInfos, page);
    }

    private void sendText(boolean isEnter) {
        if (talkText.getText() == null) {
            LogUtils.log("sendText is null.");
            return;
        }
        String dataStr = talkText.getText().toString();
        if (dataStr.length() > 1 && isEnter) {
            dataStr = dataStr.substring(0, dataStr.length() - 1);
        }
        LogUtils.log("send text=" + dataStr);
        DBTalkInfo talkInfo = new DBTalkInfo(DBTalkInfo.Type.TALK_USER
                , DBTalkInfo.TalkType.TO
                , ConfigUtils.getChatKey(friendShip.fromUser, friendShip.toUser)
                , user.userName, friendShip.toUser
                , null, dataStr, System.currentTimeMillis());
        //int type, String chatKey, String imgUrl, String msgTitle, String msgDesc, Long msgTime
        dbTalkInfoUtils.saveDBTalkInfo(talkInfo);
        DBChatList dbChatList = dbChatListUtils.getDBChatListByKey(
                ConfigUtils.getChatKey(friendShip.fromUser, friendShip.toUser));
        if (dbChatList == null) {
            dbChatList = new DBChatList(
                    DBTalkInfo.Type.TALK_USER
                    , 0
                    , ConfigUtils.getChatKey(friendShip.fromUser, friendShip.toUser)
                    , friendShip.headImg, friendShip.toUser
                    , friendShip.descName, dataStr
                    , System.currentTimeMillis()
            );
        } else {
            dbChatList.unReadCount = 0;
            dbChatList.msgDesc = dataStr;
            dbChatList.msgTime = System.currentTimeMillis();
        }
        dbChatListUtils.saveDBChatList(dbChatList);
        talkText.setText(StaticParam.EMPTY);
        initData(0);
        BaseChatPOJO baseChatPOJO = new BaseChatPOJO(BaseChatPOJO.Type.TALK_USER
                , ConfigUtils.getChatKey(friendShip.fromUser, friendShip.toUser)
                , user.userName, friendShip.toUser, null, dataStr);
        ChatServiceUtils.getInstance().send(
                ChatServiceUtils.SENT_MESSAGE, baseChatPOJO.toString()
        );
    }

    @Override
    public int getLeftIcon() {
        return R.drawable.ic_back;
    }

    @Override
    public String getAppBarTitle() {
        return friendShip.descName;
    }

    @Override
    public int getLayout() {
        linearLayoutManager = new LinearLayoutManager(getApplicationContext()
                , LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        talkAdapter = new TalkAdapter(getApplicationContext());
        return R.layout.activity_chat;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ChatServiceUtils.getInstance().unbindService(this);
    }
}
