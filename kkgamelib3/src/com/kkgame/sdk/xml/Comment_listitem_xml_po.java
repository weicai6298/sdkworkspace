package com.kkgame.sdk.xml;



import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kkgame.sdk.utils.Myimageview;

public class Comment_listitem_xml_po extends Basexml implements Layoutxml {

	private Myimageview iv_mHeadicon;
	private TextView tv_mName;
	private LinearLayout ll_line2;
	private TextView tv_mContent;

	private TextView tv_mTime;

	private ImageView iv_mLike;

	private ImageView iv_mLike1;

	private TextView tv_mLikecount;

	private TextView tv_mComment;
	private TextView tv_huifu;
	private TextView tv_mRe;
	private TextView tv_mParent;
	private LinearLayout ll_line3;

	public Comment_listitem_xml_po(Activity activity) {
		super(activity);

	}

	@Override
	public View initViewxml() {
		// 基类布局
		LinearLayout linearLayout = new LinearLayout(mContext);
		new android.widget.AbsListView.LayoutParams(MATCH_PARENT, MATCH_PARENT);
		/*
		 * linearLayout=(LinearLayout) machineFactory.MachineView(linearLayout,
		 * MATCH_PARENT, MATCH_PARENT,0, mRelativeLayout);
		 */
		linearLayout.setBackgroundColor(Color.WHITE);
		linearLayout.setGravity(Gravity.CENTER_VERTICAL);

		LinearLayout ll_content = new LinearLayout(mContext);
		machineFactory.MachineView(ll_content, MATCH_PARENT, MATCH_PARENT, 0,
				mRelativeLayout, 0, 0, 0, 0, 100);

		// 用户头像
		iv_mHeadicon = new Myimageview(mContext);
		machineFactory.MachineView(iv_mHeadicon, 60, 60, 0, mLinearLayout, 20,
				30, 0, 0, 100);

		LinearLayout ll_leftcontent = new LinearLayout(mContext);
		machineFactory.MachineView(ll_leftcontent, MATCH_PARENT, WRAP_CONTENT,
				mLinearLayout, 2, 30);
		ll_leftcontent.setOrientation(LinearLayout.VERTICAL);

		LinearLayout ll_line1 = new LinearLayout(mContext);
		machineFactory.MachineView(ll_line1, MATCH_PARENT, WRAP_CONTENT, 0,
				mLinearLayout, 20, 0, 0, 0, 100);

		tv_mName = new TextView(mContext);
		machineFactory.MachineTextView(tv_mName, WRAP_CONTENT, WRAP_CONTENT, 0,
				"", 24, mLinearLayout, 5, 0, 0, 0);
		tv_mName.setTextColor(Color.parseColor("#7e7e7e"));
		
		tv_mRe = new TextView(mContext);
		machineFactory.MachineTextView(tv_mRe, WRAP_CONTENT, WRAP_CONTENT, 0,
				"回复", 24, mLinearLayout, 5, 0, 0, 0);
		tv_mRe.setVisibility(View.GONE);
		
		tv_mParent = new TextView(mContext);
		machineFactory.MachineTextView(tv_mParent, WRAP_CONTENT, WRAP_CONTENT, 0,
				"", 24, mLinearLayout, 5, 0, 0, 0);
		tv_mParent.setTextColor(Color.parseColor("#ec7600"));
		// TODO
		ll_line1.addView(tv_mName);
		ll_line1.addView(tv_mRe);
		ll_line1.addView(tv_mParent);

		ll_line2 = new LinearLayout(mContext);
		machineFactory.MachineView(ll_line2, MATCH_PARENT, 0, 1, mLinearLayout,
				20, 5, 0, 0, 100);
		ll_line2.setOrientation(LinearLayout.VERTICAL);

		tv_mContent = new TextView(mContext);
		machineFactory.MachineTextView(tv_mContent, MATCH_PARENT, WRAP_CONTENT,
				0, "", 26, mLinearLayout, 0, 0, 0, 0);

		ll_line2.addView(tv_mContent);

		ll_line3 = new LinearLayout(mContext);
		machineFactory.MachineView(ll_line3, MATCH_PARENT, 40, mLinearLayout,
				2, 5);

		/*
		 * LinearLayout ll_zhanwei = new LinearLayout(mContext);
		 * machineFactory.MachineView(ll_zhanwei, 0, MATCH_PARENT, 1,
		 * mLinearLayout);
		 */

		// 时间
		tv_mTime = new TextView(mContext);
		machineFactory.MachineTextView(tv_mTime, 0, MATCH_PARENT, 1, "", 18,
				mLinearLayout, 20, 0, 0, 0);
		tv_mTime.setGravity(Gravity.CENTER_VERTICAL);

		// 已点赞
		iv_mLike = new ImageView(mActivity);
		machineFactory.MachineView(iv_mLike, 30, 30, mLinearLayout, 2, 3);
		iv_mLike.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"yaya_like.png", mActivity));

		// 未点赞
		iv_mLike1 = new ImageView(mActivity);
		machineFactory.MachineView(iv_mLike1, 30, 30, mLinearLayout, 2, 3);
		iv_mLike1.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"yaya_like1.png", mActivity));

		tv_mLikecount = new TextView(mContext);
		machineFactory.MachineTextView(tv_mLikecount, WRAP_CONTENT,
				WRAP_CONTENT, 0, "", 24, mLinearLayout, 5, 0, 0, 0);

		// 评论图片
		ImageView iv_comment = new ImageView(mActivity);
		machineFactory.MachineView(iv_comment, 30, 30, 0, mLinearLayout, 20, 3,
				20, 0, 100);
		iv_comment.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"yaya_taolun.png", mActivity));

		// 评论
		tv_mComment = new TextView(mContext);
		machineFactory.MachineTextView(tv_mComment, WRAP_CONTENT, WRAP_CONTENT,
				0, "", 24, mLinearLayout, 5, 0, 20, 0);

		// TODO
		ll_line3.addView(tv_mTime);
		// ll_line3.addView(iv_mLike1);
		// ll_line3.addView(iv_mLike);
		// ll_line3.addView(tv_mLikecount);
		ll_line3.addView(iv_comment);
		// ll_line3.addView(tv_mComment);

		/*
		 * // 左边第4行点赞行 LinearLayout ll_line4 = new LinearLayout(mContext);
		 * machineFactory.MachineView(ll_line4, MATCH_PARENT, 40, mLinearLayout,
		 * 2, 5);
		 * 
		 * LinearLayout ll_zhanwei = new LinearLayout(mContext);
		 * machineFactory.MachineView(ll_zhanwei, 0, MATCH_PARENT, 1,
		 * mLinearLayout);
		 * 
		 * tv_huifu = new TextView(mContext);
		 * machineFactory.MachineTextView(tv_huifu, 70, MATCH_PARENT, 0, "回复",
		 * 26, mLinearLayout, 0, 0, 0, 0);
		 * tv_huifu.setTextColor(Color.parseColor("#1888d7"));
		 * 
		 * ll_line4.addView(ll_zhanwei); ll_line4.addView(tv_huifu);
		 */

		// 左边第5行点赞行
		LinearLayout ll_line5 = new LinearLayout(mContext);
		machineFactory.MachineView(ll_line5, MATCH_PARENT, 2, mLinearLayout, 2,
				3);
		ll_line5.setBackgroundColor(Color.parseColor("#e1e1e1"));

		// TODO
		ll_leftcontent.addView(ll_line1);
		ll_leftcontent.addView(ll_line2);
		ll_leftcontent.addView(ll_line3);
		// ll_leftcontent.addView(ll_line4);
		ll_leftcontent.addView(ll_line5);

		// TODO

		ll_content.addView(iv_mHeadicon);
		ll_content.addView(ll_leftcontent);

		linearLayout.addView(ll_content);

		return linearLayout;

	}

	public Myimageview getIv_mHeadicon() {
		return iv_mHeadicon;
	}

	public void setIv_mHeadicon(Myimageview iv_mHeadicon) {
		this.iv_mHeadicon = iv_mHeadicon;
	}

	public TextView getTv_mName() {
		return tv_mName;
	}

	public void setTv_mName(TextView tv_mName) {
		this.tv_mName = tv_mName;
	}

	public LinearLayout getLl_line2() {
		return ll_line2;
	}

	public void setLl_line2(LinearLayout ll_line2) {
		this.ll_line2 = ll_line2;
	}

	public TextView getTv_mContent() {
		return tv_mContent;
	}

	public void setTv_mContent(TextView tv_mContent) {
		this.tv_mContent = tv_mContent;
	}

	public TextView getTv_mTime() {
		return tv_mTime;
	}

	public void setTv_mTime(TextView tv_mTime) {
		this.tv_mTime = tv_mTime;
	}

	public ImageView getIv_mLike() {
		return iv_mLike;
	}

	public void setIv_mLike(ImageView iv_mLike) {
		this.iv_mLike = iv_mLike;
	}

	public ImageView getIv_mLike1() {
		return iv_mLike1;
	}

	public void setIv_mLike1(ImageView iv_mLike1) {
		this.iv_mLike1 = iv_mLike1;
	}

	public TextView getTv_mLikecount() {
		return tv_mLikecount;
	}

	public void setTv_mLikecount(TextView tv_mLikecount) {
		this.tv_mLikecount = tv_mLikecount;
	}

	public TextView getTv_mComment() {
		return tv_mComment;
	}

	public void setTv_mComment(TextView tv_mComment) {
		this.tv_mComment = tv_mComment;
	}

	public TextView getTv_huifu() {
		return tv_huifu;
	}

	public void setTv_huifu(TextView tv_huifu) {
		this.tv_huifu = tv_huifu;
	}

	public LinearLayout getLl_line3() {
		return ll_line3;
	}

	public void setLl_line3(LinearLayout ll_line3) {
		this.ll_line3 = ll_line3;
	}

	public TextView getTv_mRe() {
		return tv_mRe;
	}

	public void setTv_mRe(TextView tv_mRe) {
		this.tv_mRe = tv_mRe;
	}

	public TextView getTv_mParent() {
		return tv_mParent;
	}

	public void setTv_mParent(TextView tv_mParent) {
		this.tv_mParent = tv_mParent;
	}

}
