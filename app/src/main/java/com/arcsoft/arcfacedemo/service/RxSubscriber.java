package com.arcsoft.arcfacedemo.service;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


/**
 * Created by linxiao on 2019/3/5.
 */

public abstract class RxSubscriber<T> implements Observer<T> {

    private String msg;

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(T t) {
        _onNext(t);
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        if (e instanceof UnknownHostException) {
            msg = "网络错误...";
        } else if (e instanceof SocketTimeoutException) {
            // 超时
            msg = "超时...";
        }else{
            msg = "请求失败，请稍后重试...";
        }
        _onError(msg);
    }

    @Override
    public void onComplete() {
//        ToastUtil.initToast("onComplete");
    }


    public abstract void _onNext(T t);

    public abstract void _onError(String msg);
}
