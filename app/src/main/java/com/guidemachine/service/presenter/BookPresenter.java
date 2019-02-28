package com.guidemachine.service.presenter;

import android.content.Context;
import android.content.Intent;


import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.manager.DataManager;
import com.guidemachine.service.view.BookView;
import com.guidemachine.service.view.View;

import okhttp3.RequestBody;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;


/**
 * <p>
 * BookPresenter实现了我们定义的基础Presenter，在onCreate中我们创建了DataManager的实体类，便于调用RetrofitService中的方法，
 * 还新建了一个CompositeSubscription对象，CompositeSubscription是用来存放RxJava中的订阅关系的。注意请求完数据要及时清掉这个
 * 订阅关系，不然会发生内存泄漏。可在onStop中通过调用CompositeSubscription的unsubscribe方法来取消这个订阅关系，
 * 不过一旦调用这个方法，那么这个CompositeSubscription也就无法再用了，要想再用只能重新new一个。
 * 然后我们可以看到在attachView中，我们把BookView传进去。也就是说我们要把请求下来的实体类交给BookView来处理。
 * 接下来我们定义了一个方法getSearchBooks，名字和入参都和请求接口RetrofitService中的方法相同。
 * 这里的这个方法也就是请求的具体实现过程。其实也很简单，就是向CompositeSubscription添加一个订阅关系。
 * 上面我们已经说过manager.getSearchBooks就是调用RetrofitService的getSearchBooks方法，
 * 而这个方法返回的是一个泛型为Book的Observable，即被观察者，
 * 然后通过subscribeOn(Schedulers.io())来定义请求事件发生在io线程，
 * 然后通过observeOn(AndroidSchedulers.mainThread())来定义事件在主线程消费，
 * 即在主线程进行数据的处理，最后通过subscribe使观察者订阅它。
 * 在观察者中有三个方法：onNext、onCompleted、onError。当请求成功话，就会调用onNext，并传入请求返回的Book实体类，
 * 我们在onNext中，把请求下来的Book实体类存到内存中，当请求结束后会调用onCompleted，
 * 我们把请求下来的Book实体类交给BookView处理就可以了，如果请求失败，
 * 那么不会调用onCompleted而调用onError，这样我们可以向BookView传递错误消息。
 */

public class BookPresenter implements Presenter {
    private DataManager manager;
    private CompositeSubscription mCompositeSubscription;//用来存放RxJava中的订阅关系
    private Context mContext;
    private BookView mBookView;
    private BaseBean mBaseBean;

    public BookPresenter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onCreate() {
        manager = new DataManager(mContext);
        mCompositeSubscription = new CompositeSubscription();
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {
        if (mCompositeSubscription.hasSubscriptions()) {
            mCompositeSubscription.unsubscribe();
        }
    }

    @Override
    public void pause() {

    }

    @Override
    public void attachView(View view) {
        mBookView = (BookView) view;//把请求下来的实体类交给BookView来处理。
    }

    @Override
    public void attachIncomingIntent(Intent intetn) {

    }

    public void registerAppUser(RequestBody requestBody) {//网络请求就开始
        mCompositeSubscription.add(manager.registerAppUser(requestBody)//向CompositeSubscription添加一个订阅关系
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseBean>() {
                    @Override
                    public void onCompleted() {
                        if (mBaseBean != null) {
                            mBookView.onSuccess(mBaseBean);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mBookView.onError("请求失败:"+e.getMessage());
                    }

                    @Override
                    public void onNext(BaseBean baseBean) {
                        mBaseBean = baseBean;
                    }
                })
        );
    }
}
