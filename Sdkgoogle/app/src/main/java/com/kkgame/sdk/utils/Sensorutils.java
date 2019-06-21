package com.kkgame.sdk.utils;



import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.kkgame.sdk.login.ViewConstants;
import com.kkgame.sdkmain.AgentApp;

public class Sensorutils {
	private static SensorManager sensorManager;
	private static MySensorEventListener mySensorEventListener;
	private static Sensor sensor;
	private static Activity mActivity;
	public static void setHelpsensor(Activity mactivity) {
		mActivity=mactivity;
		String service_name = mactivity.SENSOR_SERVICE;
		if (sensorManager == null) {
			sensorManager = (SensorManager) mActivity
					.getSystemService(service_name);
			sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
			mySensorEventListener = new MySensorEventListener();
			sensorManager.registerListener(mySensorEventListener, sensor,
					SensorManager.SENSOR_DELAY_GAME);
		} else {

		}
		
		
		
	}

	public static class MySensorEventListener implements SensorEventListener {

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub

		}

		// 可以得到传感器实时测量出来的变化值
		@Override
		public void onSensorChanged(SensorEvent event) {
			// TODO Auto-generated method stub
			// 方向传感器
			if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
				// x表示手机指向的方位，0表示北,90表示东，180表示南，270表示西

				float y = event.values[1];
				if (Math.abs(y) > 170) {
					if (ViewConstants.mMainActivity != null) {
						if (AgentApp.mUser!=null) {
							//YayaWan.init(ViewConstants.mMainActivity);
							
						}
						
						new Thread(){
							public void run() {
								try {
									sleep(5000);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								Sensorutils.onPause();
							};
						}.start();
						
					}

				}

				// tv_orientation是界面上的一个TextView标签，不再赘述
				 //System.out.println("Orientation:" + y );
			}
		}

	}

	public static void sensorOnResume() {
		if (sensorManager != null&&mActivity!=null) {
			String service_name = mActivity.SENSOR_SERVICE;
			sensorManager = (SensorManager) mActivity
					.getSystemService(service_name);
			sensorManager.registerListener(mySensorEventListener, sensor,
					SensorManager.SENSOR_DELAY_UI);
		}
		
		// super.onResume(); }
	}

	public static void onPause() {
		if (sensorManager != null) {
			sensorManager.unregisterListener(mySensorEventListener);
		}
		sensorManager=null;
		// 注销所有传感器的监听

	}
}
