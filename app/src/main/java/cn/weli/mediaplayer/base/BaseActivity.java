package cn.weli.mediaplayer.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.lang.reflect.Constructor;

import butterknife.ButterKnife;
import cn.weli.mediaplayer.helper.WeakHandlerHelper;

public abstract class BaseActivity<T extends IPresenter, K> extends AppCompatActivity {

    private boolean needSetOrientation = true;
    protected T mPresenter;
    protected WeakHandlerHelper mHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //绑定布局
        setContentView(getLayout());
        initPresenter();
    }


    protected abstract int getLayout();

    protected abstract void bindView();

    /**
     * 返回View层的接口类.
     */
    protected abstract Class<K> getViewClass();

    /**
     * 返回逻辑处理的具体类型.
     */
    protected abstract Class<T> getPresenterClass();

    /**
     * 初始化Presenter
     */
    protected void initPresenter() {
        try {
            Constructor constructor = getPresenterClass().getConstructor(getViewClass());
            mPresenter = (T) constructor.newInstance(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 延时处理
     * @param runnable
     * @param delayMillis
     */
    public void handleEventDelay(Runnable runnable, long delayMillis) {
        if (mHandler == null) {
            mHandler = new WeakHandlerHelper();
        }
        mHandler.postDelayed(runnable, delayMillis);
    }

    /**
     * 关闭当前页面
     */
    public void finishActivity() {
        this.finish();
    }


}
