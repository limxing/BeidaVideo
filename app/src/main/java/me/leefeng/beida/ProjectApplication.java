package me.leefeng.beida;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;


import com.litesuits.orm.LiteOrm;
import com.qq.e.ads.cfg.MultiProcessFlag;
import com.tencent.bugly.crashreport.CrashReport;
import com.xiaomi.channel.commonutils.logger.LoggerInterface;
import com.xiaomi.mipush.sdk.Logger;
import com.xiaomi.mipush.sdk.MiPushClient;

import me.leefeng.beida.bean.BuyData;
import me.leefeng.beida.bean.Course;
import me.leefeng.beida.bean.PayType;
import me.leefeng.beida.bean.User;
import me.leefeng.beida.download.DownLoadService;

import android.os.Handler;
import android.os.Message;
import android.os.Process;
//import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.smssdk.SMSSDK;
import me.leefeng.library.utils.LogUtils;
import me.leefeng.library.utils.SharedPreferencesUtil;

/**
 * @author FengTing
 * @date 2017/04/24 16:00:40
 */
public class ProjectApplication extends Application {
    public static User user;
    public static List<Course> cList;
    public static List<BuyData> buyList;
    public static List<PayType> payTypes;

//    public static ProjectApplication application;

    public static boolean isDebug = true;
//    public static Context attachContext;

    public static LiteOrm liteOrm;
    public static final String TAG = "com.xiaomi.mipushdemo";
    private static MiHandler sHandler;
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        if (!shouldInit()) {
            return;

        }
        LogUtils.isOnline(!isDebug);
        LogUtils.i("Application===onCreat");

//        mApplication.onCreate();
        startService(new Intent(this, DownLoadService.class));
        //后端云
        Bmob.initialize(this, "b8f0593656c47a6539e7f7253ecf4d37");
        //短信验证码
        SMSSDK.initSDK(this, "1de540bd8e8ec", "8c020708a2d87ef5d028192f2e054a69");//新北大
//        SMSSDK.initSDK(this, "189b617b10eb4", "bbdaa1208d663cbc3b8b4f628ef39fd8");//北大的

        CrashReport.initCrashReport(getApplicationContext(), "15da576048", isDebug);
//        if (isDebug)


        if (liteOrm == null) {
            liteOrm = LiteOrm.newSingleInstance(this, "liteorm.db");
        }
        liteOrm.setDebugged(isDebug); // open the log

        MultiProcessFlag.setMultiProcess(true);//广告

        MiPushClient.registerPush(this, "2882303761517577695", "5121757776695");//小米推送
        if (sHandler == null) {
            sHandler = new MiHandler(getApplicationContext());
        }
//        小米推送log
        LoggerInterface newLogger = new LoggerInterface() {
            @Override
            public void setTag(String tag) {
                // ignore
            }

            @Override
            public void log(String content, Throwable t) {
                Log.d(TAG, content, t);
            }

            @Override
            public void log(String content) {
                Log.d(TAG, content);
            }
        };
        Logger.setLogger(this, newLogger);
    }


    //解决多包共存
//    @Override
//    protected void attachBaseContext(Context base) {
//        super.attachBaseContext(base);
//        MultiDex.install(this);
//    }
    /**
     * 判断是否已经初始化
     *
     * @return
     */
    private boolean shouldInit() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }

    public static MiHandler getHandler() {
        return sHandler;
    }

    public static Context getContext() {
        return mContext;
    }

//    public static void setMainActivity(MainActivity activity) {
//        sMainActivity = activity;
//    }

    /**
     * 用于发送消息给UI具体的activity
     */
    public static class MiHandler extends Handler {

        private Context context;

        public MiHandler(Context context) {
            this.context = context;
        }

        @Override
        public void handleMessage(Message msg) {
            String s = (String) msg.obj;
            LogUtils.i("收到推送相关：" + s);
//            if (sMainActivity != null) {
//                sMainActivity.refreshLogInfo();
//            }
            if (!TextUtils.isEmpty(s)) {
//                Toast.makeText(context, s, Toast.LENGTH_LONG).show();
            }
        }
    }

    public static void setMiPush() {
        List<String> accounts = MiPushClient.getAllUserAccount(ProjectApplication.getContext());
        LogUtils.i("Login_accounts:" + accounts.toString());
        if (!accounts.contains(user.getObjectId()))
            MiPushClient.setUserAccount(ProjectApplication.getContext(), user.getObjectId(), null);//登陆时设置就行

//        accounts= MiPushClient.getAllTopic(getContext());
//        if (!accounts.contains("dayi"))
//            MiPushClient.su(ProjectApplication.getContext(), user.getObjectId(), null);
        accounts=MiPushClient.getAllAlias(getContext());
        LogUtils.i("Login_alias:" + accounts.toString());
        if (!accounts.contains(user.getObjectId())){
            MiPushClient.setAlias(getContext(),user.getObjectId(),null);
        }
    }
}
