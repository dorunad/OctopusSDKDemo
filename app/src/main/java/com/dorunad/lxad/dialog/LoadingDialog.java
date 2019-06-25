package com.dorunad.lxad.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.dorunad.lxad.R;


public class LoadingDialog extends Dialog {
    private TextView tvMessage;
    private Context context;
    /**
     * 下方显示message
     */
    private String message;

    public LoadingDialog(@NonNull Context context) {
        super(context, R.style.LoadingDialog);
        this.context = context;
    }

    public LoadingDialog(Context context, String message) {
        super(context, R.style.LoadingDialog);
        this.context = context;
        this.message = message;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.loading_layout);
        this.setCancelable(true);
        this.setCanceledOnTouchOutside(false);
        tvMessage = (TextView) findViewById(R.id.tv_loading_msg);


        if (!TextUtils.isEmpty(message)) {
            tvMessage.setText(message);
            tvMessage.setVisibility(View.VISIBLE);
        } else {
            tvMessage.setVisibility(View.GONE);
        }
    }


    public void show(String message) {
        this.message = message;
        if (tvMessage != null && !TextUtils.isEmpty(message)) {
            tvMessage.setText(message);
            tvMessage.setVisibility(View.VISIBLE);

        } else if (tvMessage != null && TextUtils.isEmpty(message)) {
            tvMessage.setVisibility(View.GONE);
        }
        super.show();
    }

    public void show(int msgResId) {
        show(getContext().getString(msgResId));
    }

}
