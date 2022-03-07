package cn.weli.mediaplayer.helper;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public abstract class DbOperateHelper<T> {

    public DbOperateHelper() {
        dbOperate();
    }

    private void dbOperate() {

        //被观察者
        Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(ObservableEmitter<T> emitter) throws Exception {
                emitter.onNext(doInBackground());
            }

        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.single())
                .subscribe(new Consumer<T>() {
                    @Override
                    public void accept(T t) throws Exception {     //接收到数据
                        dealResult(t);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {      //接收到异常
                        postException(throwable);
                    }
                }, new Action() {       //oncomplete
                    @Override
                    public void run() throws Exception {
                        onComplete();
                    }
                }, new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {        //订阅时
                        acceptDisposable(disposable);
                    }
                })
        ;
    }

    protected abstract T doInBackground();

    protected abstract void dealResult(T result);

    protected abstract void postException(Throwable t);

    protected abstract void onComplete();

    protected abstract void acceptDisposable(Disposable disposable);
}
