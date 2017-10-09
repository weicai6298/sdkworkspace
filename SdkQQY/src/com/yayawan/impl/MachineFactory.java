package com.yayawan.impl;



import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class MachineFactory {

	private Context mContext;
	private LayoutParams lp;
	private android.widget.RelativeLayout.LayoutParams rlp;
	private Activity mActivity;

	public MachineFactory(Activity mActivity) {
		// TODO Auto-generated constructor stub
		this.mActivity = mActivity;
	}

	/**
	 * 设定view的参数
	 * 
	 * @param view
	 * @param with
	 *            宽
	 * @param height
	 *            高
	 * @param lpname
	 *            父控件
	 * @return view
	 */
	public View MachineView(View view, int with, int height, String lpname) {
		return MachineView(view, with, height, 0, lpname, 0, 0, 0, 0, 100);
	}

	/**
	 * 设定view的参数
	 * 
	 * @param view
	 * @param with
	 * @param height
	 * @param weight
	 * @param lpname
	 * @return
	 */
	public View MachineView(View view, int with, int height, int weight,
			String lpname) {
		return MachineView(view, with, height, weight, lpname, 0, 0, 0, 0, 100);
	}

	/**
	 * 
	 * @param view
	 * @param with
	 * @param height
	 * @param lpname
	 * @param magintype
	 *            1是left 2是top 3.是right 4是buttom
	 * @param magin
	 * @return
	 */
	public View MachineView(View view, int with, int height, String lpname,
			int magintype, int magin) {
		switch (magintype) {
		case 1:

			return MachineView(view, with, height, 0, lpname, magin, 0, 0, 0,
					100);

		case 2:
			return MachineView(view, with, height, 0, lpname, 0, magin, 0, 0,
					100);

		case 3:
			return MachineView(view, with, height, 0, lpname, 0, 0, magin, 0,
					100);

		case 4:
			return MachineView(view, with, height, 0, lpname, 0, 0, 0, magin,
					100);

		default:
			return MachineView(view, with, height, 0, lpname, 0, 0, 0, 0, 100);

		}

	}

	/**
	 * 
	 * @param view
	 * @param with
	 * @param height
	 * @param weight
	 * @param lpname
	 * @param magleft
	 * @param magtop
	 * @param magright
	 * @param magbut
	 * @param addrule
	 * @return
	 */
	public View MachineView(View view, int with, int height, float weight,
			String lpname, int magleft, int magtop, int magright, int magbut,
			int addrule) {
		ViewPr viewPr = new ViewPr();
		if (with > 0) {
			with = machSize(with);
		}
		if (height > 0) {
			height = machSize(height);
		}

		magbut = machSize(magbut);
		magleft = machSize(magleft);
		magtop = machSize(magtop);
		magright = machSize(magright);
		viewPr.setView(view);
		;
		if (lpname.equals("LinearLayout")) {
			if (weight == 0) {
				lp = new android.widget.LinearLayout.LayoutParams(with, height);
			} else {
				lp = new android.widget.LinearLayout.LayoutParams(with, height,
						weight);
			}
			lp.setMargins(magleft, magtop, magright, magbut);

			view.setLayoutParams(lp);
			viewPr.setLp(lp);
		} else if (lpname.equals("RelativeLayout")) {

			android.widget.RelativeLayout.LayoutParams rlp = new android.widget.RelativeLayout.LayoutParams(
					with, height);
			rlp.setMargins(magleft, magtop, magright, magbut);
			if (addrule != 100) {
				rlp.addRule(addrule);
			}

			view.setLayoutParams(rlp);
			viewPr.setRlp(rlp);
		}

		return view;
	}

	/**
	 * 设定textview的参数
	 * 
	 * @param view
	 * @param with
	 *            宽
	 * @param height
	 *            高
	 * @param text
	 *            文字内容
	 * @param textsize
	 *            文字大小
	 * @param lpname
	 *            父控件
	 * @param magleft
	 *            maginleft
	 * @param magtop
	 *            magintop
	 * @param magright
	 *            magright
	 * @param magbut
	 *            magright
	 * @return textview
	 */
	public TextView MachineTextView(TextView view, int with, int height,
			float weight, String text, int textsize, String lpname,
			int magleft, int magtop, int magright, int magbut) {

		if (with > 0) {
			with = machSize(with);
		}
		if (height > 0) {
			height = machSize(height);
		}
		magbut = machSize(magbut);
		magleft = machSize(magleft);
		magtop = machSize(magtop);
		magright = machSize(magright);
		textsize = machSize(textsize);
		view.setTextColor(Color.parseColor("#333333"));
		if (lpname.equals("LinearLayout")) {
			if (weight == 0) {
				lp = new android.widget.LinearLayout.LayoutParams(with, height);
			} else {
				lp = new android.widget.LinearLayout.LayoutParams(with, height,
						weight);
			}

			view.setText(text);
			view.setTextSize(TypedValue.COMPLEX_UNIT_PX, textsize);
			lp.setMargins(magleft, magtop, magright, magbut);
			view.setLayoutParams(lp);
		} else if (lpname.equals("RelativeLayout")) {

			rlp = new android.widget.RelativeLayout.LayoutParams(with, height);

			view.setText(text);
			view.setTextSize(TypedValue.COMPLEX_UNIT_PX, textsize);
			rlp.setMargins(magleft, magtop, magright, magbut);
			view.setLayoutParams(rlp);

		}

		return view;
	}

	/**
	 * 
	 * @param view
	 * @param with
	 * @param height
	 * @param weight
	 * @param text
	 * @param textsize
	 * @param lpname
	 * @param magleft
	 * @param magtop
	 * @param magright
	 * @param magbut
	 * @param addrule
	 * @return textview
	 */
	public TextView MachineTextView(TextView view, int with, int height,
			float weight, String text, int textsize, String lpname,
			int magleft, int magtop, int magright, int magbut, int addrule) {

		if (with > 0) {
			with = machSize(with);
		}
		if (height > 0) {
			height = machSize(height);
		}
		magbut = machSize(magbut);
		magleft = machSize(magleft);
		magtop = machSize(magtop);
		magright = machSize(magright);
		textsize = machSize(textsize);

		if (lpname.equals("LinearLayout")) {
			if (weight == 0) {
				lp = new android.widget.LinearLayout.LayoutParams(with, height);
			} else {
				lp = new android.widget.LinearLayout.LayoutParams(with, height,
						weight);
			}

			view.setText(text);
			view.setTextSize(TypedValue.COMPLEX_UNIT_PX, textsize);
			lp.setMargins(magleft, magtop, magright, magbut);
			view.setLayoutParams(lp);
		} else if (lpname.equals("RelativeLayout")) {

			rlp = new android.widget.RelativeLayout.LayoutParams(with, height);

			if (addrule != 100) {
				rlp.addRule(addrule);
			}
			view.setText(text);
			view.setTextSize(TypedValue.COMPLEX_UNIT_PX, textsize);
			rlp.setMargins(magleft, magtop, magright, magbut);
			view.setLayoutParams(rlp);

		}

		return view;
	}

	/**
	 * 设定textview的参数
	 * 
	 * @param view
	 * @param with
	 *            宽
	 * @param height
	 *            高
	 * @param text
	 *            hint文字内容
	 * @param textsize
	 *            文字大小
	 * @param lpname
	 *            父控件
	 * @param magleft
	 *            maginleft
	 * @param magtop
	 *            magintop
	 * @param magright
	 *            magright
	 * @param magbut
	 *            magright
	 * @return textview
	 */
	public EditText MachineEditText(EditText view, int with, int height,
			float weight, String text, int textsize, String lpname,
			int magleft, int magtop, int magright, int magbut) {

		if (with > 0) {
			with = machSize(with);
		}
		if (height > 0) {
			height = machSize(height);
		}
		magbut = machSize(magbut);
		magleft = machSize(magleft);
		magtop = machSize(magtop);
		magright = machSize(magright);
		textsize = machSize(textsize);
		// Log.e("textsia", textsize + "+++++++++++++");
		if (lpname.equals("LinearLayout")) {
			if (weight == 0) {
				lp = new android.widget.LinearLayout.LayoutParams(with, height);
			} else {
				lp = new android.widget.LinearLayout.LayoutParams(with, height,
						weight);
			}

			view.setHint(text);
			view.setTextSize(TypedValue.COMPLEX_UNIT_PX, textsize);
			// view.setTextSize(textsize);
			lp.setMargins(magleft, magtop, magright, magbut);
			view.setLayoutParams(lp);
		} else if (lpname.equals("RelativeLayout")) {

			rlp = new android.widget.RelativeLayout.LayoutParams(with, height);

			view.setHint(text);
			view.setTextSize(TypedValue.COMPLEX_UNIT_PX, textsize);
			rlp.setMargins(magleft, magtop, magright, magbut);
			view.setLayoutParams(rlp);

		}

		return view;
	}

	/**
	 * 设定textview的参数
	 * 
	 * @param view
	 * @param with
	 *            宽
	 * @param height
	 *            高
	 * @param text
	 *            文字内容
	 * @param textsize
	 *            文字大小
	 * @param lpname
	 *            父控件
	 * @param magleft
	 *            maginleft
	 * @param magtop
	 *            magintop
	 * @param magright
	 *            magright
	 * @param magbut
	 *            magright
	 * @return button
	 */
	public Button MachineButton(Button view, int with, int height,
			float weight, String text, int textsize, String lpname,
			int magleft, int magtop, int magright, int magbut) {

		if (with > 0) {
			with = machSize(with);
		}
		if (height > 0) {
			height = machSize(height);
		}
		magbut = machSize(magbut);
		magleft = machSize(magleft);
		magtop = machSize(magtop);
		magright = machSize(magright);
		// Log.e("matextsize前", textsize + "");
		textsize = machSize(textsize);
		// Log.e("matextsize后", textsize + "");
		if (lpname.equals("LinearLayout")) {
			if (weight == 0) {
				lp = new android.widget.LinearLayout.LayoutParams(with, height);
			} else {
				lp = new android.widget.LinearLayout.LayoutParams(with, height,
						weight);
			}
			view.setText(text);
			view.setTextSize(TypedValue.COMPLEX_UNIT_PX, textsize);
			lp.setMargins(magleft, magtop, magright, magbut);

			view.setLayoutParams(lp);
		} else if (lpname.equals("RelativeLayout")) {

			android.widget.RelativeLayout.LayoutParams rlp = new android.widget.RelativeLayout.LayoutParams(
					with, height);
			view.setText(text);
			view.setTextSize(TypedValue.COMPLEX_UNIT_PX, textsize);
			rlp.setMargins(magleft, magtop, magright, magbut);
			view.setLayoutParams(rlp);
		}

		return view;
	}

	/**
	 * 设定textview的参数
	 * 
	 * @param view
	 * @param with
	 *            宽
	 * @param height
	 *            高
	 * @param text
	 *            文字内容
	 * @param textsize
	 *            文字大小
	 * @param lpname
	 *            父控件
	 * @param magleft
	 *            maginleft
	 * @param magtop
	 *            magintop
	 * @param magright
	 *            magright
	 * @param magbut
	 *            magright
	 * @return button
	 */
	public Button MachineButton(Button view, int with, int height,
			float weight, String text, int textsize, String lpname,
			int magleft, int magtop, int magright, int magbut, int addrule) {

		if (with > 0) {
			with = machSize(with);
		}
		if (height > 0) {
			height = machSize(height);
		}
		magbut = machSize(magbut);
		magleft = machSize(magleft);
		magtop = machSize(magtop);
		magright = machSize(magright);
		// Log.e("matextsize前", textsize + "");
		textsize = machSize(textsize);
		// Log.e("matextsize后", textsize + "");
		if (lpname.equals("LinearLayout")) {
			if (weight == 0) {
				lp = new android.widget.LinearLayout.LayoutParams(with, height);
			} else {
				lp = new android.widget.LinearLayout.LayoutParams(with, height,
						weight);
			}
			view.setText(text);
			view.setTextSize(TypedValue.COMPLEX_UNIT_PX, textsize);
			lp.setMargins(magleft, magtop, magright, magbut);
			view.setLayoutParams(lp);
		} else if (lpname.equals("RelativeLayout")) {

			android.widget.RelativeLayout.LayoutParams rlp = new android.widget.RelativeLayout.LayoutParams(
					with, height);
			view.setText(text);
			view.setTextSize(TypedValue.COMPLEX_UNIT_PX, textsize);
			if (addrule != 100) {
				rlp.addRule(addrule);
			}
			rlp.setMargins(magleft, magtop, magright, magbut);
			view.setLayoutParams(rlp);
		}

		return view;
	}

	/**
	 * 将720像素转成其他像素值
	 * 
	 * @param size
	 * @return
	 */
	private int machSize(int size) {
		float widthPx = 720;

		DisplayMetrics dm = new DisplayMetrics();
		mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels;// 宽度
		int height = dm.heightPixels;// 高度

		if (width < height) {
			widthPx = width;
		} else {
			widthPx = height;
		}
		if (widthPx == 720) {
			return size;
		}
		if (widthPx > 1080) {
			widthPx = 1080;
		}
		float bili = 720 / widthPx;
		// Log.e("bili", bili+"++++++++++++++");
		int resize = (int) ((size / bili) + 0.5);
		// Log.e("后size", resize+"++++++++++++++");
		return resize;

	}

	class ViewPr {
		private View view;
		private android.widget.LinearLayout.LayoutParams lp;
		private android.widget.RelativeLayout.LayoutParams rlp;
		private TextView mtextView;
		private Button mButton;

		public View getView() {
			return view;
		}

		public void setView(View view) {
			this.view = view;
		}

		public android.widget.LinearLayout.LayoutParams getLp() {
			return lp;
		}

		public void setLp(android.widget.LinearLayout.LayoutParams lp) {
			this.lp = lp;
		}

		public android.widget.RelativeLayout.LayoutParams getRlp() {
			return rlp;
		}

		public void setRlp(android.widget.RelativeLayout.LayoutParams rlp) {
			this.rlp = rlp;
		}

		public TextView getMtextView() {
			return mtextView;
		}

		public void setMtextView(TextView mtextView) {
			this.mtextView = mtextView;
		}

		public Button getmButton() {
			return mButton;
		}

		public void setmButton(Button mButton) {
			this.mButton = mButton;
		}

	}
}
