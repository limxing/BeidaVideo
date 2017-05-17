package me.leefeng.beida.notice;

/**
 * @author FengTing
 * @date 2017/05/17 16:39:04
 */
public class NoticePresenter implements NoticePreInterface {
    private NoticeView noticeView;

    public NoticePresenter(NoticeView noticeView) {
        this.noticeView = noticeView;
    }

    @Override
    public void destory() {
        noticeView = null;
    }
}
