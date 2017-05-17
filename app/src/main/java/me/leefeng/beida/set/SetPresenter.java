package me.leefeng.beida.set;

/**
 * @author FengTing
 * @date 2017/05/17 16:39:26
 */
public class SetPresenter implements SetPreInterface {
    private SetView setView;

    public SetPresenter(SetView setView) {
        this.setView = setView;
    }

    @Override
    public void destory() {
        setView = null;
    }
}
