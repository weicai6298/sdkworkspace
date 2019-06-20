package com.kkgame.sdk.pay;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kkgame.sdk.login.ViewConstants;
import com.kkgame.sdk.xml.Basexml;
import com.kkgame.sdk.xml.GetAssetsutils;
import com.kkgame.sdk.xml.Layoutxml;
import com.kkgame.sdk.xml.MachineFactory;



public class YingYongBaoPayxml extends Basexml implements Layoutxml {

	private LinearLayout baseLinearLayout;
	private Button bt_mMorepay;
	
	private ImageView iv_xiaomipay;
	private ImageView iv_greenp;
	private ImageView iv_buttonicon;

	public YingYongBaoPayxml(Activity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View initViewxml() {
		// TODO Auto-generated method stub
		// 基类布局
		baseLinearLayout = new LinearLayout(mContext);

		baseLinearLayout.setOrientation(LinearLayout.VERTICAL);
		//baseLinearLayout.setGravity(Gravity.CENTER);
		MachineFactory machineFactory = new MachineFactory(mActivity);

		machineFactory.MachineView(baseLinearLayout,
				ViewConstants.getHoldActivityWith(mContext),
				ViewConstants.getHoldActivityHeight(mContext), "LinearLayout");
		baseLinearLayout.setBackgroundColor(Color.TRANSPARENT);

		// 上面空白部分
		LinearLayout onelinearLayout = new LinearLayout(mContext);
		machineFactory.MachineView(onelinearLayout,
				ViewConstants.getHoldActivityWith(mContext), 100, 0,
				mLinearLayout);
		onelinearLayout.setBackgroundColor(Color.WHITE);
		onelinearLayout.setOrientation(LinearLayout.HORIZONTAL);
		onelinearLayout.setGravity(Gravity_CENTER);
		
		TextView mtitileView = new TextView(mContext);
		machineFactory.MachineTextView(mtitileView,MATCH_PARENT , MATCH_PARENT, 0, "确认支付方式", 28, mLinearLayout, 0, 0, 0, 0);
		mtitileView.setText("确认支付方式");
		mtitileView.setTextColor(Color.BLACK);
		mtitileView.setGravity(Gravity_CENTER);
		
		
		
		
		ImageView iv_pre = new ImageView(mContext);
		machineFactory.MachineView(iv_pre, 40, 40, 0, mLinearLayout,
				20, 0, 20, 0, 100);
		// machineFactory.MachineView(iv_greenp, 160, 160, 0, mLinearLayout);
		iv_pre.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"yaya_x.png", mActivity));
		
		
		iv_pre.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mActivity.finish();
			}
		});
		onelinearLayout.addView(iv_pre);
		onelinearLayout.addView(mtitileView);
		
		
		
		LinearLayout twolinearLayout = new LinearLayout(mContext);
		machineFactory.MachineView(twolinearLayout,
				ViewConstants.getHoldActivityWith(mContext), 200, 0,
				mLinearLayout);
		twolinearLayout.setBackgroundColor(Color.parseColor("#f5f5f5"));
		twolinearLayout.setOrientation(LinearLayout.VERTICAL);
		twolinearLayout.setGravity(Gravity_CENTER);
		
		mgoodnameView = new TextView(mContext);
		machineFactory.MachineTextView(mgoodnameView,MATCH_PARENT , 40, 0, "", 25, mLinearLayout, 0, 40, 0, 0);
		mgoodnameView.setText("元宝");
		mgoodnameView.setTextColor(Color.BLACK);
		mgoodnameView.setGravity(Gravity.CENTER);
		
		mgoodmoneyView = new TextView(mContext);
		machineFactory.MachineTextView(mgoodmoneyView,MATCH_PARENT , 60, 0, "", 48, mLinearLayout, 0, 0, 5, 0);
		mgoodmoneyView.setText("￥6.00");
		mgoodmoneyView.setTextColor(Color.BLACK);
		mgoodmoneyView.setGravity(Gravity_CENTER);
		
		twolinearLayout.addView(mgoodnameView);
		twolinearLayout.addView(mgoodmoneyView);
		

		
		LinearLayout threelinearLayout = new LinearLayout(mContext);
		machineFactory.MachineView(threelinearLayout,
				ViewConstants.getHoldActivityWith(mContext), 320, 0,
				mLinearLayout);
		threelinearLayout.setBackgroundColor(Color.parseColor("#f4f4fd"));
		threelinearLayout.setOrientation(LinearLayout.HORIZONTAL);
		threelinearLayout.setGravity(Gravity_CENTER);
		
		
		iv_qqpay = new ImageView(mContext);
		machineFactory.MachineView(iv_qqpay, 0, 280, 1, mLinearLayout,
				20, 0, 20, 0, 100);
		// machineFactory.MachineView(iv_greenp, 160, 160, 0, mLinearLayout);
		iv_qqpay.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"yaya_yingyongbao_qqpay.png", mActivity));
		
		iv_qbipay = new ImageView(mContext);
		machineFactory.MachineView(iv_qbipay, 0, 280, 1, mLinearLayout,
				20, 0, 20, 0, 100);
		// machineFactory.MachineView(iv_greenp, 160, 160, 0, mLinearLayout);
		iv_qbipay.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"yaya_yingyongbao_qbipay.png", mActivity));
		
		iv_greenp2 = new ImageView(mContext);
		machineFactory.MachineView(iv_greenp2, 0, 280, 1, mLinearLayout,
				20, 0, 20, 0, 100);
		// machineFactory.MachineView(iv_greenp, 160, 160, 0, mLinearLayout);
		iv_greenp2.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"yaya_yingyongbao_greenp.png", mActivity));
		
		iv_shoujipay = new ImageView(mContext);
		machineFactory.MachineView(iv_shoujipay, 0, 280, 1, mLinearLayout,
				20, 0, 20, 0, 100);
		// machineFactory.MachineView(iv_greenp, 160, 160, 0, mLinearLayout);
		iv_shoujipay.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"yaya_yingyongbao_shoujipay.png", mActivity));
		
		iv_bluepay = new ImageView(mContext);
		machineFactory.MachineView(iv_bluepay, 0, 280, 1, mLinearLayout,
				20, 0, 20, 0, 100);
		// machineFactory.MachineView(iv_greenp, 160, 160, 0, mLinearLayout);
		iv_bluepay.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"yaya_yingyongbao_bluepay.png", mActivity));
		
		
		
		threelinearLayout.addView(iv_greenp2);
		threelinearLayout.addView(iv_bluepay);
		threelinearLayout.addView(iv_shoujipay);
		threelinearLayout.addView(iv_qqpay);
		threelinearLayout.addView(iv_qbipay);
		//threelinearLayout.addView(iv_qbipay);
		


		baseLinearLayout.addView(onelinearLayout);
		baseLinearLayout.addView(twolinearLayout);
		baseLinearLayout.addView(threelinearLayout);
		
		initLogic();
		System.err.println("返回的view");
		return baseLinearLayout;

	}

	private void initLogic() {
		// TODO Auto-generated method stub
		iv_bluepay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				selectpaytye=BLUEP;
				if (mXiaomiPaylistener!=null) {
					mXiaomiPaylistener.onGoToPay(selectpaytye);
				}

			}
		});

		iv_greenp2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				selectpaytye=GREENP;
				if (mXiaomiPaylistener!=null) {
					mXiaomiPaylistener.onGoToPay(selectpaytye);
				}
			}
		});
		iv_qqpay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				selectpaytye=XIAOMIPAY;
				if (mXiaomiPaylistener!=null) {
					mXiaomiPaylistener.onGoToPay(selectpaytye);
				}
			}
		});
		
		iv_qbipay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				selectpaytye=XIAOMIPAY;
				if (mXiaomiPaylistener!=null) {
					mXiaomiPaylistener.onGoToPay(selectpaytye);
				}
			}
		});
		iv_shoujipay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				selectpaytye=XIAOMIPAY;
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
	public YingyongbaoListener mXiaomiPaylistener;
	private LinearLayout ll_button;
	private TextView tv_goodsmoney;
	private TextView tv_buttext;
	private int mPrice;
	private TextView tv_goodsname;
	private ImageView iv_qqpay;
	private ImageView iv_qbipay;
	private ImageView iv_greenp2;
	private ImageView iv_shoujipay;
	private ImageView iv_bluepay;
	private TextView mgoodnameView;
	private TextView mgoodmoneyView;
	public void addXiaomiPayListener(YingyongbaoListener mXiaomipay){
		mXiaomiPaylistener=mXiaomipay;
	}

public void setGoodsText(String name){
		
		mgoodnameView.setText(name);

	}
	public void setPrice(int price){
		mPrice=price;
		mgoodmoneyView.setText("￥"+price+".00");
		//tv_buttext.setText("使用微信支付"+price+".00元");
	}
	interface YingyongbaoListener{
		
		public void onGoToPay(int selectpaytype);
	}
	
}
