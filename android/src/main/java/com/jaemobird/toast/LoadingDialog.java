package com.jaemobird.toast;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by LT on 2018/5/12.
 */
public class LoadingDialog extends Dialog {

    private static final String TAG = "LoadingDialog";

    private String mMessage;
    private int mImageId;
    private boolean mCancelable;
    //private RotateAnimation mRotateAnimation;

    public LoadingDialog(Context context, String message) {
        this(context,R.style.LoadingDialog,message,false);
    }

    public LoadingDialog(Context context, int themeResId, String message, boolean cancelable) {
        super(context, themeResId);
        mMessage = message;
        mImageId = R.mipmap.ic_dialog_loading;
        mCancelable = cancelable;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        initView();
    }

    private void initView() {
        setContentView(R.layout.loading_dialog);
        // 设置窗口大小
        WindowManager windowManager = getWindow().getWindowManager();
        int screenWidth = windowManager.getDefaultDisplay().getWidth();
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.alpha = 0.7f;
        attributes.width = screenWidth/3;
        attributes.height = attributes.width;
        getWindow().setAttributes(attributes);
        setCancelable(mCancelable);

        TextView tv_loading = findViewById(R.id.tv_loading);
        tv_loading.setText(mMessage);

//        ImageView iv_loading = findViewById(R.id.iv_loading);
//        iv_loading.setImageResource(mImageId);
//        iv_loading.measure(0,0);
//        mRotateAnimation = new RotateAnimation(0,360,iv_loading.getMeasuredWidth()/2,iv_loading.getMeasuredHeight()/2);
//        mRotateAnimation.setInterpolator(new LinearInterpolator());
//        mRotateAnimation.setDuration(1500);
//        mRotateAnimation.setRepeatCount(-1);
//        iv_loading.startAnimation(mRotateAnimation);
    }

    @Override
    public void dismiss() {
        //mRotateAnimation.cancel();
        super.dismiss();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            // 屏蔽返回键
            return mCancelable;
        }
        return super.onKeyDown(keyCode, event);
    }
}
