package com.kkgame.sdk.login;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kkgame.sdk.db.UserDao;
import com.kkgame.sdk.utils.Basedialogview;
import com.kkgame.sdk.utils.LoginUtils;
import com.kkgame.sdk.xml.GetAssetsutils;
import com.kkgame.sdk.xml.Loginpo_listviewitem;
import com.kkgame.sdk.xml.MachineFactory;
import com.kkgame.sdkmain.AgentApp;
import com.kkgame.utils.DeviceUtil;

public class Login_ho_dialog extends Basedialogview {

	private LinearLayout ll_mUser;
	private ImageView iv_mUn_icon;
	private EditText et_mUn;
	private LinearLayout ll_mDown;
	private ImageView iv_mUn_down;
	private LinearLayout ll_mPassword;
	private ImageView iv_mPs_icon;
	private EditText et_mPs;
	private LinearLayout ll_mBut;
	private Button bt_mPhonelogin;
	private Button bt_mlogin;
	private ArrayList<String> mNames;
	private String mSelectUser;
	private String mPassword;
	private ListView lv_mHistoryuser;
	private String mName;
	protected static final int ERROR = 11;

	private TextView tv_fogetpassword;
	private RelativeLayout rl_fogetpassword;
	private Login_ho_dialog login_ho_dialog;

	@SuppressWarnings("deprecation")
	@Override
	public void createDialog(final Activity mActivity) {

		dialog = new Dialog(mActivity);

		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		int ho_height = 650;
		int ho_with = 750;
		int po_height = 650;
		int po_with = 650;

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

		baselin = new LinearLayout(mActivity);
		baselin.setOrientation(LinearLayout.VERTICAL);
		MachineFactory machineFactory = new MachineFactory(mActivity);
		machineFactory
				.MachineView(baselin, with, height, "LinearLayout", 2, 50);
		baselin.setBackgroundColor(Color.TRANSPARENT);
		baselin.setGravity(Gravity.CENTER_VERTICAL);

		// 中间内容
		RelativeLayout rl_content = new RelativeLayout(mActivity);
		machineFactory.MachineView(rl_content, with, height, mLinearLayout, 2,
				25);
		rl_content.setBackgroundColor(Color.WHITE);

		// 中间内容
		LinearLayout ll_content = new LinearLayout(mActivity);
		machineFactory.MachineView(ll_content, with, height, "LinearLayout");
		ll_content.setBackgroundColor(Color.WHITE);
		ll_content.setGravity(Gravity.CENTER_HORIZONTAL);
		ll_content.setOrientation(LinearLayout.VERTICAL);

		// 头部
		LinearLayout ll_title = new LinearLayout(mActivity);
		machineFactory.MachineView(ll_title, MATCH_PARENT, 130, mLinearLayout);
		ll_title.setGravity(Gravity_CENTER);
		ll_title.setOrientation(LinearLayout.VERTICAL);
		ll_title.setBackgroundColor(Color.parseColor("#f1f1f1"));

		// 头部icon
		ImageView iv_icon = new ImageView(mActivity);
		machineFactory.MachineView(iv_icon, WRAP_CONTENT, WRAP_CONTENT,
				mLinearLayout);
		iv_icon.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"yaya_logo_ho1.png", mActivity));
		// TODO
		ll_title.addView(iv_icon);

		// 设置username的ll 注意背景的兼容性问题
		ll_mUser = new LinearLayout(mActivity);
		ll_mUser = (LinearLayout) machineFactory.MachineView(ll_mUser,
				MATCH_PARENT, 100, 0, "LinearLayout", 20, 20, 20, 0, 100);

		ll_mUser.setBackgroundDrawable(GetAssetsutils
				.get9DrawableFromAssetsFile("yaya_biankuang2.9.png", mActivity));

		ll_mUser.setGravity(Gravity.CENTER);

		// username 的icon
		iv_mUn_icon = new ImageView(mActivity);
		iv_mUn_icon = (ImageView) machineFactory.MachineView(iv_mUn_icon, 40,
				40, 0, mLinearLayout, 20, 0, 0, 0, 100);
		iv_mUn_icon.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"yaya_username.png", mActivity));

		// username的edtext
		et_mUn = new EditText(mActivity);
		et_mUn = machineFactory.MachineEditText(et_mUn, 0, MATCH_PARENT, 1,
				"请输入用户名", 28, mLinearLayout, 0, 6, 0, 0);
		et_mUn.setTextColor(Color.BLACK);
		et_mUn.setBackgroundColor(Color.TRANSPARENT);

		ll_mDown = new LinearLayout(mActivity);
		machineFactory.MachineView(ll_mDown, 60, MATCH_PARENT, mLinearLayout,
				3, 10);
		ll_mDown.setGravity(Gravity_CENTER);
		ll_mDown.setClickable(true);

		// username的下拉图片
		iv_mUn_down = new ImageView(mActivity);
		iv_mUn_down = (ImageView) machineFactory.MachineView(iv_mUn_down, 40,
				40, 0, mLinearLayout, 0, 0, 0, 0, 100);
		iv_mUn_down.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"yaya_down.png", mActivity));
		iv_mUn_down.setClickable(false);

		ll_mDown.addView(iv_mUn_down);

		// TODO
		ll_mUser.addView(iv_mUn_icon);
		ll_mUser.addView(et_mUn);
		ll_mUser.addView(ll_mDown);

		// 设置password的ll 注意背景的兼容性问题
		ll_mPassword = new LinearLayout(mActivity);
		ll_mPassword = (LinearLayout) machineFactory.MachineView(ll_mPassword,
				MATCH_PARENT, 100, 0, "LinearLayout", 20, 20, 20, 0, 100);

		ll_mPassword
				.setBackgroundDrawable(GetAssetsutils
						.get9DrawableFromAssetsFile("yaya_biankuang2.9.png",
								mActivity));

		ll_mPassword.setGravity(Gravity.CENTER);

		// password 的icon
		iv_mPs_icon = new ImageView(mActivity);
		iv_mPs_icon = (ImageView) machineFactory.MachineView(iv_mPs_icon, 40,
				40, 0, mLinearLayout, 20, 0, 0, 0, 100);
		iv_mPs_icon.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"yaya_password.png", mActivity));

		// password的edtext
		et_mPs = new EditText(mActivity);
		et_mPs = machineFactory.MachineEditText(et_mPs, 0, MATCH_PARENT, 1,
				"请输入密码", 28, mLinearLayout, 0, 6, 0, 0);
		et_mPs.setBackgroundColor(Color.TRANSPARENT);
		et_mPs.setTextColor(Color.BLACK);
		et_mPs.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_VARIATION_PASSWORD);

		// TODO
		ll_mPassword.addView(iv_mPs_icon);
		ll_mPassword.addView(et_mPs);

		// but 的lin

		ll_mBut = new LinearLayout(mActivity);
		ll_mBut = (LinearLayout) machineFactory.MachineView(ll_mBut,
				MATCH_PARENT, 100, 0, mLinearLayout, 20, 20, 20, 0, 100);

		// 横版手机登录按钮
		bt_mPhonelogin = new Button(mActivity);
		bt_mPhonelogin = machineFactory.MachineButton(bt_mPhonelogin, 0,
				MATCH_PARENT, 1, "快速注册", 30, mLinearLayout, 0, 0, 0, 0);
		bt_mPhonelogin.setTextColor(Color.WHITE);
		bt_mPhonelogin.setBackgroundDrawable(GetAssetsutils.crSelectordraw(
				"yaya_bulebutton.9.png", "yaya_bulebutton1.9.png", mActivity));
		bt_mPhonelogin.setGravity(Gravity_CENTER);

		LinearLayout ll_zhanwei = new LinearLayout(mActivity);
		ll_zhanwei = (LinearLayout) machineFactory.MachineView(ll_zhanwei, 20,
				MATCH_PARENT, mLinearLayout);

		// button的登录按钮
		bt_mlogin = new Button(mActivity);
		bt_mlogin = machineFactory.MachineButton(bt_mlogin, 0, MATCH_PARENT, 1,
				"立即登录", 30, mLinearLayout, 0, 0, 0, 0);
		bt_mlogin.setTextColor(Color.WHITE);
		bt_mlogin.setBackgroundDrawable(GetAssetsutils.crSelectordraw(
				"yaya_yellowbutton.9.png", "yaya_yellowbutton1.9.png",
				mActivity));
		bt_mlogin.setGravity(Gravity_CENTER);

		// TODO
		ll_mBut.addView(bt_mPhonelogin);
		ll_mBut.addView(ll_zhanwei);
		ll_mBut.addView(bt_mlogin);

		// 忘记密码列
		rl_fogetpassword = new RelativeLayout(mActivity);
		machineFactory.MachineView(rl_fogetpassword, MATCH_PARENT,
				WRAP_CONTENT, 0, mLinearLayout, 20, 15, 20, 0, 100);
		tv_fogetpassword = new TextView(mActivity);
		machineFactory.MachineTextView(tv_fogetpassword, WRAP_CONTENT,
				WRAP_CONTENT, 0, "忘记密码?", 22, mRelativeLayout, 0, 0, 20, 0,
				RelativeLayout.ALIGN_PARENT_RIGHT);
		tv_fogetpassword.setTextColor(Color.RED);

		rl_fogetpassword.addView(tv_fogetpassword);

		RelativeLayout rl_register = new RelativeLayout(mActivity);
		machineFactory.MachineView(rl_register, MATCH_PARENT, MATCH_PARENT, 0,
				mLinearLayout, 20, 15, 20, 0, 100);

		// 免密登录

		ll_content.addView(ll_title);
		ll_content.addView(ll_mUser);
		ll_content.addView(ll_mPassword);
		ll_content.addView(ll_mBut);
		ll_content.addView(rl_fogetpassword);
		ll_content.addView(rl_register);

		// 下拉选择历史账户
		lv_mHistoryuser = new ListView(mActivity);
		machineFactory.MachineView(lv_mHistoryuser, 700, WRAP_CONTENT, 0,
				"RelativeLayout", 20, 240, 20, 0,
				RelativeLayout.CENTER_HORIZONTAL);
		lv_mHistoryuser.setVisibility(View.GONE);

		rl_content.addView(ll_content);
		rl_content.addView(lv_mHistoryuser);
		// ll_content.addView(chongzhihelp2);

		baselin.addView(rl_content);

		dialog.setContentView(baselin);

		Window dialogWindow = dialog.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		dialogWindow.setGravity(Gravity.CENTER);

		lp.alpha = 0.9f; // 透明度

		lp.dimAmount = 0.5f; // 设置背景色对比度
		dialogWindow.setAttributes(lp);
		dialog.setCanceledOnTouchOutside(false);

		dialog.setCanceledOnTouchOutside(true);
		dialog.getWindow().setBackgroundDrawable(new BitmapDrawable());

		initlogic();
	}

	@Override
	public void dialogShow() {

		super.dialogShow();

	}

	/**
	 * 界面逻辑
	 */
	private void initlogic() {

		login_ho_dialog = this;

		dialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {

				if (AgentApp.mUser == null) {
					if (ViewConstants.TEMPLOGIN_HO != null) {
						return;
					}
					onCancel();
				}
			}
		});

		mNames = new ArrayList<String>();
		initDBData();
		// mNames

		// 忘记密码重设秘密
		rl_fogetpassword.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ResetPassword_ho_dialog resetPassword_ho_dialog = new ResetPassword_ho_dialog(
						mActivity);
				resetPassword_ho_dialog.dialogShow();

			}
		});

		// 下拉点击
		ll_mDown.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (View.GONE == lv_mHistoryuser.getVisibility()) {
					iv_mUn_down.setImageBitmap(GetAssetsutils
							.getImageFromAssetsFile("yaya_up.png", mActivity));

					lv_mHistoryuser.setVisibility(View.VISIBLE);
				} else {
					iv_mUn_down.setImageBitmap(GetAssetsutils
							.getImageFromAssetsFile("yaya_down.png", mActivity));
					lv_mHistoryuser.setVisibility(View.GONE);
				}

			}
		});

		// 登陆
		bt_mlogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 获取用户名密码,实现登陆
				mName = et_mUn.getText().toString().trim();
				mPassword = et_mPs.getText().toString().trim();
				if (mName.equals("")) {
					Toast.makeText(mActivity, "用户名不能为空", Toast.LENGTH_SHORT)
							.show();
				} else if (mName.length() < 4) {
					Toast.makeText(mActivity, "用户名不能小于4位", Toast.LENGTH_SHORT)
							.show();
				} else if (mName.length() > 20) {
					Toast.makeText(mActivity, "用户名不能大于20位", Toast.LENGTH_SHORT)
							.show();
				} else {
					// 输入的用户名和密码符合要求
					// TODO
					if (TextUtils.isEmpty(mPassword)) {
						Toast.makeText(mActivity, "密码不能为空,如忘记密码,请点击忘记密码~",
								Toast.LENGTH_SHORT).show();
						return;
					}
					

					LoginUtils loginUtils = new LoginUtils(mActivity,
							login_ho_dialog, 0);
					loginUtils.login(mName, mPassword);

				}
			}
		});

		// 注册
		bt_mPhonelogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Register_ho_dialog register_ho_dialog = new Register_ho_dialog(
						mActivity);
				register_ho_dialog.dialogShow();

				/**/
			}
		});

	}

	/**
	 * 加载数据库历史用户记录
	 */
	private void initDBData() {
		// 每次从数据库获取数据时都清空下列表,否则会有很多重复的数据
		if (mNames != null && mNames.size() > 0) {
			mNames.clear();
		}
		mNames = UserDao.getInstance(mActivity).getUsers();

		// System.out.println("wuuuuuuuuuuuuu"+mNames.size());
		// 默认选择列表中的第一项进行输入框填充
		if (mNames != null && mNames.size() > 0) {

			mSelectUser = mNames.get(0);
			mPassword = UserDao.getInstance(mActivity).getPassword(mSelectUser);

			String secret = UserDao.getInstance(mActivity).getSecret(
					mSelectUser);

			et_mUn.setText(mSelectUser);

			// 给一个填充密码
			if (TextUtils.isEmpty(mPassword) && !TextUtils.isEmpty(secret)) {
				et_mPs.setText("yayawan-zhang");
			} else {
				et_mPs.setText(mPassword);
			}
			// et_mPs.setText(mPassword);

		}

		UserListAdapter_jf userListAdapter = new UserListAdapter_jf(mActivity,
				mNames);
		lv_mHistoryuser.setAdapter(userListAdapter);

		if (mNames.size() < 4) {
			// lv_mHistoryuser.getLayoutParams().height = mNames.size() * 55;
			if (mNames.size() == 0) {
				et_mUn.setText("");
				et_mPs.setText("");
			}
			setListviewheight(mNames.size());
		} else {
			setListviewheight(4);
		}

		lv_mHistoryuser.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				// Log.e("正在点击我哦", "+++++++++++");
				mSelectUser = mNames.get(position);
				mPassword = UserDao.getInstance(mActivity).getPassword(
						mSelectUser);
				String secret = UserDao.getInstance(mActivity).getSecret(
						mSelectUser);

				et_mUn.setText(mSelectUser);
				// 给一个填充密码
				if (TextUtils.isEmpty(mPassword) && !TextUtils.isEmpty(secret)) {

					et_mPs.setText("yayawan-zhang");

				} else {

					et_mPs.setText(mPassword);
				}

				lv_mHistoryuser.setVisibility(View.GONE);
			}
		});

	}

	public void setListviewheight(int size) {
		lv_mHistoryuser.getLayoutParams().height = machSize((size * 100));
	}

	public class UserListAdapter_jf extends BaseAdapter {

		private ArrayList<String> mNames;

		private Context mActivity;

		class ViewHolder {

			TextView mName;
			ImageView mDelete;
		}

		public UserListAdapter_jf(Context context, ArrayList<String> names) {
			super();
			this.mActivity = context;
			this.mNames = names;
		}

		public int getCount() {
			return mNames.size();
		}

		public Object getItem(int position) {
			return mNames.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				Loginpo_listviewitem loginpo_listviewitem = new Loginpo_listviewitem(
						mActivity);
				convertView = loginpo_listviewitem.initViewxml();
				holder.mName = (TextView) loginpo_listviewitem.getTextView();
				holder.mDelete = (ImageView) loginpo_listviewitem
						.getImageView();
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			final String name = mNames.get(position);
			holder.mName.setText(name);

			holder.mDelete.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					mNames.remove(name);
					UserDao.getInstance(mActivity).removeUser(name);
					UserListAdapter_jf.this.notifyDataSetChanged();
					if (mNames.size() == 0) {
						et_mUn.setText("");
						et_mPs.setText("");
						lv_mHistoryuser.setVisibility(View.GONE);
					}
				}
			});

			return convertView;
		}

	}

	public Login_ho_dialog(Activity activity) {
		super(activity);
	}

	/**
	 * 查询数据库获取第一个有secret的账号
	 */
	public String[] getFirstuserSecreat() {

		ArrayList<String> users = UserDao.getInstance(mContext).getUsers();

		for (int i = 0; i < users.size(); i++) {
			String secret = UserDao.getInstance(mContext).getSecret(
					users.get(i));
			if (!TextUtils.isEmpty(secret)) {

				return new String[] { secret, users.get(i) };
			}
		}
		return null;
	}

}
