package com.kkgame.sdk.login;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kkgame.common.CommonData;
import com.kkgame.sdk.bean.Question;
import com.kkgame.sdk.callback.ExitdialogCallBack;
import com.kkgame.sdk.utils.Basedialogview;
import com.kkgame.sdk.xml.GetAssetsutils;
import com.kkgame.sdk.xml.MachineFactory;
import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Yayalog;

public class Exit_dialog extends Basedialogview {

	private LinearLayout ll_mPre;
	private ImageButton iv_mPre;
	private WebView lv_helpcontent;
	private ProgressBar pb_mPb;
	private ArrayList<Question> mQuestionList;
	private String html;
	private Button bt_mlogin;
	protected static final int SHOWCONTENT = 3;
	public ExitdialogCallBack mExitdialogcallback;
	public Exit_dialog(Activity activity) {
		super(activity);
	}
	public Exit_dialog(Activity activity,String html,ExitdialogCallBack mexitdialogcallback) {
		
		super(activity);
		Yayalog.loger("wo"+html);
		this.html=html;
		mExitdialogcallback=mexitdialogcallback;
		initlogic();
		
	}

	@Override
	public void createDialog(Activity mActivity) {
		dialog = new Dialog(mContext);

		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		int ho_height = 384;
		int ho_with = 628;
		int po_height = 384;
		int po_with = 628;

		int height = 0;
		int with = 0;
		int pad = 0;
		// 设置横竖屏
		String orientation = DeviceUtil.getOrientation(mContext);
		if (orientation == "") {

		} else if ("landscape".equals(orientation)) {
			height = ho_height;
			with = ho_with;

		} else if ("portrait".equals(orientation)) {
			height = po_height;
			with = po_with;
		}

		baselin = new LinearLayout(mContext);
		baselin.setOrientation(LinearLayout.VERTICAL);
		MachineFactory machineFactory = new MachineFactory(mActivity);
		machineFactory.MachineView(baselin, with, height, "LinearLayout");
		baselin.setBackgroundColor(Color.TRANSPARENT);
		baselin.setGravity(Gravity.CENTER_VERTICAL);

		// 中间内容
		LinearLayout ll_content = new LinearLayout(mContext);
		machineFactory.MachineView(ll_content, with, height, mLinearLayout, 2,
				0);
		ll_content.setBackgroundColor(Color.WHITE);
		ll_content.setGravity(Gravity.CENTER_HORIZONTAL);
		ll_content.setOrientation(LinearLayout.VERTICAL);



	

	

		// webview
		lv_helpcontent = new WebView(mActivity);
		machineFactory.MachineView(lv_helpcontent, MATCH_PARENT, 300,
				0, mLinearLayout, 0, 0, 0, 0, 100);
		

		// button的退出
				bt_mlogin = new Button(mActivity);
				bt_mlogin = machineFactory.MachineButton(bt_mlogin, MATCH_PARENT, 84, 1,
						"退出游戏", 25, mLinearLayout, 0, 0, 0, 0);
				bt_mlogin.setTextColor(Color.WHITE);
				bt_mlogin.setBackgroundColor(Color.BLACK);
				bt_mlogin.setPadding(0, 0, 0, 0);
				bt_mlogin.setGravity(Gravity_CENTER);
				bt_mlogin.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						mExitdialogcallback.goExit();
						dialogDismiss();
					}
				});
		
		//ll_content.addView(rl_title);
		ll_content.addView(lv_helpcontent);
		ll_content.addView(bt_mlogin);

		// baselin.addView(rl_title);
		baselin.addView(ll_content);

		dialog.setContentView(baselin);

		Window dialogWindow = dialog.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		dialogWindow.setGravity(Gravity.CENTER);

		lp.alpha = 1f; // 透明度

		lp.dimAmount = 0.5f; // 设置背景色对比度
		dialogWindow.setAttributes(lp);
		dialog.setCanceledOnTouchOutside(false);

		RelativeLayout.LayoutParams ap2 = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);

		dialog.setCanceledOnTouchOutside(true);
		dialog.getWindow().setBackgroundDrawable(new BitmapDrawable());

		
	}

	private void initlogic() {
		
		WebSettings settings = lv_helpcontent.getSettings();
		settings.setSupportZoom(true); // 支持缩放
		settings.setBuiltInZoomControls(false); // 启用内置缩放装置
		settings.setJavaScriptEnabled(true); // 启用JS脚本
		settings.setCacheMode(WebSettings.LOAD_NO_CACHE);// 关闭webview中缓存
		
		settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
		Yayalog.loger("ni..."+html);
		//lv_helpcontent.loadUrl("http://danjiyou.duapp.com/Home/Blog/index");
		settings.setDefaultTextEncodingName("utf-8"); //设置文本编码
		
		lv_helpcontent.loadUrl(CommonData.exiturl);
		//lv_helpcontent.loadData(html, "text/html; charset=UTF-8", null);
	}
	

}
