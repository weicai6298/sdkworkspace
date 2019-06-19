package com.kkgame.sdk.pay;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.kkgame.sdk.pay.XiaomiPayxml.XiaomiPayListener;
import com.kkgame.sdk.pay.YingYongBaoPayxml.YingyongbaoListener;
import com.kkgame.sdkmain.AgentApp;
import com.kkgame.sdkmain.KgameSdk;
import com.kkgame.utils.DeviceUtil;

public class XiaoMipayActivity extends Activity {

	XiaomiPayxml xiaomiPayxml;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		System.out.println(DeviceUtil.getUnionid(this));
		if (DeviceUtil.getUnionid(this).equals("2958292331")) {
			StartYingBaoPay();
		}else{
		
		 xiaomiPayxml = new XiaomiPayxml(this);
		//View initViewxml = new XiaomiPayxml(this).initViewxml();
		setContentView(xiaomiPayxml.initViewxml());
		xiaomiPayxml.setPrice(Integer.parseInt(AgentApp.mPayOrder.money/100+""));
		
		String gamename=DeviceUtil.getGameInfo(getApplicationContext(), "gamename");
		String moneyname=DeviceUtil.getGameInfo(getApplicationContext(), "moneyname");
		xiaomiPayxml.setGoodsText(gamename+"-"+moneyname);
		xiaomiPayxml.addXiaomiPayListener(new XiaomiPayListener() {
			
			@Override
			public void onGoToPay(int selectpaytype) {
				// TODO Auto-generated method stub
				System.out.println(selectpaytype);
				if (selectpaytype==xiaomiPayxml.BLUEP) {
					GreenblueP greenbluePay = new GreenblueP(XiaoMipayActivity.this, AgentApp.mPayOrder,GreenblueP.BLUEP , KgameSdk.mPaymentCallback);
					greenbluePay.greenP();
				}else if(selectpaytype==xiaomiPayxml.GREENP){
					GreenblueP greenbluePay = new GreenblueP(XiaoMipayActivity.this, AgentApp.mPayOrder,GreenblueP.GREENP , KgameSdk.mPaymentCallback);
					greenbluePay.greenP();
				}else {
					//选择了小米支付，就把界面关闭，以后都不会打开了
					GreenblueP.isselectxiaomipay=true;
					Toast.makeText(XiaoMipayActivity.this, "小米钱包初始化完毕，请重新点击商品", Toast.LENGTH_LONG).show();
					finish();
				}
				
				
				}
			});
		
		}
	}
	
	YingYongBaoPayxml yingyongbaoPayxml ;
	private void StartYingBaoPay() {
		System.out.println("StartYingBaoPay");
		// TODO Auto-generated method stub
		 yingyongbaoPayxml = new YingYongBaoPayxml(this);
			//View initViewxml = new XiaomiPayxml(this).initViewxml();
		setContentView(yingyongbaoPayxml.initViewxml());
		
		
		yingyongbaoPayxml.setPrice(Integer.parseInt(AgentApp.mPayOrder.money/100+""));
		
	//	String gamename=DeviceUtil.getGameInfo(getApplicationContext(), "gamename");
		String moneyname=DeviceUtil.getGameInfo(getApplicationContext(), "moneyname");
		//yingyongbaoPayxml.setGoodsText(gamename+"-"+moneyname);
		yingyongbaoPayxml.addXiaomiPayListener(new YingyongbaoListener() {
			
			@Override
			public void onGoToPay(int selectpaytype) {
				// TODO Auto-generated method stub
				System.out.println(selectpaytype);
				if (selectpaytype==yingyongbaoPayxml.BLUEP) {
					GreenblueP greenbluePay = new GreenblueP(XiaoMipayActivity.this, AgentApp.mPayOrder,GreenblueP.BLUEP , KgameSdk.mPaymentCallback);
					greenbluePay.greenP();
				}else if(selectpaytype==yingyongbaoPayxml.GREENP){
					GreenblueP greenbluePay = new GreenblueP(XiaoMipayActivity.this, AgentApp.mPayOrder,GreenblueP.GREENP , KgameSdk.mPaymentCallback);
					greenbluePay.greenP();
				}else {
					//选择了小米支付，就把界面关闭，以后都不会打开了
					GreenblueP.isselectxiaomipay=true;
					Toast.makeText(XiaoMipayActivity.this, "支付更新完毕，请重新点击商品", Toast.LENGTH_LONG).show();
					finish();
				}
				
				
				}
			});
	}
	
	
}
