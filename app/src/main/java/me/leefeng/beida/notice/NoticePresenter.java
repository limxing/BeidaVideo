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
        Observable.create(new Observable.OnSubscribe<List<NoticeMessage>>() {
            @Override
            public void call(Subscriber<? super List<NoticeMessage>> subscriber) {
                QueryBuilder builder = new QueryBuilder<>(NoticeMessage.class);
                builder.appendOrderDescBy("time");
               List<NoticeMessage> list = ProjectApplication.liteOrm.query(builder);
                subscriber.onNext(list);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<NoticeMessage>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }

                    @Override
                    public void onNext(List<NoticeMessage> l) {
                        LogUtils.i("list:" + l.size());
                        list.clear();
                        list.addAll(l);
                        if (list.size() == 0) {
                            noticeView.initSuccessNone();
                        } else {
                            noticeView.initSuccess();
                            adapter.notifyDataSetChanged();
                        }

                    }
                });

    }

    @Override
    public List<NoticeMessage> getList() {
        return list;
    }

    @Override
    public void setPositionRead(int position) {
        list.get(position).setRead(true);
        ProjectApplication.liteOrm.update(list.get(position));
        adapter.notifyItemChanged(position);
    }


}
