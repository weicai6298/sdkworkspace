package com.kkgame.sdk.utils;

import java.lang.reflect.Field;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.kkgame.sdk.login.Help_dissmiss_dialog;
import com.kkgame.sdk.utils.ShakeListener.OnShakeListener;
import com.kkgame.sdk.xml.DisplayUtils;
import com.kkgame.sdk.xml.GetAssetsutils;
import com.kkgame.sdkmain.KgameSdk;
import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Yayalog;

/**
 * 
 * @author zjf
 * 
 */
public class LogoWindow {

	private static WindowManager wm;
	private static WindowManager.LayoutParams params;
	Context con;
	public boolean isadd = false;

	static Activity mactivity;
	private int screenHeigh;

	private static LogoWindow mLogowindow;

	public static LogoWindow getInstants(Activity mactivity) {
		if (mLogowindow == null) {
			Yayalog.loger("重新new了mlogowindow");

			mLogowindow = new LogoWindow(mactivity);

		} else {
			Yayalog.loger("mlo不为空");
		}
		// mLogowindow.mactivity
		return mLogowindow;

	}

	private LogoWindow(Activity co) {

		mactivity = co;

		createView();

	}

	private static Handler mhandler = new Handler() {
		@Override
		public void dispatchMessage(Message msg) {
			// TODO Auto-generated method stub
			super.dispatchMessage(msg);
			switch (msg.what) {
			case 1:
				if (myview != null && params.x == 0) {
					myview.setImageBitmap(GetAssetsutils
							.getImageFromAssetsFile("yaya_yylogotouming.png",
									mactivity));
				}
			case 521:
				// createView();
				Yayalog.loger("我在接到了消息：hasWindowFocus"
						+ mactivity.hasWindowFocus() + "hasview:" + hasview);
				if (mactivity.hasWindowFocus() && !hasview) {
					addView();
					Yayalog.loger("mactivity.hasWindowFocus()+添加了view");
				} else {
					Yayalog.loger("我在发消息");
					if (hasview) {

					} else {
						mhandler.sendEmptyMessageDelayed(521, 500);
					}

				}

				break;

			default:
				break;
			}
		}
	};
	private boolean ishelpshow = false;

	private ShakeListener shakeListener;
	private static ImageView myview;

	private void createView() {

		Yayalog.loger("兔子oncreate");
		DisplayMetrics dm = new DisplayMetrics();

		mactivity.getWindowManager().getDefaultDisplay().getMetrics(dm);

		screenHeigh = dm.heightPixels;
		wm = ((WindowManager) mactivity.getSystemService("window"));
		if (myview == null) {

			myview = new ImageView(mactivity);
			// 创建时设置view的正常参数
			// myview.setX(-machSize(0));
			// myview.setRotation(0);
			// myview.setAlpha(225);
			myview.setLayoutParams(new LinearLayout.LayoutParams(machSize(100),
					machSize(100)));

			myview.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
					"yaya_yylogo.png", mactivity));

			myview.setOnTouchListener(new OnTouchListener() {

				float x;
				float y;

				private float mTouchX;
				private float mTouchY;

				private float mTempX;
				private float mTempY;
				private float mdownTempX;
				private float mdownTempY;
				private long ontouchtime;

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					x = event.getRawX();
					y = event.getRawY(); // statusBarHeight是系统状态栏的高度

					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN: // 捕获手指触摸按下动作
						// 获取相对View的坐标，即以此View左上角为原点
						mTouchX = event.getX();
						mTouchY = event.getY();

						mdownTempX = event.getRawX();
						mdownTempY = event.getRawY();

						ontouchtime = System.currentTimeMillis();
						// wm.removeView(myview);

						// myview.setX(0);
						// myview.setRotation(0);
						// myview.setAlpha(225);
						// updateViewPosition();
						// wm.addView(myview, params);
						// Yayalog.loger("我的图像的x"+myview.getX());

						myview.setImageBitmap(GetAssetsutils
								.getImageFromAssetsFile("yaya_yylogo.png",
										mactivity));
						break;

					case MotionEvent.ACTION_MOVE: // 捕获手指触摸移动动作

						updateViewPosition();

						int distance_x = (int) event.getRawX()
								- (int) mdownTempX;
						int distance_y = (int) event.getRawY()
								- (int) mdownTempY;

						if (Math.abs(distance_x) > 40
								&& Math.abs(distance_y) > 40) {

							if (!ishelpshow) {

								new Help_dissmiss_dialog(mactivity)
										.dialogShow();

								ishelpshow = true;

							}

							// YayaWan.stop(mActivity);
							// return false;
						}
						break;

					case MotionEvent.ACTION_UP: // 捕获手指触摸离开动作

						distance_x = (int) event.getRawX() - (int) mdownTempX;
						distance_y = (int) event.getRawY() - (int) mdownTempY;
						if (Math.abs(distance_x) <= 20
								&& Math.abs(distance_y) <= 20) {

							if ((System.currentTimeMillis() - ontouchtime) > 1500) {
								// YayaWan.stop(mactivity);
								// 这里关闭了永久隐藏

							} else {
								onClick();
							}

							// YayaWan.stop(mActivity);
							// return false;
						} else {
							if (event.getRawX() < machSize(150)) {
								updateViewPosition1();
							}
							// updateViewPosition1();
						}
						ishelpshow = false;
						break;
					}
					return true;
				}

				private void updateViewPosition() {

					params.x = (int) (x - mTouchX);

					params.y = (int) (screenHeigh - y - mTouchY);

					wm.updateViewLayout(myview, params);

				}

				private void updateViewPosition1() {

					params.x = -30;

					params.y = (int) (screenHeigh - y - mTouchY);

					// myview.setX(-machSize(30));
					// myview.setRotation(50);
					// myview.setAlpha(100);

					wm.updateViewLayout(myview, params);
					myview.setImageBitmap(GetAssetsutils
							.getImageFromAssetsFile("yaya_yylogotouming.png",
									mactivity));
					// myview.startAnimation(alphaAnimation);

				}

			});

		}

		params = new WindowManager.LayoutParams();
		params.format = PixelFormat.RGBA_8888;
		params.type = 1000;
		params.flags = 40;
		// params.flags |= 262144;
		// params.flags |= 512;
		params.gravity = 83;
		params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
		params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
		params.alpha = (float) 1;

		params.x = 0;
		if (DeviceUtil.getGameInfo(mactivity, "yayawan_orientation").equals(
				"landscape")) {
			params.y = machSize(360);
		} else {
			params.y = machSize(600);
		}

		// 摇一摇监听..分别在oncreate和destory中开始监听和关闭监听
		shakeListener = new ShakeListener(mactivity);
		shakeListener.setOnShakeListener(new OnShakeListener() {

			@Override
			public void onShake() {
				// TODO Auto-generated method stub
				Yayalog.loger("再要");
				// YayaWan.init(mactivity);
			}
		});

	}

	// 添加
	private static void addView() {

		wm.addView(myview, params);
		hasview = true;
	}

	private void onClick() {
		// TODO Auto-generated method stub
		// if (id == ResourceUtil.getId(mContext, "iv_floating_icon")) {
		// YayaWan.stop(mactivity);
		// 打开选择窗口
		 KgameSdk.accountManager(mactivity);
	}

	public void start() {
		// 停止摇一摇监听
		Yayalog.loger("开始监听暂停");
		shakeListener.stop();
		// System.out.println("1");
		
		
		mhandler.sendEmptyMessageAtTime(521, 1500);

	}

	public static boolean hasview = false;

	public void Stop() {
		// 开始摇一摇监听
		shakeListener.start();
		Yayalog.loger("暂停监听开始");
		if (hasview) {

			Yayalog.loger("暂停监听开始mLogowindow = null");

			wm.removeView(myview);
			mLogowindow = null;
			hasview = false;

		}
	}

	/**
	 * 将720像素转成其他像素值
	 * 
	 * @param size
	 * @return
	 */
	private int machSize(int size) {

		int dealWihtSize = DisplayUtils.dealWihtSize(size, mactivity);

		return dealWihtSize;
	}

	/**
	 * 获取某个view的x位置
	 * 
	 * @param view
	 * @return
	 */
	public int getViewX(View view) {
		int[] locations = new int[2];
		view.getLocationOnScreen(locations);
		int kx = locations[0];// 获取组件当前位置的横坐标
		return kx;
	}

	private int getStatusBarHeight(Activity macActivity) {
		Class<?> c = null;
		Object obj = null;
		Field field = null;
		int x = 0;
		try {
			c = Class.forName("com.android.internal.R$dimen");
			obj = c.newInstance();
			field = c.getField("status_bar_height");
			x = Integer.parseInt(field.get(obj).toString());
			int dimensionPixelSize = macActivity.getResources()
					.getDimensionPixelSize(x);
			System.out.println("状态栏高度" + dimensionPixelSize);
			return dimensionPixelSize;
		} catch (Exception e1) {
			// Log.d(TAG, "get status bar height fail");
			// e1.printStackTrace();
			System.out.println("获取状态栏高度失败");
			return 75;
		}
	}
}
