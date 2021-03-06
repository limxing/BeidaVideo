package me.leefeng.beida.main;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.litesuits.orm.LiteOrm;
import com.xiaomi.mipush.sdk.MiPushClient;

import me.leefeng.beida.Constants;
import me.leefeng.beida.ProjectApplication;
import me.leefeng.beida.bean.BuyData;
import me.leefeng.beida.bean.Course;
import me.leefeng.beida.bean.User;
import me.leefeng.beida.bean.Version;
import me.leefeng.beida.dbmodel.DBBuy;
import me.leefeng.beida.utils.HttpSend;
import me.leefeng.beida.utils.SecUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import me.leefeng.library.utils.IOUtils;
import me.leefeng.library.utils.LogUtils;
import me.leefeng.library.utils.SharedPreferencesUtil;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author FengTing
 * @date 2017/04/24 16:00:40
 */
public class MainPresenter implements MainPreInterface {
    private VideoAdapter adapter;
    private MainView mainView;
    private String sOrderId;
    private String sVacCode;
    private File couseFile;
    private MainApi mainApi;

    public MainPresenter(MainView mainView) {
        this.mainView = mainView;
        adapter = new VideoAdapter();
        mainView.setAdapter(adapter);
//        updateCourse();
//        initUpdata();

    }

    @Override
    public void destory() {
        mainView = null;
    }

    @Override
    public void pay(Course course) {
        mainView.showLoading("正在提交订单");
        initPay(course);
    }

    @Override
    public void login(String phone) {

        BmobQuery<User> query = new BmobQuery<>();
        query.addWhereEqualTo("phone", phone);
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null && list != null && list.size() > 0) {
                    ProjectApplication.user = list.get(0);
               ProjectApplication.setMiPush();
                    mainView.loginSuccess();
                } else {
                    e.printStackTrace();
                    mainView.loginFail();
                }
            }
        });
    }

    @Override
    public boolean isBuy(int position) {
        return adapter.isBuy(position);
    }

    /**
     * 条目的点击事件
     *
     * @param position
     */

    public void toBuy(int position) {
//                BuyData buyData=  new BuyData(adapter.getList().get(position).getId(), ProjectApplication.user.getPhone());
//                buyData.save();
//                adapter.notifyBuydata();
        if (ProjectApplication.user.getAccount() != null && ProjectApplication.user.getAccount() >= 1) {
            mainView.showGoldDialog(adapter.getList().get(position), 1);
        } else {
            pay(adapter.getList().get(position));
        }


    }


    /**
     * 登陆成功后，查询已购买列表
     */
    @Override
    public void getCourseList() {
        BmobQuery<BuyData> query = new BmobQuery<>();
        query.addWhereEqualTo("user", ProjectApplication.user.getObjectId());
        query.findObjects(new FindListener<BuyData>() {
            @Override
            public void done(List<BuyData> list, BmobException e) {
                if (e == null) {
                    LogUtils.i(list.toString());
                    ProjectApplication.buyList = list;
                    //缓存本地
                    ProjectApplication.liteOrm.deleteAll(DBBuy.class);
                    for (BuyData buyData : list) {
                        ProjectApplication.liteOrm.save(new DBBuy(ProjectApplication.user.getPhone(), buyData.getCourseid()));
                    }

                    //...
                    adapter.notifyBuydata();

                    mainView.stopFresh(true);
                } else {
                    e.printStackTrace();
                    switch (e.getErrorCode()) {
                        case 9016:
                            mainView.showErrorWithStatus("无法连接网络");
                            break;
                        default:
                            mainView.showErrorWithStatus("获取购买信息失败，请刷新列表");

                            break;
                    }
                    mainView.stopFresh(false);
                }

            }
        });
    }

    /**
     * 购买成功，添加数据库
     */
    @Override
    public void paySuccess(Course course) {
        BuyData buyData = new BuyData();
        buyData.setUser(ProjectApplication.user);
        buyData.setCourseid(course.getId());
        buyData.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    mainView.buySuccess();

                } else {
                    e.printStackTrace();
                    mainView.showErrorWithStatus("购买失败");
                }
            }
        });
    }

    /**
     * 用金币支付课程
     *
     * @param course
     * @param money
     */
    @Override
    public void buyWithGold(final Course course, int money) {
        final User user = ProjectApplication.user;
        Long count = user.getAccount() - money;
        user.setAccount(count);
        user.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    //添加购买了的列表
                    BuyData buyData = new BuyData();
                    buyData.setCourseid(course.getId());
                    buyData.setUser(user);
                    buyData.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null) {
                                login(user.getPhone());
                            } else {
                                e.printStackTrace();
                                mainView.showErrorWithStatus("支付失败，请重试");
                            }
                        }
                    });
                } else {
                    e.printStackTrace();
                    mainView.showErrorWithStatus("支付失败，请重试");
                }
            }
        });
    }

    private void initPay(final Course course) {
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                String ss = null;
                try {
                    ss = getCode();
                    if (ss != null) {
                        JSONObject json = null;
                        json = new JSONObject(ss);
                        sOrderId = json.getString("orderid");
                        sVacCode = json.getString("vacCode");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                subscriber.onNext(ss);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        LogUtils.i("onError");
                        mainView.showErrorWithStatus("获取订单失败，请稍后再试");
                    }

                    @Override
                    public void onNext(String s) {
                        LogUtils.i("onNext:" + s);
                        if (sOrderId == null || sVacCode == null) {
                            mainView.showErrorWithStatus("获取订单失败，请稍后再试");
                        } else {
                            mainView.payView(sOrderId, sVacCode, course);
                        }
                    }
                });
    }


    public String getCode() throws JSONException, IOException {

        String url = "http://mobilepay.wocheng.tv:8090/wochengPay/wosdk/generateOrderId?channelCode=50021" +
                "&protocolVersion=1001";
        JSONObject json = new JSONObject();
        json.put("spcode", "1001");
        json.put("appid", "557950046");
        json.put("payid", "55795004601");
        json.put("mobile", "18686549472");
        json.put("price", 100);
        json.put("appname", "一元夺宝");
        json.put("payname", "1001");
        json.put("imsi", "1234654641");
        json.put("cpparam", "1001");
        String key = "074dd3792d3ca1a94fb9763a60e7cec8";
        String body = SecUtil.AESEncrypt(json.toString(), key);


        String ans = HttpSend.post(url, body);
        System.out.println("getCode::" + ans);
        return ans;
    }

    private final String course = "course.json";

    public void initUpdata() {
        couseFile = new File(((Activity) mainView).getCacheDir(), course);
//        try {
//            FileOutputStream out = new FileOutputStream(new File(Environment.getExternalStorageDirectory().getAbsoluteFile(), "course.json"));
//            FileInputStream in = new FileInputStream(couseFile);
//            int byteread = 0; // 读取的字节数
//            byte[] buffer = new byte[1024];
//            while ((byteread = in.read(buffer)) != -1) {
//                out.write(buffer, 0, byteread);
//            }
//            out.flush();
//            out.close();
//            in.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        mainApi = new Retrofit.Builder()
                .baseUrl("http://leefeng.me")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build().create(MainApi.class);
        if (couseFile.exists()) {
            updateCourse();
            mainApi.getVersion()
                    .subscribeOn(rx.schedulers.Schedulers.io())
                    .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                    .subscribe(new rx.Subscriber<Version>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(Version version) {
//                            version.setCourse(2);
//                            version.setTitle("课程有更新");
//                            version.setValue("添加了毛概");
//                            version.setUrl("http://www.leefeng.me/download/beidacourse1.1.apk");
                            LogUtils.i(version.toString());
                            try {
//                                if (version.getVersion() > ((Activity) mainView).getPackageManager().
//                                        getPackageInfo(((Activity) mainView).getPackageName(), 0).versionCode) {
////                                    mainView.updateApp(version);
//                                    return;
//                                }
                                if (JSON.parseObject(IOUtils.streamToString(new FileInputStream(couseFile)))
                                        .getIntValue("courseVersion") != version.getCourse())
//                                    mainView.updateDialog(version.getTitle(), version.getValue());
                                {
                                    LogUtils.i("版本不一致更新课程");
                                    upDateCouse();
                                }
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }

                        }
                    });

        } else {
            upDateCouse();
        }

    }

    /**
     * 联网获取课程json
     */
    public void upDateCouse() {
        mainView.showLoading("正在更新课程", false);
        mainApi.getCourse()
                .subscribeOn(rx.schedulers.Schedulers.io())
                .observeOn(rx.schedulers.Schedulers.io())
                .map(new Func1<ResponseBody, Integer>() {
                    @Override
                    public Integer call(ResponseBody responseBody) {
                        int i = 0;
                        try {
                            FileOutputStream fout = new FileOutputStream(couseFile);
//                                openFileOutput(couseFile.toString(), MODE_PRIVATE);
                            byte[] bytes = responseBody.bytes();
                            fout.write(bytes);
                            fout.close();
                            i = 1;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return new Integer(i);
                    }
                })
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe(new rx.Observer<Integer>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mainView.showErrorWithStatus("更新失败，请稍后重试");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        if (integer.intValue() == 1) {
                            mainView.updateCourseSuccess();
                            updateCourse();
                        } else {
                            mainView.showErrorWithStatus("更新失败，请稍后重试");
                        }
                    }
                });

    }

    /**
     * w为界面更新课程
     */
    private void updateCourse() {
        rx.Observable.create(new rx.Observable.OnSubscribe<List<Course>>() {
            @Override
            public void call(rx.Subscriber<? super List<Course>> subscriber) {
                try {
                    subscriber.onNext(JSON.parseArray(JSON.parseObject(IOUtils.streamToString(new FileInputStream(
                            new File(((Activity) mainView).getCacheDir(), "course.json")))).getString("courses"), Course.class)
                    );
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
        }).observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe(new rx.Subscriber<List<Course>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<Course> courses) {
                        ProjectApplication.cList = courses;
                        adapter.setList(courses);
                        adapter.notifyDataSetChanged();
                        mainView.stopFresh(true);
                    }
                });

    }


}
