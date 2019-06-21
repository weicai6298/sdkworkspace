package com.kkgame.sdk.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;



public class LoadDialog extends Dialog {
    private Context mContext;

    private LayoutInflater inflater;

    private LayoutParams lp;

    private ProgressBar progressBar;

    private TextView mMessage;

    private TextView mSpeed;

    public LoadDialog(Context context) {
        super(context, ResourceUtil.getStyleId(context, "CustomProgressDialog"));
        this.mContext = context;

        inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(ResourceUtil.getLayoutId(mContext,
                "prompt_update_loading_dialog"), null);
        progressBar = (ProgressBar) layout.findViewById(ResourceUtil.getId(
                mContext, "pb_loading"));
        mMessage = (TextView) layout.findViewById(ResourceUtil.getId(mContext,
                "tv_message"));
        mSpeed = (TextView) layout.findViewById(ResourceUtil.getId(mContext,
                "tv_speed"));
        mMessage.setText("正在下载");
        setContentView(layout);

        // 设置window属性
        lp = getWindow().getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.dimAmount = (float) 0.5; // 设置背景遮盖
        lp.alpha = 1.0f;
        getWindow().setAttributes(lp);
    }

    public void setProgress(int max, int progress, String speed) {
        progressBar.setMax(max);
        progressBar.setProgress(progress);
        mSpeed.setText(speed);
    }

}
