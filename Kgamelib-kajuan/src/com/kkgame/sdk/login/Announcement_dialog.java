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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kkgame.sdk.bean.Question;
import com.kkgame.sdk.utils.Basedialogview;
import com.kkgame.sdk.xml.GetAssetsutils;
import com.kkgame.sdk.xml.MachineFactory;
import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Yayalog;

public class Announcement_dialog extends Basedialogview {

	private LinearLayout ll_mPre;
	private ImageButton iv_mPre;
	private WebView lv_helpcontent;
	private ProgressBar pb_mPb;
	private ArrayList<Question> mQuestionList;
	private String html;
	protected static final int SHOWCONTENT = 3;

	public Announcement_dialog(Activity activity) {
		super(activity);
	}
	public Announcement_dialog(Activity activity,String html) {
		
		super(activity);
		Yayalog.loger("wo"+html);
		this.html=html;
		initlogic();
		
	}

	@Override
	public void createDialog(Activity mActivity) {
		dialog = new Dialog(mContext);

		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		int ho_height = 650;
		int ho_with = 750;
		int po_height = 850;
		int po_with = 650;

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
				25);
		ll_content.setBackgroundColor(Color.WHITE);
		ll_content.setGravity(Gravity.CENTER_HORIZONTAL);
		ll_content.setOrientation(LinearLayout.VERTICAL);

		// 标题栏
		RelativeLayout rl_title = new RelativeLayout(mContext);
		machineFactory.MachineView(rl_title, MATCH_PARENT, 96, mLinearLayout);
		rl_title.setBackgroundColor(Color.parseColor("#999999"));

		ll_mPre = new LinearLayout(mContext);
		machineFactory.MachineView(ll_mPre, 96, MATCH_PARENT, 0,
				mRelativeLayout, 0, 0, 0, 0, RelativeLayout.CENTER_VERTICAL);
		ll_mPre.setGravity(Gravity_CENTER);
		ll_mPre.setClickable(true);
		// 返回上一层的图片
		iv_mPre = new ImageButton(mContext);
		machineFactory.MachineView(iv_mPre, 40, 40, 0, mLinearLayout, 0, 0, 0,
				0, RelativeLayout.CENTER_VERTICAL);
		iv_mPre.setClickable(false);
		/*
		 * iv_mPre.setImageDrawable(GetAssetsutils.getDrawableFromAssetsFile(
		 * "yaya_pre.png", mContext));
		 */
		iv_mPre.setBackgroundDrawable(GetAssetsutils.getDrawableFromAssetsFile(
				"yaya_pre.png", mActivity));
		ll_mPre.addView(iv_mPre);
		ll_mPre.setClickable(true);
		ll_mPre.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();

			}
		});

		// 注册textview
		TextView tv_zhuce = new TextView(mContext);
		machineFactory.MachineTextView(tv_zhuce, MATCH_PARENT, MATCH_PARENT, 0,
				"游戏公告", 38, mLinearLayout, 0, 0, 0, 0);
		tv_zhuce.setTextColor(Color.WHITE);
		tv_zhuce.setGravity(Gravity_CENTER);

		// TODO
		rl_title.addView(ll_mPre);
		rl_title.addView(tv_zhuce);

		pb_mPb = new ProgressBar(mActivity);
		machineFactory.MachineView(pb_mPb, 40, 40, mLinearLayout, 2, 400);

		// 帮助的列表内容
		lv_helpcontent = new WebView(mActivity);
		machineFactory.MachineView(lv_helpcontent, MATCH_PARENT, MATCH_PARENT,
				0, mLinearLayout, 20, 0, 20, 10, 100);
		

		ll_content.addView(rl_title);
		ll_content.addView(lv_helpcontent);
		ll_content.addView(pb_mPb);

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

		android.widget.RelativeLayout.LayoutParams ap2 = new android.widget.RelativeLayout.LayoutParams(
				android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT,
				android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);

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
		lv_helpcontent.loadData(html, "text/html; charset=UTF-8", null);
	}
	
	

}
