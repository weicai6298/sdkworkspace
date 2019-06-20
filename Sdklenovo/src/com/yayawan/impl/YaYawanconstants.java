package com.yayawan.impl;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import com.kkgame.sdk.bean.User;
import com.kkgame.sdk.callback.KgameSdkCallback;
import com.kkgame.sdkmain.KgameSdk;
import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Handle;
import com.kkgame.utils.JSONUtil;
import com.lenovo.lsf.gamesdk.GamePayRequest;
import com.lenovo.lsf.gamesdk.IAuthResult;
import com.lenovo.lsf.gamesdk.IPayResult;
import com.lenovo.lsf.gamesdk.LenovoGameApi;
import com.lenovo.lsf.lenovoid.LenovoIDApi;
import com.lidroid.jxutils.HttpUtils;
import com.lidroid.jxutils.exception.HttpException;
import com.lidroid.jxutils.http.RequestParams;
import com.lidroid.jxutils.http.ResponseInfo;
import com.lidroid.jxutils.http.callback.RequestCallBack;
import com.lidroid.jxutils.http.client.HttpRequest.HttpMethod;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.YYWMain;

public class YaYawanconstants {

	//	private static HashMap<String, String> mGoodsid;

	private static Activity mActivity;

	private static boolean isinit = false;

	private static String appid;
	//	private static int isguanggao; //是否有广告
	//	private static String interstitial_id;

	/**
	 * 初始化sdk
	 */
	public static void inintsdk(Activity mactivity) {
		mActivity = mactivity;
		Yayalog.loger("YaYawanconstants初始化sdk");

		//SDK初始化
		appid= ""+DeviceUtil.getGameInfo(mactivity, "lenovo.open.appid");
//		Log.i("tag", "appid="+appid);
		LenovoGameApi.doInit(mactivity,appid);
		isinit = true ;

		//		String guanggao = DeviceUtil.getGameInfo(mActivity, "isguanggao");//是否有广告，1有，0没有
		//		interstitial_id = DeviceUtil.getGameInfo(mActivity, "interstitial_id");
		//		isguanggao = Integer.parseInt(guanggao);
		//		if(isguanggao == 1){
		//			startIntersititial(mactivity,interstitial_id);
		//		}

	}



	/**
	 * application初始化
	 */
	public static void applicationInit(Context applicationContext) {


	}

	/**
	 * 登录
	 */
	public static void login(final Activity mactivity) {
		Yayalog.loger("YaYawanconstantssdk登录");
		if(isinit){
			LenovoGameApi.doAutoLogin(mactivity, new IAuthResult() {

				@Override
				public void onFinished(boolean ret, final String data) {
					// TODO Auto-generated method stub
					Log.i("tag", "ret = "+ret);
					Log.i("tag", "data = "+data);

					if(ret){
						HttpUtils httpUtil = new HttpUtils();
						String url = "https://api.sdk.75757.com/data/get_uid/";
						RequestParams requestParams = new RequestParams();
						requestParams.addBodyParameter("app_id",DeviceUtil.getAppid(mActivity));
						requestParams.addBodyParameter("code", data);
						httpUtil.send(HttpMethod.POST, url, requestParams,
								new RequestCallBack<String>() {

									@Override
									public void onFailure(HttpException arg0, String arg1) {
										// TODO Auto-generated method stub
										Yayalog.loger("请求失败"+arg1.toString());
									}

									@Override
									public void onSuccess(ResponseInfo<String> arg0) {
										// TODO Auto-generated method stub
										try {
											Yayalog.loger("请求成功"+arg0.result);
											JSONObject obj = new JSONObject(arg0.result);
											String uid = obj.getString("uid");
											Yayalog.loger("uid ="+uid);
											loginSuce(mActivity, uid, uid, data);
											Toast("登录成功");
										} catch (JSONException e) {
											e.printStackTrace();
										}
									}
								});
					}else{
						// 登录失败(失败原因开启飞行模式、 网络不通等)
						loginFail();
						Toast("登录失败");
					}
				}
			});
		}else{
			inintsdk(mactivity);
		}
	}

	/**
	 * 支付
	 * 
	 * @param mactivity
	 */
	public static void pay(Activity mactivity, String morderid) {

		Yayalog.loger("YaYawanconstantssdk支付");

		String ext = YYWMain.mOrder.ext;
//		Log.i("tag","ext = " + ext);
		String goods = YYWMain.mOrder.goods;
		int waresid = getWaresid(goods);
//		Log.i("tag","waresid = " + waresid);
		GamePayRequest payRequest = new GamePayRequest();
		// 请填写商品自己的参数
		//		payRequest.addParam("notifyurl", DeviceUtil.getGameInfo(mActivity, "callback"));//当前版本暂时不用，传空String
		//		payRequest.addParam("notifyurl", "");//当前版本暂时不用，传空String
		payRequest.addParam("appid", appid);
		payRequest.addParam("waresid", waresid);
		payRequest.addParam("exorderno", morderid);
		payRequest.addParam("price", Integer.parseInt(YYWMain.mOrder.money+""));
//		Log.i("tag","payRequest="+payRequest);
//		Log.i("tag","appkey="+appkey);
		LenovoGameApi.doPay(mactivity, "", payRequest, new IPayResult() {

			@Override
			public void onPayResult(int resultCode, String signValue,
					String resultInfo) {
				if (LenovoGameApi.PAY_SUCCESS == resultCode) {
					paySuce();
					Toast.makeText(mActivity,"支付成功", Toast.LENGTH_SHORT).show();
				} else if (LenovoGameApi.PAY_CANCEL == resultCode) {
					payFail();
					Toast.makeText(mActivity, "取消支付",Toast.LENGTH_SHORT).show();
					// 取消支付处理，默认采用finish()，请根据需要修改
					//					Log.e(Config.TAG, "return cancel");
				} else {
					payFail();
					Toast.makeText(mActivity, "支付失败",Toast.LENGTH_SHORT).show();
					// 计费失败处理，默认采用finish()，请根据需要修改
					//					Log.e(Config.TAG, "return Error");
				}
			}
		});
	}

	private static int getWaresid(String goods) {
		int waresid = 0 ;
		//决战九天
		//		if(goods.equals("100元宝")){
		//			waresid = 181015;
		//		}else if(goods.equals("300元宝")){
		//			waresid = 181016;
		//		}else if(goods.equals("500元宝")){
		//			waresid = 181017;
		//		}else if(goods.equals("1000元宝")){
		//			waresid = 181018;
		//		}else if(goods.equals("2000元宝")){
		//			waresid = 181019;
		//		}else if(goods.equals("5000元宝")){
		//			waresid = 181020;
		//		}else if(goods.equals("10000元宝")){
		//			waresid = 181021;
		//		}else if(goods.equals("20000元宝")){
		//			waresid = 181022;
		//		}else if(goods.equals("月卡")){
		//			waresid = 181023;
		//		}else if(goods.equals("霸主特权")){
		//			waresid = 181024;
		//		}else if(goods.equals("99元红装礼包")){
		//			waresid = 181025;
		//		}else if(goods.equals("149元红装礼包")){
		//			waresid = 181026;
		//		}else if(goods.equals("289元红装礼包")){
		//			waresid = 181027;
		//		}else if(goods.equals("299元红装礼包")){
		//			waresid = 181028;
		//		}else if(goods.equals("199元红装材料礼包")){
		//			waresid = 181029;
		//		}else if(goods.equals("599元红装材料礼包")){
		//			waresid = 181030;
		//		}else if(goods.equals("589元红装材料礼包")){
		//			waresid = 181031;
		//		}else if(goods.equals("449元红装材料礼包")){
		//			waresid = 181032;
		//		}else if(goods.equals("12元今日特惠礼包")){
		//			waresid = 181033;
		//		}else if(goods.equals("20元今日特惠礼包")){
		//			waresid = 181034;
		//		}else if(goods.equals("32元今日特惠礼包")){
		//			waresid = 181035;
		//		}else if(goods.equals("56元今日特惠礼包")){
		//			waresid = 181036;
		//		}else if(goods.equals("60元今日特惠礼包")){
		//			waresid = 181037;
		//		}else if(goods.equals("86元今日特惠礼包")){
		//			waresid = 181038;
		//		}else if(goods.equals("116元今日特惠礼包")){
		//			waresid = 181039;
		//		}else if(goods.equals("118元今日特惠礼包")){
		//			waresid = 181040;
		//		}else if(goods.equals("128元今日特惠礼包")){
		//			waresid = 181041;
		//		}else if(goods.equals("130元今日特惠礼包")){
		//			waresid = 181042;
		//		}else if(goods.equals("138元今日特惠礼包")){
		//			waresid = 181043;
		//		}else if(goods.equals("226元今日特惠礼包")){
		//			waresid = 181044;
		//		}else if(goods.equals("228元今日特惠礼包")){
		//			waresid = 181045;
		//		}else if(goods.equals("238元今日特惠礼包")){
		//			waresid = 181046;
		//		}else if(goods.equals("258元今日特惠礼包")){
		//			waresid = 181047;
		//		}else if(goods.equals("538元今日特惠礼包")){
		//			waresid = 181048;
		//		}else if(goods.equals("558元今日特惠礼包")){
		//			waresid = 181049;
		//		}else if(goods.equals("1080元今日特惠礼包")){
		//			waresid = 181050;
		//		}else if(goods.equals("2280元今日特惠礼包")){
		//			waresid = 181051;
		//		}else if(goods.equals("108武器时装礼包")){
		//			waresid = 181052;
		//		}else if(goods.equals("148坐骑幻化礼包")){
		//			waresid = 181053;
		//		}
		//无双战纪
		//		if(goods.equals("600元宝")){
		//			waresid = 188945;
		//		}else if(goods.equals("3000元宝")){
		//			waresid = 188946;
		//		}else if(goods.equals("6800元宝")){
		//			waresid = 188947;
		//		}else if(goods.equals("12800元宝")){
		//			waresid = 188948;
		//		}else if(goods.equals("19800元宝")){
		//			waresid = 188949;
		//		}else if(goods.equals("32800元宝")){
		//			waresid = 188950;
		//		}else if(goods.equals("64800元宝")){
		//			waresid = 188951;
		//		}
		//我的便利店
//		if(goods.equals("10钻石")){
//			waresid = 191630;
//		}else if(goods.equals("32钻石")){
//			waresid = 191631;
//		}else if(goods.equals("57钻石")){
//			waresid = 191632;
//		}else if(goods.equals("120钻石")){
//			waresid = 191633;
//		}else if(goods.equals("390钻石")){
//			waresid = 191634;
//		}else if(goods.equals("680钻石")){
//			waresid = 191635;
//		}else if(goods.equals("1450钻石")){
//			waresid = 191636;
//		}
//		else if(goods.equals("首冲390钻石")){
//			waresid = 191637;
//		}
//		else if(goods.equals("每日钻石套餐")){
//			waresid = 191638;
//		}
//		else if(goods.equals("新手套餐")){
//			waresid = 191639;
//		}
//		else if(goods.equals("实惠套餐")){
//			waresid = 191640;
//		}
//		else if(goods.equals("高级套餐")){
//			waresid = 191641;
//		}
//		else if(goods.equals("每日特惠周一")){
//			waresid = 191642;
//		}
//		else if(goods.equals("每日特惠周二")){
//			waresid = 191643;
//		}
//		else if(goods.equals("每日特惠周三")){
//			waresid = 191644;
//		}
//		else if(goods.equals("每日特惠周四")){
//			waresid = 191645;
//		}
//		else if(goods.equals("每日特惠周五")){
//			waresid = 191646;
//		}
//		else if(goods.equals("每日特惠周六")){
//			waresid = 191647;
//		}
//		else if(goods.equals("每日特惠周日")){
//			waresid = 191648;
//		}
//		else if(goods.equals("首充礼包")){
//			waresid = 191649;
//		}
//		else if(goods.equals("新手礼包")){
//			waresid = 209979;
//		}
//		else if(goods.equals("加油礼包")){
//			waresid = 209980;
//		}
//		else if(goods.equals("进取礼包")){
//			waresid = 209981;
//		}
//		else if(goods.equals("超值礼包")){
//			waresid = 209982;
//		}
		//萌斗魏蜀吴
		//		if(goods.equals("60元宝")){
		//			waresid = 198672;
		//		}else if(goods.equals("280元宝")){
		//			waresid = 198673;
		//		}else if(goods.equals("680元宝")){
		//			waresid = 198674;
		//		}else if(goods.equals("1280元宝")){
		//			waresid = 198675;
		//		}else if(goods.equals("3280元宝")){
		//			waresid = 198676;
		//		}else if(goods.equals("6480元宝")){
		//			waresid = 198677;
		//		}else if(goods.equals("9980元宝")){
		//			waresid = 198678;
		//		}
		//		else if(goods.equals("20480元宝")){
		//			waresid = 198679;
		//		}
		//		else if(goods.equals("周卡")){
		//			waresid = 198680;
		//		}
		//		else if(goods.equals("月卡")){
		//			waresid = 198681;
		//		}
		//		else if(goods.equals("至尊卡")){
		//			waresid = 198682;
		//		}
		//		else if(goods.equals("一元礼包")){
		//			waresid = 198683;
		//		}		

		//不朽之城
		//		if(goods.equals("投资计划(7705001)")){
		//			waresid = 214079;
		//		}else if(goods.equals("雨师首充(7702001)")){
		//			waresid = 214080;
		//		}else if(goods.equals("体力礼包(7704001)")){
		//			waresid = 214081;
		//		}else if(goods.equals("抽卡劵(7704002)")){
		//			waresid = 214082;
		//		}else if(goods.equals("法宝进阶材料(7704003)")){
		//			waresid = 214083;
		//		}else if(goods.equals("法宝进阶材料(7704004)")){
		//			waresid = 214084;
		//		}else if(goods.equals("伙伴礼包(7704005)")){
		//			waresid = 214085;
		//		}
		//		else if(goods.equals("伙伴礼包(7704006)")){
		//			waresid = 214086;
		//		}
		//		else if(goods.equals("灵珠礼包(7704007)")){
		//			waresid = 214087;
		//		}
		//		else if(goods.equals("灵珠礼包(7704008)")){
		//			waresid = 214088;
		//		}
		//		else if(goods.equals("灵珠礼包(7704009)")){
		//			waresid = 214089;
		//		}
		//		else if(goods.equals("灵珠礼包(7704010)")){
		//			waresid = 214090;
		//		}		
		//		else if(goods.equals("VIP经验礼包(7704011)")){
		//			waresid = 214091;
		//		}		
		//		else if(goods.equals("VIP经验礼包(7704012)")){
		//			waresid = 214092;
		//		}		
		//		else if(goods.equals("声望礼包(7704013)")){
		//			waresid = 214093;
		//		}		
		//		else if(goods.equals("充值6元档(1001)")){
		//			waresid = 214094;
		//		}		
		//		else if(goods.equals("充值30元档(1002)")){
		//			waresid = 214095;
		//		}		
		//		else if(goods.equals("充值68元档(1003)")){
		//			waresid = 214096;
		//		}		
		//		else if(goods.equals("充值128元档(1004)")){
		//			waresid = 214097;
		//		}		
		//		else if(goods.equals("充值198元档(1005)")){
		//			waresid = 214098;
		//		}		
		//		else if(goods.equals("充值648元档(1006)")){
		//			waresid = 214099;
		//		}		
		//		else if(goods.equals("充值6元档(2001)")){
		//			waresid = 214100;
		//		}		
		//		else if(goods.equals("充值30元档(2002)")){
		//			waresid = 214101;
		//		}		
		//		else if(goods.equals("充值68元档(2003)")){
		//			waresid = 214102;
		//		}		
		//		else if(goods.equals("充值128元档(2004)")){
		//			waresid = 214103;
		//		}		
		//		else if(goods.equals("充值198元档(2005)")){
		//			waresid = 214104;
		//		}		
		//		else if(goods.equals("充值648元档(2006)")){
		//			waresid = 214105;
		//		}		
		//		else if(goods.equals("首充6元(7706001)")){
		//			waresid = 214106;
		//		}		
		//		else if(goods.equals("清凉夏日男(7706002)")){
		//			waresid = 214107;
		//		}		
		//		else if(goods.equals("清凉夏日女(7706003)")){
		//			waresid = 214108;
		//		}		
		//		else if(goods.equals("大话西游男(7706004)")){
		//			waresid = 214109;
		//		}		
		//		else if(goods.equals("大话西游女(7706005)")){
		//			waresid = 214110;
		//		}		
		//		else if(goods.equals("酒神舆(7706006)")){
		//			waresid = 214111;
		//		}		
		//		else if(goods.equals("仙鹤(7706007)")){
		//			waresid = 214112;
		//		}		
		//		else if(goods.equals("清泉莲台(7706008)")){
		//			waresid = 214113;
		//		}		
		//		else if(goods.equals("斗神祥云(7706009)")){
		//			waresid = 214114;
		//		}		
		//		else if(goods.equals("蚩尤魔云(7706010)")){
		//			waresid = 214115;
		//		}		
		//		else if(goods.equals("神仙笔(7706011)")){
		//			waresid = 214116;
		//		}		
		//		else if(goods.equals("1元礼包(7706012)")){
		//			waresid = 214117;
		//		}		
		//		else if(goods.equals("体力礼包(7706013)")){
		//			waresid = 214118;
		//		}		
		//		else if(goods.equals("灵珠礼包(7706014)")){
		//			waresid = 214119;
		//		}		
		//		else if(goods.equals("灵珠礼包(7706015)")){
		//			waresid = 214120;
		//		}		
		//		else if(goods.equals("声望礼包(7706016)")){
		//			waresid = 214121;
		//		}		
		//		else if(goods.equals("神器礼包(7706017)")){
		//			waresid = 214122;
		//		}		
		//		else if(goods.equals("神器礼包(7706018)")){
		//			waresid = 214123;
		//		}		
		//		else if(goods.equals("伙伴礼包(7706019)")){
		//			waresid = 214124;
		//		}		
		//		else if(goods.equals("周卡(1)")){
		//			waresid = 214125;
		//		}		
		//		else if(goods.equals("月卡(2)")){
		//			waresid = 214126;
		//		}		
		//		else if(goods.equals("半年卡(3)")){
		//			waresid = 214127;
		//		}		
		//		else if(goods.equals("龙吟剑(7706020)")){
		//			waresid = 214128;
		//		}		
		//		else if(goods.equals("伙伴礼包(7706027)")){
		//			waresid = 214129;
		//		}		
		//		else if(goods.equals("体力礼包(7706021)")){
		//			waresid = 214130;
		//		}		
		//		else if(goods.equals("灵珠礼包(7706022)")){
		//			waresid = 214131;
		//		}		
		//		else if(goods.equals("灵珠礼包(7706023)")){
		//			waresid = 214132;
		//		}		
		//		else if(goods.equals("声望礼包(7706024)")){
		//			waresid = 214133;
		//		}		
		//		else if(goods.equals("神器礼包(7706025)")){
		//			waresid = 214134;
		//		}		
		//		else if(goods.equals("神器礼包(7706026)")){
		//			waresid = 214135;
		//		}		
		//		else if(goods.equals("V7出身(7706028)")){
		//			waresid = 214136;
		//		}		
		//		else if(goods.equals("V3出身(7706029)")){
		//			waresid = 214137;
		//		}		
		//		else if(goods.equals("山河社稷图(7706030)")){
		//			waresid = 214138;
		//		}		
		//		else if(goods.equals("新周卡")){
		//			waresid = 217987;
		//		}		
		//		else if(goods.equals("新月卡")){
		//			waresid = 217988;
		//		}		
		//		else if(goods.equals("新季卡")){
		//			waresid = 217989;
		//		}		
		//		else if(goods.equals("普通声望礼包")){
		//			waresid = 217990;
		//		}		
		//		else if(goods.equals("精英声望礼包")){
		//			waresid = 217991;
		//		}		
		//		else if(goods.equals("豪华声望礼包")){
		//			waresid = 217992;
		//		}		
		//		else if(goods.equals("普通法宝礼包")){
		//			waresid = 217993;
		//		}		
		//		else if(goods.equals("精英法宝礼包")){
		//			waresid = 217994;
		//		}		
		//		else if(goods.equals("豪华法宝礼包")){
		//			waresid = 217995;
		//		}		
		//		else if(goods.equals("至尊法宝礼包")){
		//			waresid = 217996;
		//		}		
		//		else if(goods.equals("普通体力礼包")){
		//			waresid = 217997;
		//		}		
		//		else if(goods.equals("精英体力礼包")){
		//			waresid = 217998;
		//		}		
		//		else if(goods.equals("豪华抽卡礼包")){
		//			waresid = 217999;
		//		}		
		//		else if(goods.equals("至尊抽卡礼包")){
		//			waresid = 218000;
		//		}		
		//		else if(goods.equals("普通神器礼包")){
		//			waresid = 218001;
		//		}		
		//		else if(goods.equals("精英神器礼包")){
		//			waresid = 218002;
		//		}		
		//		else if(goods.equals("豪华神器礼包")){
		//			waresid = 218003;
		//		}		
		//		else if(goods.equals("豪华缘分礼包")){
		//			waresid = 218004;
		//		}		
		//		else if(goods.equals("至尊缘分礼包")){
		//			waresid = 218005;
		//		}		
		//		
		//		else if(goods.equals("超值升级礼包")){
		//			waresid = 220452;
		//		}		
		//		else if(goods.equals("超值体力礼包")){
		//			waresid = 220453;
		//		}		
		//		else if(goods.equals("超值进阶礼包")){
		//			waresid = 220454;
		//		}		
		//		else if(goods.equals("超值材料礼包")){
		//			waresid = 220455;
		//		}		
		//		else if(goods.equals("特惠升级礼包")){
		//			waresid = 220456;
		//		}		
		//		else if(goods.equals("特惠体力礼包")){
		//			waresid = 220457;
		//		}		
		//		else if(goods.equals("特惠进阶礼包")){
		//			waresid = 220458;
		//		}		
		//		else if(goods.equals("特惠材料礼包")){
		//			waresid = 220459;
		//		}		

		//雄霸武神
		//		if(goods.equals("100元宝")){
		//			waresid = 220069;
		//		}else if(goods.equals("200元宝")){
		//			waresid = 220070;
		//		}else if(goods.equals("500元宝")){
		//			waresid = 220071;
		//		}else if(goods.equals("1000元宝")){
		//			waresid = 220072;
		//		}else if(goods.equals("2000元宝")){
		//			waresid = 220073;
		//		}else if(goods.equals("5000元宝")){
		//			waresid = 220074;
		//		}else if(goods.equals("10000元宝")){
		//			waresid = 220075;
		//		}else if(goods.equals("25000元宝")){
		//			waresid = 220076;
		//		}else if(goods.equals("魂器礼包")){
		//			waresid = 220077;
		//		}


		//超能特战队
//						if(goods.equals("红宝石120")){
//							waresid = 228247;
//						}else if(goods.equals("红宝石600")){
//							waresid = 228248;
//						}else if(goods.equals("红宝石1500")){
//							waresid = 228249;
//						}else if(goods.equals("红宝石2820")){
//							waresid = 228250;
//						}else if(goods.equals("红宝石4560")){
//							waresid = 228251;
//						}else if(goods.equals("红宝石7550")){
//							waresid = 228252;
//						}else if(goods.equals("红宝石15560")){
//							waresid = 228253;
//						}else if(goods.equals("首充礼包")){
//							waresid = 228254;
//						}else if(goods.equals("月卡商品30天")){
//							waresid = 228255;
//						}else if(goods.equals("关卡礼包")){
//							waresid = 228256;
//						}else if(goods.equals("超值礼包")){
//							waresid = 228257;
//						}else if(goods.equals("高级成长礼包")){
//							waresid = 228258;
//						}else if(goods.equals("高级道具礼包")){
//							waresid = 228259;
//						}else if(goods.equals("高速成长礼包")){
//							waresid = 228260;
//						}else if(goods.equals("灵魂转生券")){
//							waresid = 228261;
//						}else if(goods.equals("活动礼包1")){
//							waresid = 228262;
//						}else if(goods.equals("活动礼包2")){
//							waresid = 228263;
//						}else if(goods.equals("活动礼包3")){
//							waresid = 228264;
//						}else if(goods.equals("活动礼包4")){
//							waresid = 228265;
//						}else if(goods.equals("活动礼包5")){
//							waresid = 228266;
//						}else if(goods.equals("周卡礼包")){
//							waresid = 257382;
//						}
		
		//黑暗使者
		if(goods.equals("60钻石")){
			waresid = 249955;
		}else if(goods.equals("300钻石")){
			waresid = 249956;
		}else if(goods.equals("600钻石")){
			waresid = 249957;
		}else if(goods.equals("980钻石")){
			waresid = 249958;
		}else if(goods.equals("1980钻石")){
			waresid = 249959;
		}else if(goods.equals("3280钻石")){
			waresid = 249960;
		}else if(goods.equals("6480钻石")){
			waresid = 249961;
		}
		else if(goods.equals("成长礼包")){
			waresid = 249962;
		}
		else if(goods.equals("成长礼包1")){
			waresid = 249963;
		}
		else if(goods.equals("成长礼包2")){
			waresid = 249964;
		}
		else if(goods.equals("成长礼包3")){
			waresid = 249965;
		}
		else if(goods.equals("成长礼包4")){
			waresid = 249966;
		}
		else if(goods.equals("成长礼包5")){
			waresid = 249967;
		}
		else if(goods.equals("礼包商品18元礼包")){
			waresid = 249968;
		}
		else if(goods.equals("礼包商品50元礼包")){
			waresid = 249969;
		}
		else if(goods.equals("礼包商品108元礼包")){
			waresid = 249970;
		}
		else if(goods.equals("礼包商品188元礼包")){
			waresid = 249971;
		}
		else if(goods.equals("30天礼包")){
			waresid = 249972;
		}
		else if(goods.equals("新手礼包")){
			waresid = 249973;
		}
		else if(goods.equals("蜕变礼包")){
			waresid = 249974;
		}
		else if(goods.equals("进阶礼包")){
			waresid = 249975;
		}
		else if(goods.equals("648元礼包")){
			waresid = 249976;
		}
		else if(goods.equals("庆典礼包")){
			waresid = 249977;
		}
		else if(goods.equals("全能灵魂石礼包")){
			waresid = 249978;
		}
		else if(goods.equals("抽宝礼包")){
			waresid = 249979;
		}
		else if(goods.equals("30天礼包2")){
			waresid = 249980;
		}
		else if(goods.equals("超值礼包28元礼包")){
			waresid = 249990;
		}
		else if(goods.equals("超值礼包60元礼包")){
			waresid = 249991;
		}
		else if(goods.equals("超值礼包98元礼包")){
			waresid = 249992;
		}
		else if(goods.equals("超值礼包198元礼包")){
			waresid = 249993;
		}
		else if(goods.equals("超值礼包128元礼包")){
			waresid = 249994;
		}
		else if(goods.equals("超值礼包328元礼包")){
			waresid = 249995;
		}
		else if(goods.equals("新手礼包")){
			waresid = 249996;
		}
		else if(goods.equals("蜕变礼包")){
			waresid = 249997;
		}
		else if(goods.equals("进阶礼包")){
			waresid = 249998;
		}
		else if(goods.equals("首充礼包")){
			waresid = 249999;
		}
		else if(goods.equals("豪华礼包")){
			waresid = 250000;
		}
		
		//来咬我呀
//		if(goods.equals("60钻石")){
//			waresid = 261034;
//		}else if(goods.equals("280钻石")){
//			waresid = 261035;
//		}else if(goods.equals("680钻石")){
//			waresid = 261036;
//		}else if(goods.equals("1280钻石")){
//			waresid = 261037;
//		}else if(goods.equals("3280钻石")){
//			waresid = 261038;
//		}else if(goods.equals("6480钻石")){
//			waresid = 261039;
//		}else if(goods.equals("月卡")){
//			waresid = 261941;
//		}else if(goods.equals("季卡")){
//			waresid = 261942;
//		}else if(goods.equals("许愿池1")){
//			waresid = 261970;
//		}else if(goods.equals("许愿池2")){
//			waresid = 261971;
//		}else if(goods.equals("许愿池3")){
//			waresid = 261972;
//		}else if(goods.equals("许愿池4")){
//			waresid = 261973;
//		}else if(goods.equals("许愿池5")){
//			waresid = 261974;
//		}else if(goods.equals("许愿池6")){
//			waresid = 261975;
//		}else if(goods.equals("许愿池7")){
//			waresid = 261976;
//		}
		return waresid;
	}

	/**
	 * 退出
	 * 
	 * @param paramActivity
	 * @param callback
	 */
	public static void exit(final Activity paramActivity,
			final YYWExitCallback callback) {
		Yayalog.loger("YaYawanconstantssdk退出");
		paramActivity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				LenovoGameApi.doQuit(paramActivity, new IAuthResult() {

					@Override
					public void onFinished(boolean ret, String data) {
						if(ret){
							//					callback.onExit();
							mActivity.finish();
							System.exit(0);
						}else{
							//					Toast("继续游戏");
						}
					}
				});
			}
		});
	}

	/**
	 * 设置角色信息
	 * 
	 */
	public static void setData(Activity paramActivity, String roleId, String roleName,String roleLevel, String zoneId, String zoneName, String roleCTime,String ext){
		Yayalog.loger("YaYawanconstants设置角色信息");
	}

	public static void onResume(Activity paramActivity) {

	}

	public static void onPause(Activity paramActivity) {

	}

	public static void onDestroy(Activity paramActivity) {
		//		institial.destroyIntersititial();
	}

	public static void onActivityResult(Activity paramActivity) {

	}

	public static void onNewIntent(Intent paramIntent) {

	}

	public static void onStart(Activity paramActivity) {

	}

	public static void onRestart(Activity paramActivity) {

	}

	public static void onCreate(Activity paramActivity) {

	}

	public static void onStop(Activity paramActivity) {

	}

	/**
	 * 登录成功调用
	 * 
	 * @param mactivity
	 * @param uid
	 *            唯一id
	 * @param username
	 *            用户名..如果用户名为空.则拿uid作为用户名
	 * @param session
	 *            token验证码
	 */
	public static void loginSuce(Activity mactivity, String uid,
			String username, String session) {

		YYWMain.mUser = new YYWUser();

		YYWMain.mUser.uid = DeviceUtil.getGameId(mactivity) + "-" + uid + "";
		;
		if (username != null) {
			YYWMain.mUser.userName = DeviceUtil.getGameId(mactivity) + "-"
					+ username + "";
		} else {
			YYWMain.mUser.userName = DeviceUtil.getGameId(mactivity) + "-"
					+ uid + "";
		}

		// YYWMain.mUser.nick = data.getNickName();
		YYWMain.mUser.token = JSONUtil.formatToken(mactivity, session,
				YYWMain.mUser.userName);

		if (YYWMain.mUserCallBack != null) {
			YYWMain.mUserCallBack.onLoginSuccess(YYWMain.mUser, "success");
			Handle.login_handler(mactivity, YYWMain.mUser.uid,
					YYWMain.mUser.userName);
		}
	}


	/**
	 * 登出
	 */
	public static void loginOut() {
		if (YYWMain.mUserCallBack != null) {
			YYWMain.mUserCallBack.onLogout(null);

		}
	}
	/**
	 * 登录失败
	 */
	public static void loginFail() {
		if (YYWMain.mUserCallBack != null) {
			YYWMain.mUserCallBack.onLoginFailed(null, null);
		}
	}

	/**
	 * 支付成功
	 * 
	 */
	public static void paySuce() {
		if (YYWMain.mPayCallBack != null) {
			YYWMain.mPayCallBack.onPaySuccess(YYWMain.mUser, YYWMain.mOrder,
					"success");
		}
	}

	/**
	 * 支付失败
	 * 
	 */
	public static void payFail() {
		if (YYWMain.mPayCallBack != null) {
			YYWMain.mPayCallBack.onPayFailed(null, null);
		}
	}

	/*
	 * Toast提示
	 */
	public static void Toast(final String msg){
		//		mActivity.runOnUiThread(new Runnable() {
		//
		//			@Override
		//			public void run() {
		Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();	
		//			}
		//		});
	}


	//	private static void startIntersititial(final Activity mactivity,String placementID_Interstitial) {
	//		
	//		institial = new Interstitial(mactivity, placementID_Interstitial, new InterstitialListener() {
	//
	//			@Override
	//			public void onInterstitialShowSuccess(String msg) {
	//				Toast.makeText(mactivity, "插屏展示成功", Toast.LENGTH_SHORT).show();
	//			}
	//
	//			@Override
	//			public void onInterstitialRequestFailed(String msg) {
	//				Toast.makeText(mactivity, "插屏请求失败 "+msg, Toast.LENGTH_SHORT).show();
	//			}
	//
	//			@Override
	//			public void onInterstitialDismiss() {
	//				Toast.makeText(mactivity, "插屏关闭成功", Toast.LENGTH_SHORT).show();
	//			}
	//		});
	//	}

}
