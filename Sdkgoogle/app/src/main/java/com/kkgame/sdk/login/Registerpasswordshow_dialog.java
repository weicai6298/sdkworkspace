package com.kkgame.sdk.login;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kkgame.sdk.utils.Basedialogview;
import com.kkgame.sdk.xml.GetAssetsutils;
import com.kkgame.sdk.xml.MachineFactory;



public class Registerpasswordshow_dialog extends Basedialogview {

	private TextView tv_line2;

	public Registerpasswordshow_dialog(Activity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createDialog(final Activity mActivity) {
		dialog = new Dialog(mContext);

		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		baselin = new LinearLayout(mContext);
		baselin.setOrientation(LinearLayout.VERTICAL);
		MachineFactory machineFactory = new MachineFactory(mActivity);
		machineFactory.MachineView(baselin, 724, 650, "LinearLayout");
		baselin.setBackgroundColor(Color.WHITE);
		baselin.setGravity(Gravity.CENTER_VERTICAL);

		// 中间内容
		RelativeLayout rl_content = new RelativeLayout(mContext);
		machineFactory.MachineView(rl_content, 724, 450, mLinearLayout);
		rl_content.setBackgroundColor(Color.WHITE);

		// 中间内容
		LinearLayout ll_content = new LinearLayout(mContext);
		machineFactory.MachineView(ll_content, 724, 450, "LinearLayout");
		ll_content.setBackgroundColor(Color.WHITE);
		ll_content.setGravity(Gravity.CENTER_HORIZONTAL);
		ll_content.setOrientation(LinearLayout.VERTICAL);

		// 头部
		LinearLayout ll_title = new LinearLayout(mActivity);
		machineFactory.MachineView(ll_title, MATCH_PARENT, 130, mLinearLayout);
		ll_title.setGravity(Gravity_CENTER);
		ll_title.setOrientation(LinearLayout.VERTICAL);
		ll_title.setBackgroundColor(Color.parseColor("#f1f1f1"));

		// 头部icon
		ImageView iv_icon = new ImageView(mActivity);
		machineFactory.MachineView(iv_icon, WRAP_CONTENT, WRAP_CONTENT,
				mLinearLayout);
		iv_icon.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"yaya_logo_ho1.png", mActivity));
		// TODO
		ll_title.addView(iv_icon);

		TextView tv_line1 = new TextView(mActivity);
		machineFactory.MachineTextView(tv_line1, MATCH_PARENT, WRAP_CONTENT, 0,
				"您的初始密码为：", 24, mLinearLayout, 0, 30, 0, 0);
		tv_line1.setGravity(Gravity_CENTER);

		tv_line2 = new TextView(mActivity);
		machineFactory.MachineTextView(tv_line2, MATCH_PARENT, WRAP_CONTENT, 0,
				"", 24, mLinearLayout, 0, 0, 0, 0);
		tv_line2.setGravity(Gravity_CENTER);
		tv_line2.setTextColor(Color.RED);

		TextView tv_line3 = new TextView(mActivity);
		machineFactory.MachineTextView(tv_line3, MATCH_PARENT, WRAP_CONTENT, 0,
				"请妥善保管", 24, mLinearLayout, 0, 10, 0, 0);
		tv_line3.setGravity(Gravity_CENTER);

		TextView tv_line4 = new TextView(mActivity);
		machineFactory.MachineTextView(tv_line4, MATCH_PARENT, WRAP_CONTENT, 0,
				"400-004-2115", 24, mLinearLayout, 0, 0, 0, 0);
		tv_line4.setGravity(Gravity_CENTER);

		TextView tv_line5 = new TextView(mActivity);
		machineFactory.MachineTextView(tv_line5, MATCH_PARENT, WRAP_CONTENT, 0,
				"投诉邮箱", 24, mLinearLayout, 0, 10, 0, 0);
		tv_line5.setGravity(Gravity_CENTER);

		TextView tv_line6 = new TextView(mActivity);
		machineFactory.MachineTextView(tv_line6, MATCH_PARENT, WRAP_CONTENT, 0,
				"kf@yayawan.com", 24, mLinearLayout, 0, 0, 0, 0);
		tv_line6.setGravity(Gravity_CENTER);

		TextView tv_line7 = new TextView(mActivity);
		machineFactory.MachineTextView(tv_line7, MATCH_PARENT, WRAP_CONTENT, 0,
				"官方网址", 24, mLinearLayout, 0, 10, 0, 0);
		tv_line7.setGravity(Gravity_CENTER);

		TextView tv_line8 = new TextView(mActivity);
		machineFactory.MachineTextView(tv_line8, MATCH_PARENT, WRAP_CONTENT, 0,
				"www.yayawan.com", 24, mLinearLayout, 0, 0, 0, 0);
		tv_line8.setGravity(Gravity_CENTER);
		
		
		TextView tv_line11 = new TextView(mActivity);
		machineFactory.MachineTextView(tv_line11, MATCH_PARENT, WRAP_CONTENT, 0,
				"客服qq：2518792532(点击打开会话)", 24, mLinearLayout, 0, 0, 0, 0);
		tv_line11.setGravity(Gravity_CENTER);
		tv_line11.setTextColor(Color.BLUE);
		tv_line11.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 try {
					  mActivity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(StringConstants.QQ_KEFUURL)));  

				} catch (Exception e) {
					// TODO: handle exception
					mActivity.runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							Toast.makeText(mActivity, "检测到无qq应用，请手动添加qq客服为好友", 0).show();
						}
					});
				}
		      
			}
		});
		
		TextView tv_line9 = new TextView(mActivity);
		machineFactory.MachineTextView(tv_line9, MATCH_PARENT, WRAP_CONTENT, 0,
				"", 20, mLinearLayout, 0, 10, 0, 0);
		tv_line9.setGravity(Gravity_CENTER);
		tv_line9.setText("版本:"+ViewConstants.SDKVERSION);
		TextView tv_line10 = new TextView(mActivity);
		machineFactory.MachineTextView(tv_line10, MATCH_PARENT, WRAP_CONTENT, 0,
				"", 16, mLinearLayout, 0, 10, 0, 0);
		tv_line10.setGravity(Gravity_CENTER);
		tv_line10.setText("温馨提示:用手机找回密码可在登录时,点击切换账号,弹出登陆框,点击忘记密码");
		
		
		ImageView iv_banquan = new ImageView(mActivity);
		machineFactory.MachineView(iv_banquan, WRAP_CONTENT, WRAP_CONTENT,
				mLinearLayout);
		// iv_banquan.setImageDrawable(GetAssetsutils.getDrawableFromAssetsFile("yaya_copyright.png",
		// mContext));
		iv_banquan.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"yaya_copyright.png", mActivity));

		ll_content.addView(ll_title);
		ll_content.addView(tv_line1);

		ll_content.addView(tv_line2);
		ll_content.addView(tv_line3);
//		ll_content.addView(tv_line4);
//		ll_content.addView(tv_line5);
//		ll_content.addView(tv_line6);
//		ll_content.addView(tv_line7);
//		ll_content.addView(tv_line8);
//		ll_content.addView(tv_line11);
//		
//		ll_content.addView(tv_line9);
//		ll_content.addView(tv_line10);

//		ll_content.addView(iv_banquan);

		rl_content.addView(ll_content);

		// ll_content.addView(chongzhihelp2);

		baselin.addView(rl_content);

		dialog.setContentView(baselin);

		Window dialogWindow = dialog.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		dialogWindow.setGravity(Gravity.CENTER);

		lp.alpha = 0.9f; // 透明度

		lp.dimAmount = 0.5f; // 设置背景色对比度
		dialogWindow.setAttributes(lp);
		dialog.setCanceledOnTouchOutside(false);

		RelativeLayout.LayoutParams ap2 = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);

		dialog.setCanceledOnTouchOutside(true);
		dialog.getWindow().setBackgroundDrawable(new BitmapDrawable());

	}

	public TextView getTv_line2() {
		return tv_line2;
	}

	public void setTv_line2(TextView tv_line2) {
		this.tv_line2 = tv_line2;
	}
	
	

}
