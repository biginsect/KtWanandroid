package com.biginsect.mvp;

import androidx.annotation.UiThread;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

import com.orhanobut.logger.Logger;

import java.lang.ref.WeakReference;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

/**
 * @author biginsect
 * Created at 2021/3/18 20:27
 */
public abstract class BaseMvpPresenter<V extends MvpView> implements MvpPresenter<V>, LifecycleObserver {

    private WeakReference<V> viewRef;
    private boolean presenterDestroy = false;
    private CompositeDisposable compositeDisposable;

    @Override
    public void attachView(V v) {
        viewRef = new WeakReference<>(v);
        presenterDestroy = false;
        if (v instanceof LifecycleOwner){
            ((LifecycleOwner) v).getLifecycle().addObserver(this);
        }
    }

    @Override
    public void destroy() {
        presenterDestroy = true;
        if (getView() instanceof LifecycleOwner) {
            ((LifecycleOwner) getView()).getLifecycle().removeObserver(this);
        }
    }

    @Override
    public void detachView() {
        if (viewRef != null) {
            Logger.d("detach-->" + viewRef.get().toString());
            viewRef.clear();
            viewRef = null;
        }
        if (compositeDisposable != null){
            compositeDisposable.clear();
        }
    }

    @UiThread
    public V getView() {
        return viewRef != null ? viewRef.get() : null;
    }

    @UiThread
    public boolean isViewAttached() {
        return viewRef != null && viewRef.get() != null;
    }

    protected final void ifViewAttached(ViewAction<V> action) {
        if (action == null) {
            return;
        }

        final V view = viewRef != null ? viewRef.get() : null;
        if (view != null) {
            action.run(view);
        }
    }

    public interface ViewAction<V> {
        void run(V view);
    }

    protected void addSubscribe(Disposable disposable){
        if (compositeDisposable == null){
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(disposable);
    }
}
