package com.kkgame.sdk.pay;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kkgame.sdk.login.ViewConstants;
import com.kkgame.sdk.xml.Basexml;
import com.kkgame.sdk.xml.GetAssetsutils;
import com.kkgame.sdk.xml.Layoutxml;
import com.kkgame.sdk.xml.MachineFactory;

public class XiaomiPayxml extends Basexml implements Layoutxml {

	private LinearLayout baseLinearLayout;
	private Button bt_mMorepay;
	private ImageView iv_bluepay;
	private ImageView iv_xiaomipay;
	private ImageView iv_greenp;
	private ImageView iv_buttonicon;

	public XiaomiPayxml(Activity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View initViewxml() {
		// TODO Auto-generated method stub
		// 基类布局
		baseLinearLayout = new LinearLayout(mContext);

		baseLinearLayout.setOrientation(LinearLayout.VERTICAL);
		baseLinearLayout.setGravity(Gravity.CENTER_HORIZONTAL);
		MachineFactory machineFactory = new MachineFactory(mActivity);

		machineFactory.MachineView(baseLinearLayout,
				ViewConstants.getHoldActivityWith(mContext),
				ViewConstants.getHoldActivityHeight(mContext), "LinearLayout");
		baseLinearLayout.setBackgroundColor(Color.TRANSPARENT);

		// 上面空白部分
		LinearLayout onelinearLayout = new LinearLayout(mContext);
		machineFactory.MachineView(onelinearLayout,
				ViewConstants.getHoldActivityWith(mContext), ViewConstants.getHoldActivityHeight(mContext)-520, 0,
				mLinearLayout);
		onelinearLayout.setBackgroundColor(Color.TRANSPARENT);

		// 下面内容部分
		LinearLayout twolinearLayout = new LinearLayout(mContext);
		machineFactory.MachineView(twolinearLayout, 630, 520, 0, mLinearLayout);
		twolinearLayout.setBackgroundColor(Color.WHITE);
		twolinearLayout.setGravity(Gravity.CENTER_HORIZONTAL);
		twolinearLayout.setOrientation(LinearLayout.VERTICAL);

		RelativeLayout rl_titilebar = new RelativeLayout(mContext);
		machineFactory.MachineView(rl_titilebar, 630, 96, 0, mLinearLayout);

		// 物品名字 冰霜之剑-元宝
		
		tv_goodsname = new TextView(mContext);
		machineFactory.MachineTextView(tv_goodsname, WRAP_CONTENT,
				MATCH_PARENT, 0, "", 28, mRelativeLayout, 25, 0, 0, 0);
		tv_goodsname.setGravity(Gravity_CENTER);
		tv_goodsname.setTextColor(Color.parseColor("#1d1d1d"));

		tv_goodsmoney = new TextView(mContext);
		machineFactory.MachineTextView(tv_goodsmoney, WRAP_CONTENT,
				MATCH_PARENT, 0, "30.00元", 36, mRelativeLayout, 0, 0, 25, 0,
				RelativeLayout.ALIGN_PARENT_RIGHT);
		tv_goodsmoney.setGravity(Gravity_CENTER);
		tv_goodsmoney.setTextColor(Color.parseColor("#00a4e5"));

		// 添加物品和价格
		rl_titilebar.addView(tv_goodsname);
		rl_titilebar.addView(tv_goodsmoney);

		// 横线
		View line = new View(mContext);
		machineFactory.MachineView(line, 630, 3, mLinearLayout);
		line.setBackgroundColor(Color.parseColor("#ededed"));

		// 请选择支付方式
		TextView tv_choosepaytype = new TextView(mContext);
		machineFactory.MachineTextView(tv_choosepaytype, MATCH_PARENT, 75, 0,
				"请选择支付方式", 28, mLinearLayout, 18, 0, 0, 0);
		tv_choosepaytype.setGravity(Gravity.CENTER_VERTICAL);
		tv_choosepaytype.setTextColor(Color.parseColor("#1d1d1d"));

		// 支付图标行
		LinearLayout ll_icon = new LinearLayout(mContext);
		machineFactory.MachineView(ll_icon, 630, 200, 0, mLinearLayout, 80, 0,
				80, 0, 100);
		ll_icon.setBackgroundColor(Color.WHITE);
		ll_icon.setGravity(Gravity_CENTER);
		ll_icon.setOrientation(LinearLayout.HORIZONTAL);

		iv_greenp = new ImageView(mContext);
		machineFactory.MachineView(iv_greenp, 160, 160, 0, mLinearLayout,
				20, 0, 20, 0, 100);
		// machineFactory.MachineView(iv_greenp, 160, 160, 0, mLinearLayout);
		iv_greenp.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"xiaomi_greenpchoosed.png", mActivity));
		

		iv_bluepay = new ImageView(mContext);
		machineFactory.MachineView(iv_bluepay, 160, 160, 0, mLinearLayout, 20,
				0, 20, 0, 100);
		iv_bluepay.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"xiaomi_bluepaychoose.png", mActivity));

		iv_xiaomipay = new ImageView(mContext);
		machineFactory.MachineView(iv_xiaomipay, 160, 160, 0, mLinearLayout,
				20, 0, 20, 0, 100);
		iv_xiaomipay.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"xiaomi_xiaomipay.png", mActivity));

		ll_icon.addView(iv_greenp);
		ll_icon.addView(iv_bluepay);
		ll_icon.addView(iv_xiaomipay);

		// 本次支付由小米官方保障
		TextView tv_tip = new TextView(mContext);
		machineFactory.MachineTextView(tv_tip, MATCH_PARENT, 50, 0,
				"本次支付由小米官方保障", 25, mLinearLayout, 0, 0, 0, 0);
		tv_tip.setGravity(Gravity.CENTER);
		tv_tip.setTextColor(Color.parseColor("#939393"));

		ll_button = new LinearLayout(mContext);
		machineFactory.MachineView(ll_button, 570, 76, 0, mLinearLayout, 0, 0,
				0, 0, 100);
		ll_button.setBackgroundColor(Color.parseColor("#00a4e5"));
		ll_button.setGravity(Gravity_CENTER);
		
		iv_buttonicon = new ImageView(mContext);
		machineFactory.MachineView(iv_buttonicon, 35, 30, 0, mLinearLayout,
				0, 0, 0, 0, 100);
		// machineFactory.MachineView(iv_greenp, 160, 160, 0, mLinearLayout);
		iv_buttonicon.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"xiaomi_paydun.png", mActivity));
		
		
		tv_buttext = new TextView(mContext);
		machineFactory.MachineTextView(tv_buttext, WRAP_CONTENT, MATCH_PARENT, 0,
				"使用微信支付30.00元", 30, mLinearLayout, 0, 0, 0, 0);
		tv_buttext.setGravity(Gravity.CENTER);
		tv_buttext.setTextColor(Color.WHITE);
		
		ll_button.addView(iv_buttonicon);
		ll_button.addView(tv_buttext);

		
		

		twolinearLayout.addView(rl_titilebar);
		twolinearLayout.addView(line);
		twolinearLayout.addView(tv_choosepaytype);
		twolinearLayout.addView(ll_icon);
		twolinearLayout.addView(tv_tip);
		twolinearLayout.addView(ll_button);

		baseLinearLayout.addView(onelinearLayout);
		baseLinearLayout.addView(twolinearLayout);

		initLogic();

		return baseLinearLayout;

	}

	private void initLogic() {
		// TODO Auto-generated method stub
		iv_bluepay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				selectpaytye=BLUEP;
				// TODO Auto-generated method stub
				tv_buttext.setText("使用支付宝支付"+mPrice+".00元");
				iv_greenp.setImageBitmap(GetAssetsutils
						.getImageFromAssetsFile("xiaomi_greenp.png",
								mActivity));

				iv_bluepay.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
						"xiaomi_bluepaychoosed.png", mActivity));

				iv_xiaomipay.setImageBitmap(GetAssetsutils
						.getImageFromAssetsFile("xiaomi_xiaomipay.png",
								mActivity));

			}
		});

		iv_greenp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				selectpaytye=GREENP;
				// TODO Auto-generated method stub
				tv_buttext.setText("使用微信支付"+mPrice+".00元");
				iv_greenp.setImageBitmap(GetAssetsutils
						.getImageFromAssetsFile("xiaomi_greenpchoosed.png",
								mActivity));

				iv_bluepay.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
						"xiaomi_bluepaychoose.png", mActivity));

				iv_xiaomipay.setImageBitmap(GetAssetsutils
						.getImageFromAssetsFile("xiaomi_xiaomipay.png",
								mActivity));
			}
		});
		iv_xiaomipay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				selectpaytye=XIAOMIPAY;
				tv_buttext.setText("使用小米钱包支付"+mPrice+".00元");
				iv_greenp.setImageBitmap(GetAssetsutils
						.getImageFromAssetsFile("xiaomi_greenp.png",
								mActivity));

				iv_bluepay.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
						"xiaomi_bluepaychoose.png", mActivity));

				iv_xiaomipay.setImageBitmap(GetAssetsutils
						.getImageFromAssetsFile("xiaomi_xiaomipaychoosed.png",
								mActivity));
			}
		});
		ll_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mXiaomiPaylistener!=null) {
					mXiaomiPaylistener.onGoToPay(selectpaytye);
				}
			}
		});
	}
	public int selectpaytye=2;
	
	public int BLUEP=1;
	public int GREENP=2;
	public int XIAOMIPAY=3;
	public XiaomiPayListener mXiaomiPaylistener;
	private LinearLayout ll_button;
	private TextView tv_goodsmoney;
	private TextView tv_buttext;
	private int mPrice;
	private TextView tv_goodsname;
	public void addXiaomiPayListener(XiaomiPayListener mXiaomipay){
		mXiaomiPaylistener=mXiaomipay;
	}

public void setGoodsText(String name){
		
		tv_goodsname.setText(name);

	}
	public void setPrice(int price){
		mPrice=price;
		tv_goodsmoney.setText(price+".00元");
		tv_buttext.setText("使用微信支付"+price+".00元");
	}
	interface XiaomiPayListener{
		
		public void onGoToPay(int selectpaytype);
	}
	
}
