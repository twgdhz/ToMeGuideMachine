package com.guidemachine.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.guidemachine.R;


/**
 * Created by Brother_Lin on 2016/5/10.
 */
public class CustomAlertDialog extends Dialog {

    public CustomAlertDialog(Context context) {
        super(context);
    }

    public CustomAlertDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public static class Builder {
        private Context context;
        private String title;
        private String message;
        private String positiveButtonText;
        private String negativeButtonText;
        private View contentView;
        private DialogInterface.OnClickListener positiveButtonClickListener;
        private DialogInterface.OnClickListener negativeButtonClickListener;
        private int positiveTextColor = -1;
        private int negativeTextColor = -1;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        public Builder setPositiveTextColor(int positiveTextColor) {
            this.positiveTextColor = positiveTextColor;
            return this;
        }

        public Builder setNegativeTextColor(int negativeTextColor) {
            this.negativeTextColor = negativeTextColor;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText,
                                         DialogInterface.OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }


        public Builder setNegativeButton(String negativeButtonText,
                                         DialogInterface.OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }

        public CustomAlertDialog create() {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final CustomAlertDialog customAlertDialog = new CustomAlertDialog(context, R.style.customdialog);
            View view = inflater.inflate(R.layout.dialog_custom, null);
            customAlertDialog.addContentView(view, new LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            if (title != null) {
                ((TextView) view.findViewById(R.id.txt_custom_dialog_title)).setText(title);
            } else {
                ((TextView) view.findViewById(R.id.txt_custom_dialog_title)).setVisibility(View.GONE);
            }
            if (positiveButtonText != null) {
                TextView textViewPositive = ((TextView) view.findViewById(R.id.txt_custom_dialog_positiveButtonText));
                textViewPositive.setText(positiveButtonText);
                if (positiveTextColor != -1)
                    textViewPositive.setTextColor(positiveTextColor);
                if (positiveButtonClickListener != null) {
                    ((RelativeLayout) view.findViewById(R.id.btn_custom_dialog_positiveButton)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            positiveButtonClickListener.onClick(customAlertDialog, DialogInterface.BUTTON_POSITIVE);
                        }
                    });
                }
            } else {
                ((RelativeLayout) view.findViewById(R.id.btn_custom_dialog_positiveButton)).setVisibility(View.GONE);
            }

            if (negativeButtonText != null) {
                TextView textViewNegative = ((TextView) view.findViewById(R.id.txt_custom_dialog_negativeButtonText));
                textViewNegative.setText(negativeButtonText);
                if (negativeTextColor != -1)
                    textViewNegative.setTextColor(negativeTextColor);
                if (negativeButtonClickListener != null) {
                    ((RelativeLayout) view.findViewById(R.id.btn_custom_dialog_negativeButton)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            negativeButtonClickListener.onClick(customAlertDialog, DialogInterface.BUTTON_NEGATIVE);
                        }
                    });
                }
            } else {
                ((RelativeLayout) view.findViewById(R.id.btn_custom_dialog_negativeButton)).setVisibility(View.GONE);
            }

            if (message != null) {
                ((TextView) view.findViewById(R.id.txt_custom_dialog_message)).setText(message);
            } else if (contentView != null) {

                RelativeLayout linearLayout = (RelativeLayout) view.findViewById(R.id.layout_custom_dialog_content);
                linearLayout.removeAllViews();
                linearLayout.addView(contentView,
                        new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));

            }
            customAlertDialog.setContentView(view);
            return customAlertDialog;
        }

    }
}
