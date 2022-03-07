package cn.weli.mediaplayer.module.main.find.webview;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.weli.mediaplayer.R;
import cn.weli.mediaplayer.base.BaseActivity;


public class WebViewActivity extends BaseActivity<IWebViewPresenter, IWebView> implements IWebView {

    @BindView(R.id.wv_news)
    WebView mWebView;
    private String url;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindView();
        initView();
    }

    private void initView() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        url = extras.getString("url");

        //方式一：加载一个网页
        mWebView.loadUrl(url);

    }

    @Override
    protected int getLayout() {
        return R.layout.webview_layout;
    }

    @Override
    protected void bindView() {
        ButterKnife.bind(this);
    }

    @Override
    protected Class<IWebView> getViewClass() {
        return IWebView.class;
    }

    @Override
    protected Class<IWebViewPresenter> getPresenterClass() {
        return IWebViewPresenter.class;
    }
}
