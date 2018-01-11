package com.kkgame.sdk.smallhelp;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipboardManager;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kkgame.sdk.bean.Gift;
import com.kkgame.sdk.bean.GiftInfo;
import com.kkgame.sdk.pay.Showgift_dialog;
import com.kkgame.sdk.pay.Showgiftcode_dialog;
import com.kkgame.sdk.xml.Giftview_listitem_xml_po;
import com.kkgame.sdk.xml.Giftview_xml_po;
import com.kkgame.sdkmain.AgentApp;

public class GiftView extends BaseContentView {

	private Giftview_xml_po mThisview;
	private ProgressBar pb_mLoading;
	private Button bt_mReload;
	private ListView lv_giftlist;
	private ArrayList<GiftInfo> mGiftList;
	private Gift mGift;

	private ClipboardManager mCmb;

	protected static final int SHOWGIFT = 3;

	protected static final int SHOWSTRATEGY = 4;

	protected static final int GETGIFTSUCCESS = 5;
	protected static final int ERROR = 6;

	protected static final String CLIPBOARD_SERVICE = null;

	public GiftView(Activity activity) {
		super(activity);
	}

	@Override
	public View initview() {
		mThisview = new Giftview_xml_po(mActivity);
		return mThisview.initViewxml();
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SHOWGIFT:
				lv_giftlist.setVisibility(View.VISIBLE);
				pb_mLoading.setVisibility(View.GONE);
				bt_mReload.setVisibility(View.GONE);
				showGift();

				break;
			case GETGIFTSUCCESS:
				if (mGift.is_success != 1) {
					Toast.makeText(mContext, mGift.body, Toast.LENGTH_SHORT)
							.show();

				}
				showCdKeyDialog();

				break;
			case ERROR:
				lv_giftlist.setVisibility(View.GONE);
				pb_mLoading.setVisibility(View.GONE);
				bt_mReload.setVisibility(View.VISIBLE);
				break;

			default:
				break;
			}
		}

	};

	@Override
	public void initdata() {
		// loading进度条
		pb_mLoading = mThisview.getPb_mLoading();
		pb_mLoading.setVisibility(View.VISIBLE);

		// 重新加载
		bt_mReload = mThisview.getBt_mReload();
		bt_mReload.setVisibility(View.GONE);

		bt_mReload.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				initdata();
			}
		});

		// listview列表
		lv_giftlist = mThisview.getLv_giftlist();

		lv_giftlist.setVisibility(View.GONE);
		// 获取礼包数据
		new Thread() {

			@Override
			public void run() {
				try {
					// mGiftList = ObtainData.getGameGiftList(mContext, 3);
					mHandler.sendEmptyMessage(SHOWGIFT);

				} catch (Exception e) {
					mHandler.sendEmptyMessage(ERROR);

					e.printStackTrace();
				}
			}
		}.start();
	}

	/**
	 * 显示礼包列表
	 */
	protected void showGift() {
		MyGiftAdapter myGiftAdapter = new MyGiftAdapter();
		lv_giftlist.setAdapter(myGiftAdapter);
		// System.out.println("++++++++++++++" + mGiftList.toString());
		lv_giftlist.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				final GiftInfo giftInfo = mGiftList.get(position);
				// 显示礼包信息dialog
				showGiftDialog(giftInfo);
			}
		});
		if (mGiftList.size() == 0) {
			lv_giftlist.setVisibility(View.GONE);
			pb_mLoading.setVisibility(View.GONE);
			bt_mReload.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 礼包适配器
	 * 
	 * @author Administrator
	 * 
	 */
	public class MyGiftAdapter extends BaseAdapter {

		class ViewHolder {
			TextView gifttext1;
			TextView gifttext2;
			TextView gifttext3;

		}

		public int getCount() {
			if (mGiftList != null && mGiftList.size() > 0) {
				return mGiftList.size();
			}
			return 0;
		}

		public Object getItem(int position) {
			if (mGiftList != null && mGiftList.size() > 0) {
				return mGiftList.get(position);
			}
			return null;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			GiftInfo giftInfo = mGiftList.get(position);

			if (convertView == null) {

				holder = new ViewHolder();
				Giftview_listitem_xml_po giftview_listitem_xml_po = new Giftview_listitem_xml_po(
						mActivity);

				convertView = giftview_listitem_xml_po.initViewxml();

				holder.gifttext1 = giftview_listitem_xml_po.getTv_gift1();
				holder.gifttext2 = giftview_listitem_xml_po.getTv_gift2();
				holder.gifttext3 = giftview_listitem_xml_po.getTv_gift3();

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (giftInfo.name.contains("请下载")) {
				String tempname = giftInfo.name.substring(0,
						(giftInfo.name.indexOf("请") - 1));
				holder.gifttext1.setText(tempname);
				String temptip = giftInfo.name.substring(
						(giftInfo.name.indexOf("请") - 1),
						(giftInfo.name.indexOf("取") + 2));
				holder.gifttext2.setText(temptip);
			} else {
				holder.gifttext1.setText(giftInfo.name);
			}
			holder.gifttext3.setText(giftInfo.create_time.substring(0, 10));

			return convertView;
		}
	}

	/**
	 * 显示礼包cdkeydialog
	 */
	private void showCdKeyDialog() {

		final Showgiftcode_dialog showgift_dialog1 = new Showgiftcode_dialog(
				mActivity);

		final EditText cdkey = showgift_dialog1.getTv_mDescription();
		cdkey.setText(mGift.cdkey);
		final ClipboardManager cmb = (ClipboardManager) mActivity
				.getSystemService(CLIPBOARD_SERVICE);

		showgift_dialog1.getBt_mCancel().setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						showgift_dialog1.dialogDismiss();
					}
				});
		showgift_dialog1.getBt_mOk().setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						Toast.makeText(mActivity, "长按激活码即可复制,或者用小本本记下",
								Toast.LENGTH_LONG).show();
					}
				});

		showgift_dialog1.dialogShow();

		/*
		 * // 显示礼包cdk的dialog AlertDialog.Builder builder = new
		 * AlertDialog.Builder(mContext); builder.setTitle("已领取激活码");
		 * 
		 * Giftcdkey_dialog_xml_ giftcdkey_dialog_xml_ = new
		 * Giftcdkey_dialog_xml_( mActivity); View giftcdkey =
		 * giftcdkey_dialog_xml_.initViewxml();
		 * 
		 * TextView get_time = giftcdkey_dialog_xml_.getTv_gift_gettime();
		 * 
		 * get_time.setText(mGift.get_time);
		 * 
		 * TextView release_time = giftcdkey_dialog_xml_.getTv_gift_cdkey();
		 * release_time.setText(mGift.release_time); final TextView cdkey =
		 * (TextView) giftcdkey_dialog_xml_ .getTv_gift_releasetime();
		 * cdkey.setText(mGift.cdkey);
		 * 
		 * builder.setView(giftcdkey); builder.setPositiveButton("复制", new
		 * OnClickListener() {
		 * 
		 * public void onClick(DialogInterface dialog, int which) { mCmb =
		 * (ClipboardManager) mActivity .getSystemService(CLIPBOARD_SERVICE);
		 * mCmb.setText(cdkey.getText().toString()); } });
		 * System.out.println("我是领取礼包的孩纸"); builder.setNegativeButton("取消",
		 * null); builder.create().show();
		 */
	}

	/**
	 * 显示礼包信息
	 * 
	 * @param giftInfo
	 */
	private void showGiftDialog(final GiftInfo giftInfo) {

		final Showgift_dialog showgift_dialog = new Showgift_dialog(mActivity);

		TextView description = showgift_dialog.getTv_mDescription();
		description.setText(giftInfo.description);

		TextView time = showgift_dialog.getTv_mTime();
		time.setText(giftInfo.create_time + "\n" + giftInfo.end_time);

		TextView howto = showgift_dialog.getTv_mHowto();
		howto.setText(giftInfo.howto);

		Button bt_mCancel = showgift_dialog.getBt_mCancel();
		bt_mCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showgift_dialog.dialogDismiss();
			}
		});

		Button bt_mOk = showgift_dialog.getBt_mOk();
		bt_mOk.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				new Thread() {

					@Override
					public void run() {
						try {
							// mGift = ObtainData.getGameGift(mContext,
							// AgentApp.mUser, giftInfo.gift_id);
							mHandler.sendEmptyMessage(GETGIFTSUCCESS);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}.start();
			}
		});
		showgift_dialog.dialogShow();
	}

}
