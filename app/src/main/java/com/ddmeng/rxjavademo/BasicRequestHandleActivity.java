package com.ddmeng.rxjavademo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.ddmeng.rxjavademo.network.MockFetcher;

import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;

import static rx.schedulers.Schedulers.io;

public class BasicRequestHandleActivity extends AppCompatActivity {
    public static final String TAG = BasicRequestHandleActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_request_handle);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.button_send_one)
    void onSendOneClick() {
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                Log.i(TAG, "call");
                try {
                    String output = MockFetcher.getString("Hello, Input");
                    subscriber.onNext(output);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {
                Log.i(TAG, "onCompleted()");
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError()");
            }

            @Override
            public void onNext(String response) {
                Log.i(TAG, "onNext(): " + response);
            }
        });

    }


    @OnClick(R.id.button_send_multiple)
    void onSendMultipleClick() {
        String[] urls = new String[]{"url1", "url2", "url3"};
        Observable.from(urls)
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(final String url) {
                        return Observable.create(new Observable.OnSubscribe<String>() {
                            @Override
                            public void call(Subscriber<? super String> subscriber) {
                                try {
                                    Log.i(TAG, "sending request to " + url);
                                    String response = MockFetcher.getString(url);
                                    subscriber.onNext(response);
                                    subscriber.onCompleted();
                                } catch (Exception e) {
                                    Log.i(TAG, "Error occurs when sending request to " + url + " exception: ", e);
                                    subscriber.onError(e);
                                }
                            }
                        }).subscribeOn(io());
                    }
                }).subscribeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                Log.i(TAG, "onCompleted()");
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError()");
            }

            @Override
            public void onNext(String response) {
                Log.i(TAG, "onNext(): " + response);
            }
        });
    }
}
