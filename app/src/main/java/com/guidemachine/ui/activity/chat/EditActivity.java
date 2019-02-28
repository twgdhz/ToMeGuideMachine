package com.guidemachine.ui.activity.chat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.guidemachine.R;
import com.guidemachine.base.ui.BaseActivity;
import com.guidemachine.util.StatusBarUtils;

public class EditActivity extends BaseActivity {
    private EditText editText;

    @Override
    protected int setRootViewId() {
        return R.layout.activity_edit;
    }


    @Override
    protected boolean setIsFull() {
        return false;
    }

    @Override
    protected void InitialView() {
        StatusBarUtils.setWindowStatusBarColor(EditActivity.this, R.color.text_color4);
        editText = (EditText) findViewById(R.id.edittext);
        String title = getIntent().getStringExtra("title");
        String data = getIntent().getStringExtra("data");
        Boolean editable = getIntent().getBooleanExtra("editable", false);
        if(title != null)
            ((TextView)findViewById(R.id.tv_title)).setText(title);
        if(data != null)
            editText.setText(data);

        editText.setEnabled(editable);
        editText.setSelection(editText.length());

        findViewById(R.id.btn_save).setEnabled(editable);
    }

    public void save(View view){
        setResult(RESULT_OK, new Intent().putExtra("data", editText.getText().toString()));
        finish();
    }

    public void back(View view) {
        finish();
    }
}
