package com.yayawan.proxy;

import com.yayawan.impl.ActivityStubImpl;
import com.yayawan.impl.AnimationImpl;
import com.yayawan.impl.ChargerImpl;
import com.yayawan.impl.LoginImpl;
import com.yayawan.impl.UserManagerImpl;



public class GameProxy extends CommonGameProxy {

    private static GameProxy proxy;

    private GameProxy(YYWLoginer login, YYWActivityStub stub, YYWUserManager userManager, YYWCharger charger) {
        super(login, stub, userManager, charger);
    }

    public static CommonGameProxy getInstent() {

        if (proxy == null) {
            proxy = new GameProxy(new LoginImpl(), new ActivityStubImpl(), new UserManagerImpl(), new ChargerImpl());
            proxy.setAnimation(new AnimationImpl());
        }

        return proxy;
    }

}
