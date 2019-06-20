package com.kkgame.sdk.smallhelp;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.PagerAdapter;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.kkgame.sdk.utils.Basedialogview;
import com.kkgame.sdk.utils.ExtendedViewPager;
import com.kkgame.sdk.utils.TouchImageView;
import com.kkgame.sdk.xml.MachineFactory;
import com.kkgame.utils.DeviceUtil;
import com.lidroid.jxutils.BitmapUtils;


public class Picsee_dialog_ho extends Basedialogview {

	private LinearLayout ll_mPre;

	private ImageButton iv_mPre;

	private ExtendedViewPager vp_photo;

	private ArrayList<String> images;

	private TouchImageAdapter touchImageAdapter;
	private int currentid;
	

	public Picsee_dialog_ho(Activity activity) {
		super(activity);
	}
	
	public Picsee_dialog_ho(Activity activity,ArrayList<String> imagurls,int currentid) {
		super(activity);
		this.images=imagurls;
		this.currentid=currentid;
		//System.out.println(images.get(currentid));
		initlog();
	}

	@Override
	public void createDialog(Activity mActivity) {
		dialog = new Dialog(mContext);

		int ho_height = 650;
		int ho_with = 750;
		int po_height = 650;
		int po_with = 700;

		int height = 0;
		int with = 0;
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

		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		baselin = new LinearLayout(mContext);
		baselin.setOrientation(LinearLayout.VERTICAL);
		MachineFactory machineFactory = new MachineFactory(mActivity);
		machineFactory.MachineView(baselin, with, height, "LinearLayout");
		baselin.setBackgroundColor(Color.TRANSPARENT);
		baselin.setGravity(Gravity.CENTER_VERTICAL);

		// 中间内容
		LinearLayout ll_content = new LinearLayout(mContext);
		machineFactory.MachineView(ll_content, with, height, 0,
				mLinearLayout, 0, 25, 0, 0, 100);
		ll_content.setBackgroundColor(Color.TRANSPARENT);
		ll_content.setGravity(Gravity.CENTER_HORIZONTAL);
		ll_content.setOrientation(LinearLayout.VERTICAL);

		ScrollView sv_photo = new ScrollView(mActivity);
		machineFactory.MachineView(sv_photo, MATCH_PARENT, height, 0, mLinearLayout, 0,
				0, 0, 0, 100);

		// 中间内容
		LinearLayout ll_content2 = new LinearLayout(mContext);
		machineFactory.MachineView(ll_content2, with, height, 0, mLinearLayout,
				0, 0, 0, 0, 100);
		ll_content.setBackgroundColor(Color.WHITE);
		ll_content.setGravity(Gravity.CENTER_HORIZONTAL);
		ll_content.setOrientation(LinearLayout.VERTICAL);

		vp_photo = new ExtendedViewPager(mContext);
		machineFactory.MachineView(vp_photo, with, height, 0, mLinearLayout,
				0, 0, 0, 0, 100);

		ll_content.addView(sv_photo);

		sv_photo.addView(ll_content2);

		ll_content2.addView(vp_photo);

		baselin.addView(ll_content);

		dialog.setContentView(baselin);
		Window dialogWindow = dialog.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		dialogWindow.setGravity(Gravity.CENTER);

		lp.alpha = 0.9f; // 透明度

		lp.dimAmount = 0.5f; // 设置背景色对比度

		dialogWindow.setAttributes(lp);
		dialog.setCanceledOnTouchOutside(false);

		android.widget.RelativeLayout.LayoutParams ap2 = new android.widget.RelativeLayout.LayoutParams(
				android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT,
				android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);

		dialog.getWindow().setBackgroundDrawable(new BitmapDrawable());
		dialog.getWindow().isFloating();
		Window window = dialog.getWindow();
		dialog.setCanceledOnTouchOutside(true);
		
	}

	private void initlog() {

		
		touchImageAdapter = new TouchImageAdapter();
		vp_photo.setAdapter(touchImageAdapter);
		vp_photo.setCurrentItem(currentid);
		
	}

	private class TouchImageAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return images.size();
		}

		@Override
		public View instantiateItem(ViewGroup container, int position) {
			ImageView img = null;
			//String string = images.get(position);

			img = new TouchImageView(container.getContext());

			BitmapUtils bitmapUtils = new BitmapUtils(mContext);
			bitmapUtils.display(img, images.get(position));
			//bitmapUtils.dis
			container.addView(img, LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.MATCH_PARENT);
			return img;

		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

	}

}
