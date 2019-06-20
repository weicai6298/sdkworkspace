package com.yayawan.main;

import com.yayawan.callback.YYWAnimCallBack;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.callback.YYWPayCallBack;
import com.yayawan.callback.YYWUserCallBack;
import com.yayawan.callback.YYWUserManagerCallBack;
import com.yayawan.domain.YYWOrder;
import com.yayawan.domain.YYWRole;
import com.yayawan.domain.YYWUser;

public class YYWMain {

    public static YYWAnimCallBack mAnimCallBack;

    public static YYWUserCallBack mUserCallBack;

    public static YYWUserManagerCallBack mUserManagerCallBack;

    public static YYWPayCallBack mPayCallBack;

    public static YYWExitCallback mExitCallback;

    public static YYWUser mUser;

    public static YYWOrder mOrder;

    public static YYWRole mRole;

    private static final YYWMain mInstance = new YYWMain();

	/**
	 * @param args
	 */
	public static YYWMain getInstance() {
		// TODO Auto-generated method stub
		return mInstance;
	}

}
