package com.karoline.codelibrary;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.karoline.R;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

public class TestRxAndroidActivity extends AppCompatActivity {
    @BindView(R.id.text_testrx)
    AppCompatTextView mTvText;


    final String[] mManyWords = {"Hello", "I", "am", "your", "friend", "Spike"};
    final List<String> mManyWordList = Arrays.asList(mManyWords);

    // just: 获取输入数据, 直接分发, 更加简洁, 省略其他回调.
    // from: 获取输入数组, 转变单个元素分发.
    //map: 映射, 对输入数据进行转换, 如大写.
    //flatMap: 增大, 本意就是增肥, 把输入数组映射多个值, 依次分发.
    //reduce: 简化, 正好相反, 把多个数组的值, 组合成一个数据.


    // Action类似订阅者, 设置TextView
    private Action1<String> mTextViewAction = new Action1<String>() {
        @Override public void call(String s) {
            mTvText.setText(s);
        }
    };

    // Action设置Toast
    private Action1<String> mToastAction = new Action1<String>() {
        @Override public void call(String s) {
            Toast.makeText(TestRxAndroidActivity.this, s, Toast.LENGTH_SHORT).show();
        }
    };

    // 设置映射函数
    private Func1<List<String>, Observable<String>> mOneLetterFunc = new Func1<List<String>, Observable<String>>() {
        @Override public Observable<String> call(List<String> strings) {
            return Observable.from(strings); // 映射字符串
        }
    };

    // 设置大写字母
    private Func1<String, String> mUpperLetterFunc = new Func1<String, String>() {
        @Override public String call(String s) {
            return s.toUpperCase(); // 大小字母
        }
    };

    // 连接字符串
    private Func2<String, String, String> mMergeStringFunc = new Func2<String, String, String>() {
        @Override public String call(String s, String s2) {
            return String.format("%s %s", s, s2); // 空格连接字符串
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_rx_android);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("TestRxAndroidActivity");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);

        testRxKinds();
        testRx();
    }

    private void testRxKinds(){
        // 添加字符串, 省略Action的其他方法, 只使用一个onNext.
        Observable<String> obShow = Observable.just(saySome());

        // 先映射, 再设置TextView
        obShow.observeOn(AndroidSchedulers.mainThread())
                .map(mUpperLetterFunc).subscribe(mTextViewAction);

        // 单独显示数组中的每个元素
        Observable<String> obMap = Observable.from(mManyWords);

        // 映射之后分发
        obMap.observeOn(AndroidSchedulers.mainThread())
                .map(mUpperLetterFunc).subscribe(mToastAction);

        // 优化过的代码, 直接获取数组, 再分发, 再合并, 再显示toast, Toast顺次执行.
        Observable.just(mManyWordList)
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(mOneLetterFunc)
                .reduce(mMergeStringFunc)
                .subscribe(mToastAction);
    }

    private void testRx(){
        //创建一个观察者, 收到字符串的返回.
        Observable.OnSubscribe mObservableAction = new Observable.OnSubscribe<String>(){

            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext(saySome());
                subscriber.onCompleted();
            }
        };

       // 创建两个订阅者, 使用字符串输出信息.
        Subscriber<String> mTextSubscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                mTvText.setText(s);
            }
        };

        Subscriber<String> mSnackbarSubscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                Snackbar.make(mTvText,s,Snackbar.LENGTH_SHORT).show();
            }
        };

       // 观察者接收信息, 发送至主线程AndroidSchedulers.mainThread(),
       // 再传递给订阅者, 由订阅者最终处理消息. 接收信息可以是同步, 也可以是异步.
        // 注册观察活动
        Observable<String> observable = Observable.create(mObservableAction);
        // 分发订阅信息
        observable.subscribeOn(Schedulers.io());
        observable.observeOn(AndroidSchedulers.mainThread());
        observable.subscribe(mTextSubscriber);
        observable.subscribe(mSnackbarSubscriber);
    }

    private String saySome(){
        return "I am your friend,RxAndroid!";
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == android.R.id.home){
            finish();
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
