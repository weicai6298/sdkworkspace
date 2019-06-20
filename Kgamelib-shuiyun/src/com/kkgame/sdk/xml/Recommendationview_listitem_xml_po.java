package com.kkgame.sdk.xml;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Recommendationview_listitem_xml_po extends Basexml implements
		Layoutxml {

	private LinearLayout baseLinearLayout;
	private TextView tv_item1name;
	private TextView tv_item1type;
	private TextView tv_item1size;
	private ImageView iv_download;
	private TextView tv_item2name;
	private TextView tv_item2type;
	private TextView tv_item2size;
	private ImageView iv_download2;
	private ImageView iv_icon1;
	private ImageView iv_icon2;

	public Recommendationview_listitem_xml_po(Activity activity) {
		super(activity);

	}

	@Override
	public View initViewxml() {

		// 基类布局
		LinearLayout linearLayout = new LinearLayout(mContext);
		new android.widget.AbsListView.LayoutParams(MATCH_PARENT, MATCH_PARENT);
		linearLayout.setBackgroundColor(Color.WHITE);
		linearLayout.setGravity(Gravity.CENTER_VERTICAL);

		LinearLayout ll_content = new LinearLayout(mContext);
		machineFactory.MachineView(ll_content, MATCH_PARENT, MATCH_PARENT, 0,
				mRelativeLayout, 20, 0, 20, 0, 100);

		LinearLayout ll_item1 = new LinearLayout(mContext);
		machineFactory.MachineView(ll_item1, 0, 80, 1, mLinearLayout,10,20,0,0,100);
		ll_item1.setGravity(Gravity.CENTER_VERTICAL);
		
		iv_icon1 = new ImageView(mContext);
		machineFactory.MachineView(iv_icon1, 70, 70, mLinearLayout);
		iv_icon1.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"yaya_appicon.png", mActivity));

		// 左边的textview
		LinearLayout ll_textitem1 = new LinearLayout(mContext);
		machineFactory.MachineView(ll_textitem1, 0, MATCH_PARENT, 1,
				mLinearLayout,10,0,0,0,100);
		ll_textitem1.setOrientation(LinearLayout.VERTICAL);

		tv_item1name = new TextView(mContext);
		machineFactory.MachineTextView(tv_item1name, WRAP_CONTENT,
				WRAP_CONTENT, 0, "剑仙缘", 20, mLinearLayout, 0, 0, 0, 0);
		tv_item1name.setTextColor(Color.parseColor("#333333"));
		tv_item1name.setSingleLine(true);
		
		tv_item1type = new TextView(mContext);
		machineFactory.MachineTextView(tv_item1type, WRAP_CONTENT,
				WRAP_CONTENT, 0, "类型", 16, mLinearLayout, 0, 0, 0, 0);
		tv_item1type.setTextColor(Color.parseColor("#999999"));

		tv_item1size = new TextView(mContext);
		machineFactory.MachineTextView(tv_item1size, WRAP_CONTENT,
				WRAP_CONTENT, 0, "大小", 16, mLinearLayout, 0, 0, 0, 0);
		tv_item1size.setTextColor(Color.parseColor("#999999"));

		// TODO
		ll_textitem1.addView(tv_item1name);
		ll_textitem1.addView(tv_item1type);
		ll_textitem1.addView(tv_item1size);

		iv_download = new ImageView(mContext);
		machineFactory.MachineView(iv_download, 40, 40, mLinearLayout,3,10);
		iv_download.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"yaya_download.png", mActivity));

		// TODO
		ll_item1.addView(iv_icon1);
		ll_item1.addView(ll_textitem1);
		ll_item1.addView(iv_download);

		LinearLayout ll_item2 = new LinearLayout(mContext);
		machineFactory.MachineView(ll_item2, 0, 80, 1, mLinearLayout,20,20,0,0,100);
		ll_item2.setGravity(Gravity.CENTER_VERTICAL);
		
		iv_icon2 = new ImageView(mContext);
		machineFactory.MachineView(iv_icon2, 70, 70, mLinearLayout);
		iv_icon2.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"yaya_appicon.png", mActivity));

		// 左边的textview
		LinearLayout ll_textitem2 = new LinearLayout(mContext);
		machineFactory.MachineView(ll_textitem2, 0, MATCH_PARENT, 1,
				mLinearLayout,10,0,0,0,100);
		ll_textitem2.setOrientation(LinearLayout.VERTICAL);

		tv_item2name = new TextView(mContext);
		machineFactory.MachineTextView(tv_item2name, WRAP_CONTENT,
				WRAP_CONTENT, 0, "剑仙缘", 20, mLinearLayout, 0, 0, 0, 0);
		tv_item2name.setTextColor(Color.parseColor("#333333"));

		tv_item2type = new TextView(mContext);
		machineFactory.MachineTextView(tv_item2type, WRAP_CONTENT,
				WRAP_CONTENT, 0, "类型", 16, mLinearLayout, 0, 0, 0, 0);
		tv_item2type.setTextColor(Color.parseColor("#999999"));

		tv_item2size = new TextView(mContext);
		machineFactory.MachineTextView(tv_item2size, WRAP_CONTENT,
				WRAP_CONTENT, 0, "大小", 16, mLinearLayout, 0, 0, 0, 0);
		tv_item2size.setTextColor(Color.parseColor("#999999"));

		// TODO
		ll_textitem2.addView(tv_item2name);
		ll_textitem2.addView(tv_item2type);
		ll_textitem2.addView(tv_item2size);

		iv_download2 = new ImageView(mContext);
		machineFactory.MachineView(iv_download2, 40, 40, mLinearLayout,3,20);
		iv_download2.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"yaya_download.png", mActivity));

		// TODO
		ll_item2.addView(iv_icon2);
		ll_item2.addView(ll_textitem2);
		ll_item2.addView(iv_download2);

		ll_content.addView(ll_item1);
		ll_content.addView(ll_item2);
		linearLayout.addView(ll_content);

		return linearLayout;
	}

	public LinearLayout getBaseLinearLayout() {
		return baseLinearLayout;
	}

	public void setBaseLinearLayout(LinearLayout baseLinearLayout) {
		this.baseLinearLayout = baseLinearLayout;
	}

	public TextView getTv_item1name() {
		return tv_item1name;
	}

	public void setTv_item1name(TextView tv_item1name) {
		this.tv_item1name = tv_item1name;
	}

	public TextView getTv_item1type() {
		return tv_item1type;
	}

	public void setTv_item1type(TextView tv_item1type) {
		this.tv_item1type = tv_item1type;
	}

	public TextView getTv_item1size() {
		return tv_item1size;
	}

	public void setTv_item1size(TextView tv_item1size) {
		this.tv_item1size = tv_item1size;
	}

	

	public TextView getTv_item2name() {
		return tv_item2name;
	}

	public void setTv_item2name(TextView tv_item2name) {
		this.tv_item2name = tv_item2name;
	}

	public TextView getTv_item2type() {
		return tv_item2type;
	}

	public void setTv_item2type(TextView tv_item2type) {
		this.tv_item2type = tv_item2type;
	}

	public TextView getTv_item2size() {
		return tv_item2size;
	}

	public void setTv_item2size(TextView tv_item2size) {
		this.tv_item2size = tv_item2size;
	}

	public ImageView getIv_icon1() {
		return iv_icon1;
	}

	public void setIv_icon1(ImageView iv_icon1) {
		this.iv_icon1 = iv_icon1;
	}

	public ImageView getIv_icon2() {
		return iv_icon2;
	}

	public void setIv_icon2(ImageView iv_icon2) {
		this.iv_icon2 = iv_icon2;
	}

	public ImageView getIv_download() {
		return iv_download;
	}

	public void setIv_download(ImageView iv_download) {
		this.iv_download = iv_download;
	}

	public ImageView getIv_download2() {
		return iv_download2;
	}

	public void setIv_download2(ImageView iv_download2) {
		this.iv_download2 = iv_download2;
	}

	

}
