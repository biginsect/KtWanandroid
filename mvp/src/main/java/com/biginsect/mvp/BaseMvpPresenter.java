package com.biginsect.mvp;

import androidx.annotation.UiThread;

import com.orhanobut.logger.Logger;

import java.lang.ref.WeakReference;

/**
 * @author biginsect
 * Created at 2021/3/18 20:27
 */
public abstract class BaseMvpPresenter<V extends MvpView> implements MvpPresenter<V> {
    private WeakReference<V> viewRef;
    private boolean presenterDestroy = false;

    @Override
    public void attachView(V v) {
        Logger.d("sssssview is :" + v + this);
        viewRef = new WeakReference<>(v);
        presenterDestroy = false;
    }

    @Override
    public void destroy() {
        presenterDestroy = true;
    }

    @Override
    public void detachView() {
        if (viewRef != null) {
            Logger.d("detach-->" + viewRef.get().toString());
            viewRef.clear();
            viewRef = null;
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
}
