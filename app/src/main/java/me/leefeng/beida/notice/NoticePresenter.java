package me.leefeng.beida.notice;

import com.litesuits.orm.db.assit.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

import me.leefeng.beida.ProjectApplication;
import me.leefeng.beida.dbmodel.NoticeMessage;
import me.leefeng.library.utils.LogUtils;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author FengTing
 * @date 2017/05/17 16:39:04
 */
public class NoticePresenter implements NoticePreInterface {
    private NoticeAdapter adapter;
    private NoticeView noticeView;
    private List<NoticeMessage> list;

    public NoticePresenter(NoticeView noticeView) {
        this.noticeView = noticeView;
        list = new ArrayList<>();
        adapter = new NoticeAdapter(list);
        noticeView.setAdapter(adapter);

    }

    @Override
    public void destory() {
        noticeView = null;
    }

    @Override
    public void initData() {
        Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                QueryBuilder builder = new QueryBuilder<>(NoticeMessage.class);
                builder.appendOrderDescBy("time");
                list = ProjectApplication.liteOrm.query(builder);
                subscriber.onNext(true);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        LogUtils.i("list:" + list.size());
                        if (list.size() == 0) {
                            noticeView.initSuccessNone();
                        } else {
                            noticeView.initSuccess();
                            adapter.notifyDataSetChanged();
                        }

                    }
                });

    }
}
