package com.kkgame.sdk.pay;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kkgame.sdk.bean.Question;
import com.kkgame.sdk.utils.Basedialogview;
import com.kkgame.sdk.xml.GetAssetsutils;
import com.kkgame.sdk.xml.Help_listviewitem;
import com.kkgame.sdk.xml.MachineFactory;
import com.kkgame.utils.DeviceUtil;

public class Help_dialog extends Basedialogview {

	private LinearLayout ll_mPre;
	private ImageButton iv_mPre;
	private ListView lv_helpcontent;
	private ProgressBar pb_mPb;
	private ArrayList<Question> mQuestionList;
	protected static final int SHOWCONTENT = 3;

	public Help_dialog(Activity activity) {
		super(activity);
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SHOWCONTENT:
				showContent();
				break;

			default:
				break;
			}
		}

	};
	private MYQuestionAdapter MYquestionAdapter;

	@Override
	public void createDialog(Activity mActivity) {
		dialog = new Dialog(mContext);

		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		int ho_height = 650;
		int ho_with = 750;
		int po_height = 850;
		int po_with = 650;

		int height = 0;
		int with = 0;
		int pad=0;
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
		
		baselin = new LinearLayout(mContext);
		baselin.setOrientation(LinearLayout.VERTICAL);
		MachineFactory machineFactory = new MachineFactory(mActivity);
		machineFactory.MachineView(baselin, with, height, "LinearLayout");
		baselin.setBackgroundColor(Color.TRANSPARENT);
		baselin.setGravity(Gravity.CENTER_VERTICAL);

		// 中间内容
		LinearLayout ll_content = new LinearLayout(mContext);
		machineFactory.MachineView(ll_content, with, height, mLinearLayout,2,25);
		ll_content.setBackgroundColor(Color.WHITE);
		ll_content.setGravity(Gravity.CENTER_HORIZONTAL);
		ll_content.setOrientation(LinearLayout.VERTICAL);

		// 标题栏
		RelativeLayout rl_title = new RelativeLayout(mContext);
		machineFactory.MachineView(rl_title, MATCH_PARENT, 96, mLinearLayout);
		rl_title.setBackgroundColor(Color.parseColor("#999999"));

		ll_mPre = new LinearLayout(mContext);
		machineFactory.MachineView(ll_mPre, 96, MATCH_PARENT, 0,
				mRelativeLayout, 0, 0, 0, 0, RelativeLayout.CENTER_VERTICAL);
		ll_mPre.setGravity(Gravity_CENTER);
		ll_mPre.setClickable(true);
		// 返回上一层的图片
		iv_mPre = new ImageButton(mContext);
		machineFactory.MachineView(iv_mPre, 40, 40, 0, mLinearLayout, 0, 0, 0,
				0, RelativeLayout.CENTER_VERTICAL);
		iv_mPre.setClickable(false);
		/*
		 * iv_mPre.setImageDrawable(GetAssetsutils.getDrawableFromAssetsFile(
		 * "yaya_pre.png", mContext));
		 */
		iv_mPre.setBackgroundDrawable(GetAssetsutils.getDrawableFromAssetsFile(
				"yaya_pre.png", mActivity));
		ll_mPre.addView(iv_mPre);
		ll_mPre.setClickable(true);
		ll_mPre.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();

			}
		});

		// 注册textview
		TextView tv_zhuce = new TextView(mContext);
		machineFactory.MachineTextView(tv_zhuce, MATCH_PARENT, MATCH_PARENT, 0,
				"帮助中心", 38, mLinearLayout, 0, 0, 0, 0);
		tv_zhuce.setTextColor(Color.WHITE);
		tv_zhuce.setGravity(Gravity_CENTER);

		// TODO
		rl_title.addView(ll_mPre);
		rl_title.addView(tv_zhuce);

		pb_mPb = new ProgressBar(mActivity);
		machineFactory.MachineView(pb_mPb, 40, 40,
				mLinearLayout,2,400);

		// 帮助的列表内容
		lv_helpcontent = new ListView(mActivity);
		machineFactory.MachineView(lv_helpcontent, MATCH_PARENT, MATCH_PARENT,0,
				mLinearLayout,20,0,20,10,100);
		lv_helpcontent.setVisibility(View.GONE);

		ll_content.addView(rl_title);
		ll_content.addView(lv_helpcontent);
		ll_content.addView(pb_mPb);

		
		//baselin.addView(rl_title);
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

		dialog.setCanceledOnTouchOutside(true);
		dialog.getWindow().setBackgroundDrawable(new BitmapDrawable());

		initlogic();
	}

	private void initlogic() {

		// 获取数据
		

	}

	/**
	 * 显示文章列表
	 */
	protected void showContent() {
		if (mQuestionList != null) {
			pb_mPb.setVisibility(View.GONE);
			MYquestionAdapter = new MYQuestionAdapter();
			lv_helpcontent.setAdapter(MYquestionAdapter);
			lv_helpcontent.setVisibility(View.VISIBLE);
		}

	}

	public class MYQuestionAdapter extends BaseAdapter {

		private Context mContext;

		class ViewHolder {

			TextView mTitle;
			TextView mContent;
		}

		public int getCount() {
			return mQuestionList.size();
		}

		public Object getItem(int position) {
			return mQuestionList.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				Help_listviewitem lv_item = new Help_listviewitem(mActivity);
				convertView = lv_item.initViewxml();
				holder.mTitle = (TextView) lv_item.getTv_mQuestion();
				holder.mContent = (TextView) lv_item.getTv_mAs();
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			Question question = mQuestionList.get(position);

			holder.mTitle.setText(question.name);
			
			holder.mTitle.setTextSize(machSize(18));
			holder.mTitle.setTextColor(Color.parseColor("#eb6109"));
			
			holder.mContent.setText("  "+question.content);

			return convertView;
		}
	}
}
