package me.leefeng.beida.today;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import me.leefeng.beida.BasePresenter;
import me.leefeng.library.utils.LogUtils;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by limxing on 2017/5/20.
 */

public class TodayPresenter extends BasePresenter implements TodayPresenterInter {
    private TodayView todayView;

    public TodayPresenter(TodayView toadyActivity) {
        this.todayView = toadyActivity;

    }

    public void getData(int i) {

        final String pathUrl = "http://www.pkudl.cn/xwzx.asp?aid=449&act=" + i;
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {

                    URL url = new URL(pathUrl);
                    HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
// //设置连接属性
                    httpConn.setDoOutput(true);// 使用 URL 连接进行输出
                    httpConn.setDoInput(true);// 使用 URL 连接进行输入
                    httpConn.setUseCaches(false);// 忽略缓存
                    httpConn.setConnectTimeout(3000);
                    httpConn.setRequestMethod("GET");// 设置URL请求方法
                    int responseCode = httpConn.getResponseCode();
                    if (responseCode == 200) {

                        StringBuffer sb = new StringBuffer();
                        String readLine;
                        BufferedReader responseReader;
// 处理响应流，必须与服务器响应流输出的编码一致
                        responseReader = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), "utf-8"));
                        while ((readLine = responseReader.readLine()) != null) {
                            sb.append(readLine).append("\n");
                        }
                        responseReader.close();
                        int start = sb.indexOf("<table width=\"100%\" border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"2\">");
                        sb.delete(0, start);
                        int end = sb.indexOf("</table>");

                        String s = sb.substring(0, end);

                        s = "<!doctype html><html lang=\"en\"><head><meta charset=\"UTF-8\"></head><body>" + s + "</table></body></html>";

                        subscriber.onNext(s);
                    }else{
                        LogUtils.i("code"+responseCode);
                        subscriber.onError(new Throwable(responseCode+""));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    subscriber.onError(new Throwable());
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        if (todayView==null)return;
                        todayView.showError("获取失败，请重试");
                    }

                    @Override
                    public void onNext(String s) {
                        if (todayView==null)return;
                        todayView.setWebViewData(s);
                    }
                });

    }

    @Override
    public void destory() {
        todayView = null;
    }
}
