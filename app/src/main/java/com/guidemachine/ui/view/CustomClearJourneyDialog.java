package com.guidemachine.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.guidemachine.R;


/**
 * @author ChenLinWang
 * @email 422828518@qq.com
 * @create 2018/12/24 0024 17:15
 * description:一键清空
 */
public class CustomClearJourneyDialog extends Dialog {
    private String content;
    private Button btnEnsure, btnCancel;
    ClearJourney clearJourney;

    public CustomClearJourneyDialog(Context context, String content) {
        super(context, R.style.Dialog);
        this.content = content;
        initView();
    }

    public void setClearJourney(ClearJourney clearJourney) {
        this.clearJourney = clearJourney;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (CustomClearJourneyDialog.this.isShowing())
                    CustomClearJourneyDialog.this.dismiss();
                break;
        }
        return true;
    }

    private void initView() {
        setContentView(R.layout.clear_dialog_view);
        ((TextView) findViewById(R.id.tvcontent)).setText(content);
        setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.alpha = 0.9f;
        getWindow().setAttributes(attributes);
        setCancelable(true);
        btnEnsure = findViewById(R.id.btn_ensure);
        btnCancel = findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        btnEnsure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clearJourney != null) {
                    clearJourney.clear();
                }
            }
        });
    }

    public interface ClearJourney {
        void clear();
    }
}
