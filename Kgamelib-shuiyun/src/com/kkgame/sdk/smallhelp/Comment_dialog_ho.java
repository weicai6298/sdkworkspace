package com.kkgame.sdk.smallhelp;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kkgame.sdk.bean.ComentDiscuss;
import com.kkgame.sdk.bean.ComentRespose;
import com.kkgame.sdk.bean.Discuss;
import com.kkgame.sdk.bean.User;
import com.kkgame.sdk.utils.Basedialogview;
import com.kkgame.sdk.utils.MybitmapUtils;
import com.kkgame.sdk.utils.Myimageview;
import com.kkgame.sdk.utils.Utilsjf;
import com.kkgame.sdk.xml.Comment_listitem_xml_po;
import com.kkgame.sdk.xml.GetAssetsutils;
import com.kkgame.sdk.xml.Homeview_listitem_xml_po;
import com.kkgame.sdk.xml.MachineFactory;
import com.kkgame.sdkmain.AgentApp;
import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Sputils;
import com.lidroid.jxutils.BitmapUtils;
import com.lidroid.jxutils.HttpUtils;
import com.lidroid.jxutils.exception.HttpException;
import com.lidroid.jxutils.http.RequestParams;
import com.lidroid.jxutils.http.ResponseInfo;
import com.lidroid.jxutils.http.callback.RequestCallBack;
import com.lidroid.jxutils.http.client.HttpRequest.HttpMethod;

public class Comment_dialog_ho extends Basedialogview {

	public Comment_dialog_ho(Activity activity, Discuss discuss) {
		super(activity, discuss);

	}

	private ListView lv_Comentlist;
	private EditText et_mFabiao;
	private Button bt_mFabiao;
	private ViewGroup ll_deleline;
	private LinearLayout ll_mPre;
	private ImageButton iv_mPre;
	private Homeview_listitem_xml_po mHeadview;
	private TextView tv_mContent;
	private ArrayList<ComentDiscuss> discusses;
	private int page;
	private CommentAdapter CommentAdapter;
	private Myimageview iv_mHeadicon;
	private TextView tv_mName;
	private ImageView iv_mLike;
	private ImageView iv_mLike1;
	private TextView tv_mLikecount;
	private TextView tv_mComment;
	private LinearLayout ll_mPicture1;
	private LinearLayout ll_mPicture2;
	private TextView tv_mTime;
	private String[] splits;
	private int i;
	private LinearLayout ll_mLike;
	private String responseid = "";

	@Override
	public void createDialog(Activity mActivity) {

		// System.out.println(discuss);

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
		RelativeLayout rl_content = new RelativeLayout(mContext);
		machineFactory.MachineView(rl_content, with, height, mLinearLayout, 2,
				25);
		rl_content.setBackgroundColor(Color.WHITE);
		// rl_content.setGravity(Gravity.CENTER_HORIZONTAL);
		// rl_content.setOrientation(LinearLayout.VERTICAL);

		// 中间内容
		LinearLayout ll_content = new LinearLayout(mContext);
		machineFactory.MachineView(ll_content, with, height, 0,
				mRelativeLayout, 0, 0, 0, 0, 100);
		ll_content.setBackgroundColor(Color.WHITE);
		ll_content.setGravity(Gravity.CENTER_HORIZONTAL);
		ll_content.setOrientation(LinearLayout.VERTICAL);

		// 标题栏
		RelativeLayout rl_title = new RelativeLayout(mContext);
		machineFactory.MachineView(rl_title, MATCH_PARENT, 90, mLinearLayout);
		rl_title.setBackgroundColor(Color.parseColor("#999999"));

		ll_mPre = new LinearLayout(mContext);
		machineFactory.MachineView(ll_mPre, 90, MATCH_PARENT, 0,
				mRelativeLayout, 0, 0, 0, 0, RelativeLayout.CENTER_VERTICAL);
		ll_mPre.setGravity(Gravity_CENTER);
		ll_mPre.setClickable(true);
		// 返回上一层的图片
		iv_mPre = new ImageButton(mContext);
		machineFactory.MachineView(iv_mPre, 40, 40, 0, mLinearLayout, 0, 0, 0,
				0, RelativeLayout.CENTER_VERTICAL);
		iv_mPre.setClickable(false);

		iv_mPre.setBackgroundDrawable(GetAssetsutils.getDrawableFromAssetsFile(
				"yaya_pre.png", mActivity));
		ll_mPre.addView(iv_mPre);
		// 设置点击事件.点击窗口消失
		ll_mPre.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		// tiletextview
		TextView tv_zhuce = new TextView(mContext);
		machineFactory.MachineTextView(tv_zhuce, MATCH_PARENT, MATCH_PARENT, 0,
				"评论", 38, mLinearLayout, 0, 0, 0, 0);
		tv_zhuce.setTextColor(Color.WHITE);
		tv_zhuce.setGravity(Gravity_CENTER);

		// TODO
		rl_title.addView(ll_mPre);
		rl_title.addView(tv_zhuce);

		lv_Comentlist = new ListView(mActivity);

		machineFactory.MachineView(lv_Comentlist, MATCH_PARENT, MATCH_PARENT,
				0, mLinearLayout, 15, 0, 15, 100, 100);
		lv_Comentlist.setDivider(null);

		LinearLayout ll_mFabiao = new LinearLayout(mActivity);
		machineFactory
				.MachineView(ll_mFabiao, MATCH_PARENT, 100, 0, mRelativeLayout,
						0, 0, 0, 0, RelativeLayout.ALIGN_PARENT_BOTTOM);
		ll_mFabiao.setGravity(Gravity.CENTER_VERTICAL);
		ll_mFabiao.setBackgroundColor(Color.parseColor("#f1f1f1"));

		ImageView iv_xiaobai = new ImageView(mActivity);
		machineFactory.MachineView(iv_xiaobai, 60, 60, mLinearLayout, 1, 20);
		iv_xiaobai.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"yaya_xiaobai.png", mActivity));

		et_mFabiao = new EditText(mActivity);
		machineFactory.MachineEditText(et_mFabiao, 0, 70, 1, "说点什么吧~", 26,
				mLinearLayout, 20, 0, 0, 0);
		et_mFabiao.setBackgroundColor(Color.WHITE);
		et_mFabiao.setGravity(Gravity.CENTER_VERTICAL);
		et_mFabiao.setPadding(2, 2, 0, 0);

		bt_mFabiao = new Button(mContext);
		machineFactory.MachineButton(bt_mFabiao, 110, 80, 0, "发送", 28,
				mLinearLayout, 0, 0, 0, 0);
		bt_mFabiao.setTextColor(Color.parseColor("#1888d7"));
		bt_mFabiao.setBackgroundColor(Color.parseColor("#f1f1f1"));
		bt_mFabiao.setGravity(Gravity_CENTER);
		bt_mFabiao.setPadding(0, 2, 0, 0);

		// TODO
		ll_mFabiao.addView(iv_xiaobai);
		ll_mFabiao.addView(et_mFabiao);
		ll_mFabiao.addView(bt_mFabiao);

		ll_content.addView(rl_title);
		ll_content.addView(lv_Comentlist);

		rl_content.addView(ll_content);
		rl_content.addView(ll_mFabiao);

		baselin.addView(rl_content);
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

		dialog.setCanceledOnTouchOutside(true);
		initlog();
	}

	private void initlog() {

		page = 1;

		discusses = new ArrayList<ComentDiscuss>();

		mHeadview = new Homeview_listitem_xml_po(mActivity);

		View initViewxml = mHeadview.initViewxml();

		iv_mHeadicon = mHeadview.getIv_mHeadicon();

		tv_mName = mHeadview.getTv_mName();

		tv_mContent = mHeadview.getTv_mContent();

		iv_mLike = mHeadview.getIv_mLike();

		iv_mLike1 = mHeadview.getIv_mLike1();

		ll_mLike = mHeadview.getLl_mLike();

		tv_mLikecount = mHeadview.getTv_mLikecount();

		tv_mComment = mHeadview.getTv_mComment();

		ll_mPicture1 = mHeadview.getLl_mPicture1();

		ll_mPicture2 = mHeadview.getLl_mPicture2();

		tv_mTime = mHeadview.getTv_mTime();

		setHeaddata();

		lv_Comentlist.addHeaderView(initViewxml);

		// 从网络获取图片
		getdataFromnet(page);

		bt_mFabiao.setOnClickListener(new OnClickListener() {

			private String pid;

			@Override
			public void onClick(View v) {

				String Feel = et_mFabiao.getText().toString();
				if (Feel.length() < 5) {
					Toast.makeText(mContext, "内容太少哦~!", 0).show();
					return;
				}
				String tempfeel = Feel.substring(0, 4);
				if (tempfeel.contains("回复")) {
					pid = responseid;
				}
				Feel = Feel.substring(Feel.indexOf(":") + 1);

				Utilsjf.creDialogpro(mActivity, "正在上传~~");
				User mUser = AgentApp.mUser;
				HttpUtils httpUtils = new HttpUtils();
				RequestParams params = new RequestParams();

				params.addBodyParameter("is_app", 1 + "");
				params.addBodyParameter("did", discuss.getId());
				params.addBodyParameter("uid", mUser.uid + "");
				params.addBodyParameter("app_id",
						DeviceUtil.getGameId(mActivity));
				params.addBodyParameter("token", mUser.token);

				params.addBodyParameter("discuss", Feel);
				params.addBodyParameter("pid", pid);

				// params.addBodyParameter("img", imgurls + "");

				httpUtils.send(HttpMethod.POST,
						"http://www.yayawan.com/discuss/add_reply", params,
						new RequestCallBack<String>() {

							@Override
							public void onSuccess(
									ResponseInfo<String> responseInfo) {
								Utilsjf.stopDialog();
								JSONObject jsonObject;
								try {
									jsonObject = new JSONObject(
											responseInfo.result);
									int success = jsonObject.getInt("success");
									if (success == 0) {
										Toast.makeText(mContext, "发表成功", 0)
												.show();
										et_mFabiao.setText("");
										getdataFromnet(1);
									} else {
										Toast.makeText(mContext, "发表失败", 0)
												.show();
									}
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									Toast.makeText(mContext, "发表失败", 0).show();
									Utilsjf.stopDialog();

									e.printStackTrace();
								}

							}

							@Override
							public void onFailure(HttpException error,
									String msg) {
								Utilsjf.stopDialog();
								Toast.makeText(mContext, "发表失败", 0).show();

							}
						});

			}

		});
	}

	/**
	 * 设置头部数据
	 */
	private void setHeaddata() {

		// System.out.println(discuss.getUser()+"====================");
		final BitmapUtils bitmapUtils = new BitmapUtils(mActivity);

		// 随机分配一个icon
		String headicon = "yaya_headicon" + Utilsjf.getRanknumber(discuss.getUser())
				+ ".png";

		MybitmapUtils.displayImage(mActivity, iv_mHeadicon, discuss.getIcon()
				+ "?imageView/1/w/" + machSize(80) + "/h/" + "" + machSize(80)
				+ "/q/100", headicon);
		/*
		 * holder.iv_mHeadicon.setImageBitmap(GetAssetsutils
		 * .getImageFromAssetsFile("yaya_appicon.png", mContext));
		 */
		// mHeadview.getTv_mName().setText(discuss.getUser());;
		tv_mName.setText(discuss.getUser());
		tv_mLikecount.setText(discuss.getLike());
		tv_mComment.setText(discuss.getCount_c());
		tv_mContent.setText(discuss.getMessage());
		tv_mTime.setText(discuss.getCreate_time());

		iv_mLike.setVisibility(View.GONE);

		String like_id = Sputils.getSPstring("like_id", "", mContext);

		String[] like_ids = like_id.split(",");
		iv_mLike.setVisibility(View.GONE);
		iv_mLike1.setVisibility(View.VISIBLE);
		for (int i = 0; i < like_ids.length; i++) {
			if (like_ids[i].equals(discuss.getId())) {
				iv_mLike.setVisibility(View.VISIBLE);
				iv_mLike1.setVisibility(View.GONE);
			}
		}

		ll_mLike.setClickable(true);
		// 点赞功能
		ll_mLike.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// System.out.println("点击我了");
				HttpUtils httpUtils = new HttpUtils();
				iv_mLike.setVisibility(View.VISIBLE);
				iv_mLike1.setVisibility(View.GONE);
				tv_mLikecount.setText((Integer.parseInt(discuss.getLike()) + 1)
						+ "");
				String url = "http://www.yayawan.com/discuss/like_ajax?"
						+ "like=1&id=" + discuss.getId();
				httpUtils.send(HttpMethod.GET, url,
						new RequestCallBack<String>() {
							@Override
							public void onSuccess(
									ResponseInfo<String> responseInfo) {

								int success = -1;
								int id = -1;
								try {
									// System.out.println(responseInfo.result);
									JSONObject jsonObject = new JSONObject(
											responseInfo.result);
									success = (Integer) jsonObject
											.get("success");
									id = (Integer) jsonObject.get("id");

									// homeAdapter.notifyDataSetChanged();
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

								if (success == 0) {

									saveLikeid(discuss.getId());
									// System.out.println("点赞成功" + id);
								}
							}

							@Override
							public void onFailure(HttpException error,
									String msg) {

							}
						});

			}
		});

		String imgs = discuss.getImg();
		// System.out.println(imgs);
		if (imgs.length() < 6) {

		} else {
			splits = imgs.split(",");
			for (i = 0; i < splits.length; i++) {
				if (i < 5) {
					final ImageView imageview = mHeadview.getImageview();
					bitmapUtils.display(imageview, splits[i]);
					ll_mPicture1.addView(imageview);
					imageview.setId(i);
					imageview.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {

							int id = imageview.getId();

							ArrayList<String> imgurls = new ArrayList<String>();
							for (int i = 0; i < splits.length; i++) {
								imgurls.add(splits[i]);
								// System.out.println(splits[i]);
							}

							new Picsee_dialog_ho(mActivity, imgurls, id)
									.dialogShow();
						}
					});
				} else {
					final ImageView imageview = mHeadview.getImageview();
					bitmapUtils.display(imageview, splits[i]);
					ll_mPicture2.addView(imageview);
					imageview.setId(i);
					imageview.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							int id = imageview.getId();
							File file = bitmapUtils
									.getBitmapFileFromDiskCache(splits[id]);
							Intent intent = new Intent();
							intent.setAction(android.content.Intent.ACTION_VIEW);
							intent.setDataAndType(Uri.fromFile(file), "image/*");
							mActivity.startActivity(intent);
						}
					});
				}
			}
		}

	}

	private void saveLikeid(String id) {
		String like_id = Sputils.getSPstring("like_id", "", mContext);

		String[] like_ids = like_id.split(",");

		if (like_ids.length < 50) {
			Sputils.putSPstring("like_id", like_id + "," + id, mContext);
		} else {
			like_id = like_id.substring(like_id.indexOf(",") + 1);
			Sputils.putSPstring("like_id", like_id + "," + id, mContext);
		}

	}

	int k = 2;

	/**
	 * 从网络获取数据
	 * 
	 * @param page2
	 */

	private void getdataFromnet(int page2) {
		k = k + 1;
		HttpUtils httpUtils = new HttpUtils();
		RequestParams params = new RequestParams();

		String requesturl = "http://www.yayawan.com/discuss/reply_list?id="
				+ discuss.getId() + "&limit=20&page=" + page2 + "&is_app=1"
				+ "&_=1432801414" + k;

		// System.out.println("评论请求的url" + requesturl);
		httpUtils.configCurrentHttpCacheExpiry(0);
		httpUtils.send(HttpMethod.GET, requesturl,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						// System.out.println(responseInfo.result);
						// TODO

						ComentRespose parserDiscuss = new ComentRespose();
					/*	// System.out.println(responseInfo.result);
						try {
							parserDiscuss = JSONParser
									.parserComentlist(responseInfo.result);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}*/
						dealwithData(parserDiscuss.getDiscuss());

					}

					@Override
					public void onFailure(HttpException error, String msg) {
						// TODO Auto-generated method stub
						// System.out.println(error);
					}
				});

	}

	/**
	 * 处理网络获取的数据
	 * 
	 * @param list
	 */
	private void dealwithData(List<ComentDiscuss> list) {
		discusses = new ArrayList<ComentDiscuss>();
		discusses.addAll(list);

		if (CommentAdapter == null) {
			CommentAdapter = new CommentAdapter();
			lv_Comentlist.setAdapter(CommentAdapter);
		} else {
			CommentAdapter.notifyDataSetChanged();
		}

	}

	public class CommentAdapter extends BaseAdapter {

		private Comment_listitem_xml_po view;
		private String[] splits;
		private int i = 0;
		private ArrayList<String> imageurls = new ArrayList<String>();

		class ViewHolder {
			Myimageview iv_mHeadicon;
			TextView tv_mName;
			LinearLayout ll_line2;
			TextView tv_mContent;

			TextView tv_mTime;

			TextView tv_mParent;

			TextView tv_mRe;

			TextView tv_mLikecount;

			TextView tv_mComment;
			TextView tv_huifu;
			LinearLayout ll_line3;
		}

		public int getCount() {
			return discusses.size();
		}

		public Object getItem(int position) {
			return discusses.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				view = new Comment_listitem_xml_po(mActivity);
				convertView = view.initViewxml();

				holder.iv_mHeadicon = view.getIv_mHeadicon();
				holder.tv_mName = view.getTv_mName();
				holder.tv_mContent = view.getTv_mContent();
				holder.ll_line3 = view.getLl_line3();
				holder.tv_mParent = view.getTv_mParent();
				holder.tv_mRe = view.getTv_mRe();

				holder.tv_mTime = view.getTv_mTime();
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();

			}

			holder.tv_mName.setText(discusses.get(position).getUser() + ":");
			if (!TextUtils.isEmpty(discusses.get(position).getParent())) {
				holder.tv_mRe.setVisibility(View.VISIBLE);
				holder.tv_mParent.setText(" "
						+ discusses.get(position).getParent());
			}

			// TODO
			// 随机分配一个icon
			String headicon = "yaya_headicon"
					+ Utilsjf.getRanknumber(discusses.get(position).getUser()) + ".png";

			MybitmapUtils.displayImage(mActivity, holder.iv_mHeadicon,
					discusses.get(position).getIcon() + "?imageView/1/w/"
							+ machSize(80) + "/h/" + "" + machSize(80)
							+ "/q/100", headicon);

			/*
			 * holder.iv_mHeadicon.setImageBitmap(GetAssetsutils
			 * .getImageFromAssetsFile("yaya_appicon.png", mActivity));
			 */

			holder.tv_mContent.setText(discusses.get(position).getMessage());
			holder.tv_mTime.setText(discusses.get(position).getCreate_time());

			holder.ll_line3.setId(position);

			holder.ll_line3.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					int id = v.getId();
					et_mFabiao.setText("");
					et_mFabiao.setText("回复" + discusses.get(id).getUser()
							+ " :");
					responseid = discusses.get(id).getId();
					final int length = et_mFabiao.getText().length();
					et_mFabiao.setSelection(et_mFabiao.getText().length());

					/**
					 * 添加监听
					 */
					et_mFabiao.addTextChangedListener(new TextWatcher() {

						@Override
						public void onTextChanged(CharSequence s, int start,
								int before, int count) {
							// TODO Auto-generated method stub

						}

						@Override
						public void beforeTextChanged(CharSequence s,
								int start, int count, int after) {
							// TODO Auto-generated method stub

						}

						@Override
						public void afterTextChanged(Editable s) {
							// TODO Auto-generated method stub
							if (2 < et_mFabiao.length()
									&& et_mFabiao.length() < length) {
								et_mFabiao.setText("");
								et_mFabiao.removeTextChangedListener(this);
							}
						}
					});
				}
			});

			return convertView;
		}
	}

	
}
