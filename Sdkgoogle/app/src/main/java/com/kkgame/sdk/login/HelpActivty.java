package com.kkgame.sdk.login;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;

import com.kkgame.sdk.utils.Sensorutils;
import com.kkgame.sdk.utils.ShakeListener;
import com.kkgame.sdk.utils.ShakeListener.OnShakeListener;
import com.kkgame.sdk.xml.DisplayUtils;
import com.kkgame.sdk.xml.GetAssetsutils;
import com.kkgame.utils.Yayalog;


@SuppressLint("NewApi")
public class HelpActivty {

	private WindowManager windowManager = null;

	private LayoutParams windowManagerParams = null;

	private float mTouchX;

	private float mTouchY;

	private float x;

	private float y;

	private FloatView view;

	private int screenWith;

	private Context mContext;
	private Activity mActivity;

	private boolean isShow = false;

	private ShakeListener shakeListener;

	private static class SingletonHolder {
		private static final HelpActivty INSTANCE = new HelpActivty();
	}

	private HelpActivty() {

	}

	public static final HelpActivty getInstance() {
		return SingletonHolder.INSTANCE;
	}

	public HelpActivty setContext(Activity cContext) {
		if (mContext == null) {
			mActivity = cContext;
			mContext = cContext;

			onInit();
		}
		return SingletonHolder.INSTANCE;
	}

	private void onInit() {
		// 摇一摇监听..分别在oncreate和destory中开始监听和关闭监听
		shakeListener = new ShakeListener(mActivity);
		shakeListener.setOnShakeListener(new OnShakeListener() {

			@Override
			public void onShake() {
				// TODO Auto-generated method stub
				onCreate();
				// 用户打开了yywan兔子
				ViewConstants.iscloseyylog = false;
			}
		});
		createWindow();
		view = new FloatView(mContext);
	}

	@SuppressWarnings("deprecation")
	private void createWindow() {
		// 1、获取WindowManager对象
		// windowManager = (WindowManager) mContext.getSystemService("window");
		windowManager = (WindowManager) mContext.getApplicationContext()
				.getSystemService("window");
		// 2、设置LayoutParams(全局变量）相关参数
		windowManagerParams = new LayoutParams();
		// 3、设置相关的窗口布局参数 （悬浮窗口效果）
		windowManagerParams.type = LayoutParams.TYPE_PHONE; // 设置window type
		// windowManagerParams.type = 1000;
		windowManagerParams.format = PixelFormat.RGBA_8888; // 设置图片格式，效果为背景透明
		// 4、设置Window flag == 不影响后面的事件 和 不可聚焦
		windowManagerParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL
				| LayoutParams.FLAG_NOT_FOCUSABLE;
		/*
		 * 注意，flag的值可以为： LayoutParams.FLAG_NOT_TOUCH_MODAL 不影响后面的事件
		 * LayoutParams.FLAG_NOT_FOCUSABLE 不可聚焦 LayoutParams.FLAG_NOT_TOUCHABLE
		 * 不可触摸
		 */
		// 5、 调整悬浮窗口至左上角，便于调整坐标
		windowManagerParams.gravity = Gravity.LEFT | Gravity.TOP;
		// 以屏幕左上角为原点，设置x、y初始值
		windowManagerParams.x = 0;
		windowManagerParams.y = 160;

		// 6、设置悬浮窗口长宽数据
		windowManagerParams.width = machSize(80);
		windowManagerParams.height = machSize(80);

		Display display = windowManager.getDefaultDisplay();
		screenWith = display.getWidth();
	}

	public void onCreate() {
		shakeListener.stop();
		if (isShow) {

			return;
		}
		//view.setX(-100);
		windowManager.addView(view, windowManagerParams); // 显示myFloatView图像
		isShow = true;

	}

	// 是否左边隐藏
	private boolean ishide = false;
	private boolean ishelpshow = false;

	public class FloatView extends ImageView {

		private boolean isMoved = false;
		private float mTempX;
		private float mTempY;
		private long ontouchtime;

		
		public FloatView(Context context) {
			super(context);
			setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
					"yaya_yylogo.png", mActivity));
			// setImageResource(ResourceUtil.getDrawableId(mContext, "yylogo"));
		}

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			// 1、获取到状态栏的高度
			// Rect frame = new Rect();
			// ((Activity)
			// mContext).getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
			// int statusBarHeight = frame.top;
			// System.out.println("状态栏高度" + statusBarHeight);

			// 2、获取相对屏幕的坐标，即以屏幕左上角为原点 。y轴坐标= y（获取到屏幕原点的距离）-状态栏的高度
			x = event.getRawX();
			y = event.getRawY(); // statusBarHeight是系统状态栏的高度

			if (!ishelpshow) {
				new Help_dissmiss_dialog(mActivity).dialogShow();
				ishelpshow = true;
			}

			// 3、处理触摸移动
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN: // 捕获手指触摸按下动作
				// 获取相对View的坐标，即以此View左上角为原点
				mTouchX = event.getX();
				mTouchY = event.getY();
				mTempX = event.getRawX();
				mTempY = event.getRawY();
				// System.out.println(mTouchX+"+++"+mTouchY);
				// mStartX = x;
				// mStartY = y;
				// isMoved = false;
				ontouchtime = System.currentTimeMillis();
				
				this.setX(-machSize(0));
				this.setRotation(0);
				this.setAlpha(225);

				break;

			case MotionEvent.ACTION_MOVE: // 捕获手指触摸移动动作

				// 判断图像当前x坐标是否大于五十..并且隐藏过
				/*
				 * if (mTempX > machSize(40) && ishide) { this.setX(0);
				 * this.setAlpha(255); this.setRotation(0); }
				 */

				// isMoved = true;

				updateViewPosition();
				break;

			case MotionEvent.ACTION_UP: // 捕获手指触摸离开动作

				// 移动距离少于5 ,则视为点击，触发点击的回调

				// 当view小于50的时候让其去角落隐藏
				/*
				 * if (getViewX(this) < machSize(40)) { updateViewPosition1(); }
				 */
				// Math.abs(event.getX() - mTouchX) < 10.0 &&
				// Math.abs(event.getX() - mTouchY) < 10.0
				ishelpshow = false;
				int distance_x = (int) event.getRawX() - (int) mTempX;
				int distance_y = (int) event.getRawY() - (int) mTempY;
				// System.out.println("xxxxxxxxxxxx"+distance_x+"now"+event.getX()+"temp"+mTouchX);
				// System.out.println("yyyyyyyyyyy"+distance_y);

				// if (!isMoved) {

				float left = x - mTouchX;

				if (left <= screenWith / 2) {
					// 图标icon吸附在左边
					x = mTouchX;
				} else {
					// 图标icon吸附在右边
					x = screenWith;
				}

				updateViewPosition();
				
				updateViewPosition1();

				// 移动终点的坐标，重置为0
				mTouchX = mTouchY = 0;

				if (Math.abs(distance_x) <= 20 && Math.abs(distance_y) <= 20) {
					/*
					 * System.out.println("大于三秒哦" + (System.currentTimeMillis()
					 * - ontouchtime));
					 */
					// updateViewPosition();
					if ((System.currentTimeMillis() - ontouchtime) > 1500) {
						//YayaWan.stop(mActivity);
						ViewConstants.iscloseyylog = true;
						new Help_dissmiss_dialog(mActivity).dialogShow();
						Sensorutils.setHelpsensor(ViewConstants.mMainActivity);
					} else {
						onClick(this);
					}

					// YayaWan.stop(mActivity);
					// return false;
				}

				break;
			}
			return false;
		}

		/**
		 * 移动时更新兔子位置
		 */
		private void updateViewPosition() {
			windowManagerParams.x = (int) (x - mTouchX);
			windowManagerParams.y = (int) (y - mTouchY);
			Yayalog.loger("windowManagerParams.x:" + windowManagerParams.x);
			// System.out.println("wp.x=" + windowManagerParams.x + ",wp.y="
			// + windowManagerParams.y);
			// 刷新屏幕显示
			if (isShow) {
				windowManager.updateViewLayout(this, windowManagerParams);
			}

		}

		private void updateViewPosition1() {

			windowManagerParams.x = (int) (0);
			windowManagerParams.y = (int) (y - mTouchY);

			// params.y = (int) (screenHeigh - y - mTouchY);
			// 刷新屏幕显示
			if (isShow) {
				windowManager.updateViewLayout(this, windowManagerParams);
			}
			// wm.updateViewLayout(myview, params);
			this.setX(-machSize(40));
			this.setRotation(50);
			this.setAlpha(100);
			ishide = true;
		}

		private void onClick(View v) {
			int id = v.getId();

			// if (id == ResourceUtil.getId(mContext, "iv_floating_icon")) {
			//YayaWan.stop(mActivity);
			// 打开选择窗口
			//YayaWan.accountManager(mActivity);

		}

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

	public void onDestroy() {
		if (shakeListener != null) {
			shakeListener.start();
		}

		if (mContext == null) {
			return;
		}
		if (!isShow) {
			return;
		}
		windowManager.removeView(view);
		isShow = false;
	}

	/**
	 * 将720像素转成其他像素值
	 * 
	 * @param size
	 * @return
	 */
	private int machSize(int size) {

		int dealWihtSize = DisplayUtils.dealWihtSize(size, mActivity);

		return dealWihtSize;
	}

}
