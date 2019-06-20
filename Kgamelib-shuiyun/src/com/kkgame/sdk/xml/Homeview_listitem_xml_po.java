package com.kkgame.sdk.xml;




import com.kkgame.sdk.utils.Myimageview;

import android.app.Activity;
import android.graphics.Color;
import android.text.util.Linkify;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Homeview_listitem_xml_po extends Basexml implements Layoutxml {

	private Myimageview iv_mHeadicon;
	private TextView tv_mContent;
	private TextView tv_mLikecount;
	private TextView tv_mComment;
	private ImageView iv_mLike;
	private ImageView iv_mLike1;
	private TextView tv_mName;
	private LinearLayout ll_line2;
	private LinearLayout ll_mPicture1;
	private LinearLayout ll_mPicture2;
	private TextView tv_mTime;
	private LinearLayout ll_mLike;

	public Homeview_listitem_xml_po(Activity activity) {
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
		machineFactory.MachineView(iv_mHeadicon, 80, 80, mLinearLayout, 2, 30);

		LinearLayout ll_leftcontent = new LinearLayout(mContext);
		machineFactory.MachineView(ll_leftcontent, MATCH_PARENT, WRAP_CONTENT,
				mLinearLayout, 2, 30);
		ll_leftcontent.setOrientation(LinearLayout.VERTICAL);

		LinearLayout ll_line1 = new LinearLayout(mContext);
		machineFactory.MachineView(ll_line1, MATCH_PARENT, WRAP_CONTENT, 0,
				mLinearLayout, 20, 0, 0, 0, 100);

		ImageView iv_icon = new ImageView(mContext);
		machineFactory.MachineView(iv_icon, 25, 25, mLinearLayout);
		iv_icon.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"yaya_writeicon.png", mActivity));

		tv_mName = new TextView(mContext);
		machineFactory.MachineTextView(tv_mName, WRAP_CONTENT, WRAP_CONTENT, 0,
				"", 24, mLinearLayout, 5, 0, 5, 0);
		tv_mName.setTextColor(Color.parseColor("#ff6900"));
		//tv_mName.setGravity(Gravity_CENTER);

		// TODO
		ll_line1.addView(iv_icon);
		ll_line1.addView(tv_mName);

		ll_line2 = new LinearLayout(mContext);
		machineFactory.MachineView(ll_line2, MATCH_PARENT, 0, 1, mLinearLayout);
		ll_line2.setOrientation(LinearLayout.VERTICAL);
		
		// 内容
		tv_mContent = new TextView(mContext);
		machineFactory.MachineTextView(tv_mContent, MATCH_PARENT, MATCH_PARENT,
				0, "", 28, mLinearLayout, 0, 0, 0, 0);
		//tv_mContent.setMaxLines(3);
		// tv_mContent.setMinLines(3);
		//tv_mContent.setEllipsize(TruncateAt.END);
		tv_mContent.setLineSpacing(0, (float) 1.1);
		tv_mContent.setAutoLinkMask(Linkify.ALL);

		ll_mPicture1 = new LinearLayout(mContext);
		machineFactory.MachineView(ll_mPicture1, WRAP_CONTENT, WRAP_CONTENT,
				mLinearLayout);

		ll_mPicture2 = new LinearLayout(mContext);
		machineFactory.MachineView(ll_mPicture2, WRAP_CONTENT, WRAP_CONTENT,
				mLinearLayout);

		// TODO
		ll_line2.addView(tv_mContent);
		ll_line2.addView(ll_mPicture1);
		ll_line2.addView(ll_mPicture2);

		// 左边第三行点赞行
		LinearLayout ll_line3 = new LinearLayout(mContext);
		machineFactory.MachineView(ll_line3, MATCH_PARENT, 40, mLinearLayout);

		/*LinearLayout ll_zhanwei = new LinearLayout(mContext);
		machineFactory.MachineView(ll_zhanwei, 0, MATCH_PARENT, 1,
				mLinearLayout);*/
		
		//时间
		tv_mTime = new TextView(mContext);
		machineFactory.MachineTextView(tv_mTime, 0, MATCH_PARENT, 1, "", 18, mLinearLayout, 20, 0, 0, 0);
		tv_mTime.setGravity(Gravity.CENTER_VERTICAL);

		ll_mLike = new LinearLayout(mContext);
		machineFactory.MachineView(ll_mLike, WRAP_CONTENT, WRAP_CONTENT, mLinearLayout);
		
		// 已点赞
		iv_mLike = new ImageView(mActivity);
		machineFactory.MachineView(iv_mLike, 30, 30, mLinearLayout,2,3);
		iv_mLike.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"yaya_like.png", mActivity));

		// 未点赞
		iv_mLike1 = new ImageView(mActivity);
		machineFactory.MachineView(iv_mLike1, 30, 30, mLinearLayout,2,3);
		iv_mLike1.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"yaya_like1.png", mActivity));

		tv_mLikecount = new TextView(mContext);
		machineFactory.MachineTextView(tv_mLikecount, WRAP_CONTENT,
				WRAP_CONTENT, 0, "", 24, mLinearLayout, 5, 0, 0, 0);

		// 评论图片
		ImageView iv_comment = new ImageView(mActivity);
		machineFactory.MachineView(iv_comment, 30, 30, 0,mLinearLayout, 20, 3,0,0,100);
		iv_comment.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"yaya_taolun.png", mActivity));

		// 评论
		tv_mComment = new TextView(mContext);
		machineFactory.MachineTextView(tv_mComment, WRAP_CONTENT, WRAP_CONTENT,
				0, "", 24, mLinearLayout, 5, 0, 0, 0);

		// TODO
		ll_line3.addView(tv_mTime);
		
		ll_mLike.addView(iv_mLike1);
		ll_mLike.addView(iv_mLike);
		ll_mLike.addView(tv_mLikecount);
		ll_mLike.addView(iv_comment);
		ll_mLike.addView(tv_mComment);
		
		ll_line3.addView(ll_mLike);

		LinearLayout ll_line23 = new LinearLayout(mContext);
		machineFactory.MachineView(ll_line23, MATCH_PARENT, MATCH_PARENT,
				mLinearLayout);
		ll_line23.setBackgroundDrawable(GetAssetsutils
				.get9DrawableFromAssetsFile("yaya_taolunbackground.9.png",
						mContext));
		ll_line23.setOrientation(LinearLayout.VERTICAL);
		ll_line23.setPadding(20, 10, 20, 0);

		ll_line23.addView(ll_line2);
		ll_line23.addView(ll_line3);

		// TODO
		ll_leftcontent.addView(ll_line1);
		ll_leftcontent.addView(ll_line23);

		// TODO

		ll_content.addView(iv_mHeadicon);
		ll_content.addView(ll_leftcontent);

		linearLayout.addView(ll_content);

		return linearLayout;
	}

	/**
	 * 得到一个有属性的imageview控件
	 * @return
	 */
	public ImageView getImageview() {
		ImageView iv_imageview = new ImageView(mContext);
		machineFactory.MachineView(iv_imageview, 120, 120, 0,mLinearLayout,20,10,0,0,100);
		return iv_imageview;

	}

	public Myimageview getIv_mHeadicon() {
		return iv_mHeadicon;
	}

	public void setIv_mHeadicon(Myimageview iv_mHeadicon) {
		this.iv_mHeadicon = iv_mHeadicon;
	}

	

	public TextView getTv_mContent() {
		return tv_mContent;
	}

	public void setTv_mContent(TextView tv_mContent) {
		this.tv_mContent = tv_mContent;
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

	public LinearLayout getLl_mPicture1() {
		return ll_mPicture1;
	}

	public void setLl_mPicture1(LinearLayout ll_mPicture1) {
		this.ll_mPicture1 = ll_mPicture1;
	}

	public LinearLayout getLl_mPicture2() {
		return ll_mPicture2;
	}

	public void setLl_mPicture2(LinearLayout ll_mPicture2) {
		this.ll_mPicture2 = ll_mPicture2;
	}

	public TextView getTv_mTime() {
		return tv_mTime;
	}

	public void setTv_mTime(TextView tv_mTime) {
		this.tv_mTime = tv_mTime;
	}

	public LinearLayout getLl_mLike() {
		return ll_mLike;
	}

	public void setLl_mLike(LinearLayout ll_mLike) {
		this.ll_mLike = ll_mLike;
	}

	
	
}
