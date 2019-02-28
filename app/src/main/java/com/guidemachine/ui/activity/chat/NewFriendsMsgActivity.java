package com.guidemachine.ui.activity.chat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.guidemachine.R;
import com.guidemachine.base.ui.BaseActivity;
import com.guidemachine.ui.activity.chat.adapter.NewFriendsMsgAdapter;
import com.guidemachine.ui.activity.chat.domain.InviteMessage;
import com.guidemachine.ui.activity.chat.widget.InviteMessgeDao;

import java.util.Collections;
import java.util.List;

public class NewFriendsMsgActivity extends BaseActivity {

    @Override
    protected int setRootViewId() {
        return R.layout.activity_new_friends_msg;
    }

    @Override
    protected boolean setIsFull() {
        return false;
    }

    @Override
    protected void InitialView() {
        ListView listView = (ListView) findViewById(R.id.list);
        InviteMessgeDao dao = new InviteMessgeDao(this);
        List<InviteMessage> msgs = dao.getMessagesList();
        Collections.reverse(msgs);

        NewFriendsMsgAdapter adapter = new NewFriendsMsgAdapter(this, 1, msgs);
        listView.setAdapter(adapter);
        dao.saveUnreadMessageCount(0);
    }

    public void back(View view) {
        finish();
    }
}
