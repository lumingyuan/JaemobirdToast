package com.jaemobird.toast;

import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/** ToastPlugin */
public class ToastPlugin implements MethodCallHandler {
  private static Context context;

  /** Plugin registration. */
  public static void registerWith(Registrar registrar) {
    context = registrar.activity();

    final MethodChannel channel = new MethodChannel(registrar.messenger(), "toast");
    channel.setMethodCallHandler(new ToastPlugin());
  }

  private LoadingDialog mLoadingDialog = null;

    @Override
    public void onMethodCall(MethodCall call, Result result) {
        if (call.method.equals("getPlatformVersion")) {
            result.success("Android " + android.os.Build.VERSION.RELEASE);
        } else if (call.method.equals("showToast")) {
            boolean success = call.argument("success");
            int time = call.argument("time");
            String text = call.argument("text");
            showToast(text, success ? 2 : 1, time);
            result.success(true);
        } else if (call.method.equals("showLoading")) {
            mLoadingDialog = new LoadingDialog(context, (String) (call.argument("text")));
            mLoadingDialog.show();
            result.success(true);
        } else if (call.method.equals("hideLoading")) {
            if (mLoadingDialog != null) {
                mLoadingDialog.dismiss();
            }
            result.success(true);
        } else {
            result.notImplemented();
        }
    }

    /**
     * 自定义吐司（2.5版本，大部分通用）
     *
     * @param toastTxt 吐司内容
     * @param type     1是感叹号、2是对号、3是问号、4是兑换中符号
     * @param time  时间 单位毫秒
     */
    public void showToast(String toastTxt, int type, int time) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_custom_toast, null);
        ImageView customImg = (ImageView) view.findViewById(R.id.iv_custom_img);
        TextView customText = (TextView) view.findViewById(R.id.tv_custom_text);

        switch (type) {
            case 1: //感叹号
                customImg.setBackground(context.getResources().getDrawable(R.mipmap.ic_toast_attention));
                customImg.setVisibility(View.VISIBLE);
                break;
            case 2: //对号
                customImg.setBackground(context.getResources().getDrawable(R.mipmap.ic_toast_succeed));
                customImg.setVisibility(View.VISIBLE);
                break;
            case 3: //问号
                customImg.setBackground(context.getResources().getDrawable(R.mipmap.ic_toast_wrong));
                customImg.setVisibility(View.VISIBLE);
                break;
            default: //隐藏图片
                customImg.setVisibility(View.GONE);
                break;
        }
        if (!toastTxt.equals("")) {
            customText.setText(toastTxt);
        } else {
            customText.setText("请稍后！");
        }
        final Toast toast = Toast.makeText(context, null, Toast.LENGTH_LONG);
        toast.setView(view);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toast.cancel();
            }
        }, time);
    }
}
