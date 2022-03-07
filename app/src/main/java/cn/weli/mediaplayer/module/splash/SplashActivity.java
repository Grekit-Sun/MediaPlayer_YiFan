package cn.weli.mediaplayer.module.splash;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.widget.TextView;

import com.yanzhenjie.permission.Action;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.weli.mediaplayer.R;
import cn.weli.mediaplayer.base.BaseActivity;
import cn.weli.mediaplayer.helper.PermissionHelper;
import cn.weli.mediaplayer.module.main.MainActivity;
import cn.weli.mediaplayer.service.MediaService;


public class SplashActivity extends BaseActivity<SplashPresenter, ISplashView> implements ISplashView {

    public static final String FONTS = "fonts/yizhi.ttf";

    @BindView(R.id.appname_txt)
    TextView mAppNameTxt;
    @BindView(R.id.slogan_txt)
    TextView mSloganTxt;
    @BindView(R.id.bottom_appname_txt)
    TextView mBottomAppNameTxt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindView();
        initView();     //初始化控件
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_splash;
    }

    @Override
    protected void bindView() {
        ButterKnife.bind(this);
    }

    @Override
    protected Class getViewClass() {
        return ISplashView.class;
    }

    @Override
    protected Class getPresenterClass() {
        return SplashPresenter.class;
    }

    protected void initView() {
        //设置自定义字体
        Typeface typeface = Typeface.createFromAsset(getAssets(), FONTS);
        mAppNameTxt.setTypeface(typeface);
        mSloganTxt.setTypeface(typeface);
        mBottomAppNameTxt.setTypeface(typeface);
    }

    @Override
    public void startToMainNow() {
        startToMain();
    }

    /**
     * 跳转至主页
     */
    public void startToMain() {
        initPremission();
        Intent intent = new Intent(this, MediaService.class);
        startService(intent);
    }


    /**
     * 初始化权限
     */
    private void initPremission() {
        PermissionHelper.requestMultiPermission(this, new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                        delyToMain();
                    }
                }, new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                        delyToMain();
                    }
                }, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    /**
     * 延时跳转
     */
    private void delyToMain() {
        handleEventDelay(new Runnable() {
            @Override
            public void run() {
                skipToMainActivity();
            }
        }, FLAG_MAIN_DELAY);
    }

    /**
     * 跳至主界面
     */
    private void skipToMainActivity() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            mPresenter.initMusic();
            //跳入主界面
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finishActivity();
        }
    }
}