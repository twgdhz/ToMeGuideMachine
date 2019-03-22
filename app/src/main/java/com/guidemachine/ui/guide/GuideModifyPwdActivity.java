package com.guidemachine.ui.guide;

import com.guidemachine.R;
import com.guidemachine.base.ui.BaseActivity;

import butterknife.OnClick;

/**
* @author ChenLinWang
* @email 422828518@qq.com
* @create 2018/12/25 0025 15:44
* description: 导游修改密码
*/
public class GuideModifyPwdActivity extends BaseActivity {

    @Override
    protected int setRootViewId() {
        return R.layout.activity_guide_modify_pwd;
    }

    @Override
    protected boolean setIsFull() {
        return false;
    }

    @Override
    protected void InitialView() {

    }

    @OnClick(R.id.rl_back)
    public void onClick() {
        finish();
    }
}
