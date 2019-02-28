package com.guidemachine.ui.activity.chat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.guidemachine.R;
import com.guidemachine.base.ui.BaseActivity;
import com.guidemachine.util.StatusBarUtils;
import com.guidemachine.util.share.SPHelper;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.hyphenate.exceptions.HyphenateException;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
* @author ChenLinWang
* @email 422828518@qq.com
* @create 2018/12/18 0018 11:01
* description: 聊天页面登录
*/
public class ChatLoginActivity extends BaseActivity {

    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.btn_register)
    Button btnRegister;

    @Override
    protected int setRootViewId() {
        return R.layout.activity_chat_login;
    }

    @Override
    protected boolean setIsFull() {
        return false;
    }

    @Override
    protected void InitialView() {
        StatusBarUtils.setWindowStatusBarColor(ChatLoginActivity.this, R.color.text_color4);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });
    }

    private void register() {//环信注册
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().createAccount(etName.getText().toString().trim(), etPwd.getText().toString().trim());
                    Log.d("TalkFragment", "注册成功");
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    Log.e("TalkFragment", "注册失败" + e.getMessage());

                }
            }
        }).start();
    }

    //登录的方法
    private void login() {
        EMClient.getInstance().login(etName.getText().toString().trim(), etPwd.getText().toString().trim(), new EMCallBack() {
            @Override
            public void onSuccess() {

                startActivity(new Intent(ChatLoginActivity.this, ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, etName.getText().toString().trim()));
            }

            @Override
            public void onError(int i, String s) {
                Log.d("TalkFragment", i + " " + s.toString());

            }

            @Override
            public void onProgress(int i, String s) {

            }
        });

    }
}
