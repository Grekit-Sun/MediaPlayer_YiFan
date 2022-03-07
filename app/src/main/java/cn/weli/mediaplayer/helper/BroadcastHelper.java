package cn.weli.mediaplayer.helper;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import static cn.weli.mediaplayer.MediaApplication.appCtx;

public class BroadcastHelper {
    /**
     * 注册接收者
     *
     * @param receiver
     * @param actions
     */
    public static void registReceiver(BroadcastReceiver receiver, String... actions) {
        IntentFilter filter = new IntentFilter();
        for (String action : actions) {
            filter.addAction(action);
        }
        appCtx.registerReceiver(receiver, filter);
    }

    /**
     * 发送本地广播
     *
     * @param action
     */
    public static void senLocalBroadcast(String action) {
        LocalBroadcastManager.getInstance(appCtx).sendBroadcast(new Intent(action));
    }

    /**
     * 注册本地广播
     * @param mReceiver
     * @param actions
     */
    public static void registerLocalBroadcast(BroadcastReceiver mReceiver, String... actions) {
        IntentFilter filter = new IntentFilter();
        for (String action : actions) {
            filter.addAction(action);
        }
        LocalBroadcastManager.getInstance(appCtx).registerReceiver(mReceiver, filter);
    }

    /**
     * 注销广播
     * @param mReceiver
     */
    public static void unRegisterLocalBroadcast(BroadcastReceiver mReceiver) {
        LocalBroadcastManager.getInstance(appCtx).unregisterReceiver(mReceiver);
    }

}
