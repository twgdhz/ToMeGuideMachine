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
 * description:设备关机
 */
public class CustomPowerOffDialog extends Dialog {
    private String content;
    private Button btnEnsure, btnCancel;
    private Context context;
    public ClearCache clearCache;

    public CustomPowerOffDialog(Context context, String content) {
        super(context, R.style.Dialog);
        this.content = content;
        this.context = context;
        initView();
    }

    public void setClearCache(ClearCache clearCache) {
        this.clearCache = clearCache;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (CustomPowerOffDialog.this.isShowing())
                    CustomPowerOffDialog.this.dismiss();
                break;
        }
        return true;
    }

    private void initView() {
        setContentView(R.layout.power_off_dialog_view);
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
//        btnEnsure.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (clearCache != null) {
//                    clearCache.clear();
//                }
//                dismiss();
//            }
//        });
    }

    public CustomPowerOffDialog setOffButton(final View.OnClickListener listener) {

        btnEnsure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
                dismiss();
            }
        });
        return this;
    }


    public interface ClearCache {
        void clear();
    }
}
