package com.kkgame.sdk.smallhelp;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kkgame.sdk.bean.Discuss;
import com.kkgame.sdk.bean.DiscussResponse;
import com.kkgame.sdk.bean.User;
import com.kkgame.sdk.utils.MybitmapUtils;
import com.kkgame.sdk.utils.Myimageview;
import com.kkgame.sdk.utils.ShuoMClickableSpan;
import com.kkgame.sdk.utils.TextOnClickListener;
import com.kkgame.sdk.utils.Utilsjf;
import com.kkgame.sdk.xml.DisplayUtils;
import com.kkgame.sdk.xml.GetAssetsutils;
import com.kkgame.sdk.xml.Homeview_listitem_xml_po;
import com.kkgame.sdk.xml.Homeview_xml_po;
import com.kkgame.sdkmain.AgentApp;
import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Sputils;
import com.kkgame.utils.Yayalog;
import com.lidroid.jxutils.BitmapUtils;
import com.lidroid.jxutils.HttpUtils;
import com.lidroid.jxutils.exception.HttpException;
import com.lidroid.jxutils.http.RequestParams;
import com.lidroid.jxutils.http.ResponseInfo;
import com.lidroid.jxutils.http.callback.RequestCallBack;
import com.lidroid.jxutils.http.client.HttpRequest.HttpMethod;


public class HomeView extends BaseContentView {

	private Homeview_xml_po mThisview;
	private ArrayList<DiscussResponse> mDiscussres;
	private ArrayList<Discuss> discusses;
	private ListView lv_Homelist;
	private int page = 1;
	private HomeAdapter homeAdapter;
	private int key = 0;// 网络请求数据的开关
	private HttpUtils httpUtils;

	public HomeView(Activity activity) {
		super(activity);
	}

	@Override
	public View initview() {
		mThisview = new Homeview_xml_po(mActivity);
		return mThisview.initViewxml();
	}

	@Override
	public void initdata() {
		discusses = new ArrayList<Discuss>();

		page = 1;
		// 设置隐藏listview
		lv_Homelist = mThisview.getLv_Homelist();

		pb_mLoading = mThisview.getPb_mLoading();
		pb_mLoading.setVisibility(View.VISIBLE);
		lv_Homelist.setVisibility(View.GONE);

		final EditText et_mFabiao = mThisview.getEt_mFabiao();

		ImageView iv_xiaobai = mThisview.getIv_xiaobai();

		Button bt_mFabiao = mThisview.getBt_mFabiao();

		iv_xiaobai.setClickable(true);
		iv_xiaobai.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mActivity,
						Push_feeling_Activity.class);
				mActivity.startActivity(intent);
			}
		});

		bt_mFabiao.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				User mUser = AgentApp.mUser;
				String Feel = et_mFabiao.getText().toString();

				if (Feel.length() < 6) {
					Toast.makeText(mContext, "输入的内容太少了~!", 0).show();
					return;
				}
				Utilsjf.creDialogpro(mActivity, "正在发表...");
				RequestParams params = new RequestParams();

				params.addBodyParameter("is_app", 1 + "");
				params.addBodyParameter("thread_id",
						"game" + DeviceUtil.getGameId(mActivity));
				params.addBodyParameter("uid", mUser.uid + "");
				params.addBodyParameter("app_id",
						DeviceUtil.getGameId(mActivity));
				params.addBodyParameter("token", mUser.token);

				params.addBodyParameter("discuss", Feel);
				params.addBodyParameter("pid", 0 + "");
				params.addBodyParameter("oid", DeviceUtil.getGameId(mActivity));
				params.addBodyParameter("type", 0 + "");

				httpUtils.send(HttpMethod.POST,
						"http://www.yayawan.com/discuss/add_discuss", params,
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
										page = 1;
										et_mFabiao.setText("");
										initdataFromnet(page);
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
								Toast.makeText(mContext, "发表失败", 0).show();
								Utilsjf.stopDialog();
							}
						});

			}
		});

		lv_Homelist.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// System.out.println("第一个条目是:" + firstVisibleItem);
				// System.out.println("总数:" + discusses.size());

				if (firstVisibleItem == (page * 10 - 5)) {
					// System.out.println("要拿的page" + page);
					page = page + 1;
					initdataFromnet(page);

				}

			}
		});

		lv_Homelist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Comment_dialog_ho comment_dialog_ho = new Comment_dialog_ho(
						mActivity, discusses.get(arg2));
				comment_dialog_ho.dialogShow();
			}
		});

		initdataFromnet(page);

	}

	/**
	 * 从网络获取数据
	 */
	private void initdataFromnet(int page1) {
		httpUtils = new HttpUtils();
		RequestParams params = new RequestParams();

		httpUtils.configCurrentHttpCacheExpiry(0);
		String geturl = "http://www.yayawan.com/discuss/discuss_list?"
				+ "thread_id=game" + DeviceUtil.getGameId(mContext)
				+ "&limit=10" + "&page=" + page1 + "&type=0&is_app=1";

		httpUtils.send(HttpMethod.GET, geturl, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				// System.out.println(responseInfo.result);
				// TODO
				DiscussResponse parserDiscuss = new DiscussResponse();
				/*try {
					//Yayalog.loger(responseInfo.result);
					//parserDiscuss = JSONParser
					//		.parserDiscuss(responseInfo.result);
					// Logger.e("得到的用户", message);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (parserDiscuss.getDiscuss() != null) {
					dealwithData(parserDiscuss.getDiscuss());
				}*/

			}

			@Override
			public void onFailure(HttpException error, String msg) {
				// TODO Auto-generated method stub
				// System.out.println(error);
				Toast.makeText(mContext, "请检查网络...", 0).show();
			}
		});
	}

	/**
	 * 处理网络获取的数据
	 * 
	 * @param list
	 */
	private void dealwithData(List<Discuss> list) {

		
		if (page == 1) {
			discusses = new ArrayList<Discuss>();
			discusses.addAll(list);
		} else {
			discusses.addAll(list);
		}

		if (homeAdapter == null) {
			homeAdapter = new HomeAdapter();
			lv_Homelist.setAdapter(homeAdapter);

		} else {
			homeAdapter.notifyDataSetChanged();
		}
		lv_Homelist.setVisibility(View.VISIBLE);
		pb_mLoading.setVisibility(View.GONE);

	}

	/**
	 * listviewadapter
	 * 
	 * @author jf
	 * 
	 */
	public class HomeAdapter extends BaseAdapter {

		private Homeview_listitem_xml_po view;

		private int i = 0;
		private ArrayList<String> imageurls = new ArrayList<String>();

		private BitmapUtils bitmapUtils;

		class ViewHolder {

			Myimageview iv_mHeadicon;
			TextView tv_mName;
			TextView tv_mContent;
			TextView tv_mLikecount;
			TextView tv_mComment;
			ImageView iv_mLike;
			ImageView iv_mLike1;
			LinearLayout ll_mLike;
			LinearLayout ll_line2;
			LinearLayout ll_mPicture1;
			LinearLayout ll_mPicture2;

			TextView tv_mTime;
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
			final ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				view = new Homeview_listitem_xml_po(mActivity);
				convertView = view.initViewxml();
				holder.iv_mHeadicon = view.getIv_mHeadicon();
				holder.tv_mName = view.getTv_mName();
				holder.tv_mContent = view.getTv_mContent();
				holder.iv_mLike = view.getIv_mLike();
				holder.iv_mLike1 = view.getIv_mLike1();
				holder.tv_mComment = view.getTv_mComment();
				holder.tv_mLikecount = view.getTv_mLikecount();
				holder.ll_mPicture1 = view.getLl_mPicture1();
				holder.ll_mPicture2 = view.getLl_mPicture2();
				holder.ll_mLike = view.getLl_mLike();
				holder.tv_mTime = view.getTv_mTime();
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
				holder.ll_mPicture1.removeAllViews();
				holder.ll_mPicture2.removeAllViews();
			}

			bitmapUtils = new BitmapUtils(mActivity);

			bitmapUtils.configDefaultLoadingImage(GetAssetsutils
					.getImageFromAssetsFile("yaya_defaultloading.png",
							mActivity));
			// 随机分配一个icon
			String headicon = "yaya_headicon"
					+ Utilsjf.getRanknumber(discusses.get(position).getUser())+".png";
			bitmapUtils.configDefaultLoadFailedImage(GetAssetsutils
					.getImageFromAssetsFile("yaya_default_head.png",
							mActivity));
			MybitmapUtils.displayImage(mActivity, holder.iv_mHeadicon, discusses.get(position)
						.getIcon()
						+ "?imageView/1/w/"
						+ machSize(80)
						+ "/h/"
						+ ""
						+ machSize(80) + "/q/100", headicon);
		//	Yayalog.loger("填充的内容是:"+discusses.get(position).getMessage());
			

			holder.tv_mName.setText(discusses.get(position).getUser());

			if (discusses.get(position).getUser().equals("丫丫玩")) {
				holder.tv_mName.setTextColor(Color.parseColor("#f13435"));
				//holder.tv_mName.setTextSize();
				holder.tv_mName.setTypeface(Typeface.DEFAULT_BOLD);
				String str=discusses.get(position).getMessage();
				
				  if (str.contains("http")) {
						int kouhaoindex = str.indexOf("[url");
						int firstindexurl = str.indexOf("url=");
						int lastIndexOf = str.indexOf("]阅读原文");
						String content = str.substring(0,kouhaoindex);
						 String url = str.substring(firstindexurl+4, lastIndexOf);
						// System.out.println(substring);
						// System.out.println(substring2);
						 String ttt = "阅读全文";
						 holder.tv_mContent.setText(content);
							SpannableString spanttt = new SpannableString(ttt);

							 final ShuoMClickableSpan clickttt = new ShuoMClickableSpan(ttt, mActivity);
							clickttt.setOnclickListener(new TextOnClickListener() {
								
								@Override
								public void onclick() {
									// TODO Auto-generated method stub
									//Toast.makeText(mContext, ""+clickttt.urltext, 0).show();
									Intent intent = new Intent();        
							        intent.setAction("android.intent.action.VIEW");    
							        Uri content_url = Uri.parse(clickttt.urltext);   
							        intent.setData(content_url);  
							        mActivity.startActivity(intent);
								}
							});
							clickttt.urltext=url;
							spanttt.setSpan(clickttt, 0, ttt.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
							holder.tv_mContent.append(spanttt);
							holder.tv_mContent.setMovementMethod(LinkMovementMethod.getInstance());
					}else {
						 holder.tv_mContent.setText(discusses.get(position).getMessage());
					}
				
				
			}else {
				holder.tv_mName.setTextColor(Color.parseColor("#ff6900"));
				holder.tv_mName.setTypeface(Typeface.DEFAULT);
				holder.tv_mContent.setText(discusses.get(position).getMessage());
			}
			
			holder.tv_mLikecount.setText(discusses.get(position).getLike());
			holder.tv_mComment.setText(discusses.get(position).getCount_c());
			
			Yayalog.loger(discusses.get(position).getMessage());
			holder.tv_mTime.setText(discusses.get(position).getCreate_time());

			String like_id = Sputils.getSPstring("like_id", "", mContext);

			String[] like_ids = like_id.split(",");
			holder.iv_mLike.setVisibility(View.GONE);
			holder.iv_mLike1.setVisibility(View.VISIBLE);
			for (int i = 0; i < like_ids.length; i++) {
				if (like_ids[i].equals(discusses.get(position).getId())) {
					holder.iv_mLike.setVisibility(View.VISIBLE);
					holder.iv_mLike1.setVisibility(View.GONE);
				}
			}

			// holder.iv_mLike.setVisibility(View.GONE);

			final int temposition = position;
			// holder.iv_mLike1.setClickable(true);

			holder.ll_mLike.setClickable(true);
			// 点赞功能
			holder.ll_mLike.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					holder.iv_mLike.setVisibility(View.VISIBLE);
					holder.iv_mLike1.setVisibility(View.GONE);

					String url = "http://www.yayawan.com/discuss/like_ajax?"
							+ "like=1&id=" + discusses.get(temposition).getId();

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
										discusses.get(temposition).setLike("1");

										//holder.tv_mContent.setText("好傻啊");
										// homeAdapter.notifyDataSetChanged();
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

									if (success == 0) {

										saveLikeid(discusses.get(temposition)
												.getId());
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

			String imgs = discusses.get(position).getImg();
			// System.out.println(imgs);
			if (imgs.length() < 6) {

			} else {
				final String[] splits = imgs.split(",");

				for (int i = 0; i < splits.length; i++) {
					if (i < 5) {
						final ImageView imageview = view.getImageview();

						bitmapUtils.display(imageview, splits[i]
								+ "?imageView/1/w/" + machSize(80) + "/h/" + ""
								+ machSize(80) + "/q/100");
						holder.ll_mPicture1.addView(imageview);
						imageview.setId(i);
						imageview.setOnClickListener(new OnClickListener() {
 
							@Override
							public void onClick(View v) {
								int id = imageview.getId();
								// System.out.println(splits[id]);
								File file = bitmapUtils
										.getBitmapFileFromDiskCache(splits[id]);

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
						final ImageView imageview = view.getImageview();
						// bitmapUtils.configDefaultLoadingImage(GetAssetsutils.getImageFromAssetsFile("yaya_defaultloading.png",
						// mActivity));
						bitmapUtils.display(imageview, splits[i]);
						holder.ll_mPicture2.addView(imageview);
						imageview.setId(i);
						imageview.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								int id = imageview.getId();
								File file = bitmapUtils
										.getBitmapFileFromDiskCache(splits[id]);
								Intent intent = new Intent();
								intent.setAction(android.content.Intent.ACTION_VIEW);
								intent.setDataAndType(Uri.fromFile(file),
										"image/*");
								mActivity.startActivity(intent);
							}
						});
					}
				}
			}

			return convertView;
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

	// 控制第一次resume不取数据
	private int controlgetdata = 0;
	private ProgressBar pb_mLoading;

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (controlgetdata == 0) {
			controlgetdata = 1;
		} else {
			page = 1;
			initdataFromnet(page);
		}

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
