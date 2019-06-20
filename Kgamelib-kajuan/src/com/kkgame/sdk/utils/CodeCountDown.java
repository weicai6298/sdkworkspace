package com.kkgame.sdk.utils;

import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;

public class CodeCountDown extends CountDownTimer {

    public Button mView;

    public CodeCountDown(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    public CodeCountDown(long millisInFuture, long countDownInterval,
            Button view) {
        super(millisInFuture, countDownInterval);
        this.mView = view;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        mView.setEnabled(false);
        mView.setText("重新获取(" + millisUntilFinished / 1000 + ")");
    }

    @Override
    public void onFinish() {
        mView.setText("获取验证码");
        mView.setEnabled(true);
    }

}
