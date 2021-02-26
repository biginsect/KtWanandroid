package com.biginsect.mvp;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.ArrayMap;

import com.orhanobut.logger.Logger;

import java.util.Map;
import java.util.UUID;

/**
 * @author biginsect
 * Created at 2021/2/23 16:26
 */
final class PresenterManager {

    private static boolean DEBUG = false;
    private final static String TAG = PresenterManager.class.getSimpleName();
    private final static String KEY_ACTIVITY_ID = "PresenterManagerActivityId";
    private final static Map<Activity, String> MAP_ACTIVITY_ID = new ArrayMap<>();
    private final static Map<String, ActivityScopedCache> MAP_ACTIVITY_SCOPED_CACHE = new ArrayMap<>();

    private static final Application.ActivityLifecycleCallbacks ACTIVITY_LIFECYCLE_CALLBACKS = new Application.ActivityLifecycleCallbacks() {

        @Override
        public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
            if (savedInstanceState != null) {
                String activityId = savedInstanceState.getString(KEY_ACTIVITY_ID);
                if (!TextUtils.isEmpty(activityId)) {
                    MAP_ACTIVITY_ID.put(activity, activityId);
                }
            }
        }

        @Override
        public void onActivityStarted(@NonNull Activity activity) {

        }

        @Override
        public void onActivityResumed(@NonNull Activity activity) {

        }

        @Override
        public void onActivityPaused(@NonNull Activity activity) {

        }

        @Override
        public void onActivityStopped(@NonNull Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
            String activityId = MAP_ACTIVITY_ID.get(activity);
            if (!TextUtils.isEmpty(activityId)) {
                outState.putString(KEY_ACTIVITY_ID, activityId);
            }
        }

        @Override
        public void onActivityDestroyed(@NonNull Activity activity) {
            if (!activity.isChangingConfigurations()) {
                String activityId = MAP_ACTIVITY_ID.get(activity);
                if (!TextUtils.isEmpty(activityId)) {
                    ActivityScopedCache scopedCache = MAP_ACTIVITY_SCOPED_CACHE.get(activityId);
                    if (scopedCache != null) {
                        scopedCache.clear();
                        MAP_ACTIVITY_SCOPED_CACHE.remove(activityId);
                    }

                    if (MAP_ACTIVITY_SCOPED_CACHE.isEmpty()) {
                        activity.getApplication().unregisterActivityLifecycleCallbacks(ACTIVITY_LIFECYCLE_CALLBACKS);
                        if (DEBUG) {
                            Logger.d(TAG, "unregisterActivityLifecycleCallbacks");
                        }
                    }
                }
            }
            MAP_ACTIVITY_ID.remove(activity);
        }
    };

    private PresenterManager() {
        throw new UnsupportedOperationException("Cannot be initialized");
    }

    @MainThread
    static ActivityScopedCache getOrCreateActivityScopedCache(Activity activity) {
        String activityId = MAP_ACTIVITY_ID.get(activity);
        if (TextUtils.isEmpty(activityId)) {
            activityId = UUID.randomUUID().toString();
            MAP_ACTIVITY_ID.put(activity, activityId);
            if (MAP_ACTIVITY_ID.size() == 1) {
                activity.getApplication().registerActivityLifecycleCallbacks(ACTIVITY_LIFECYCLE_CALLBACKS);
                if (DEBUG) {
                    Logger.d(TAG, "registerActivityLifecycleCallbacks");
                }
            }
        }

        ActivityScopedCache scopedCache = MAP_ACTIVITY_SCOPED_CACHE.get(activityId);
        if (scopedCache == null) {
            scopedCache = new ActivityScopedCache();
            MAP_ACTIVITY_SCOPED_CACHE.put(activityId, scopedCache);
        }

        return scopedCache;
    }

    @MainThread
    static ActivityScopedCache getActivityScoped(Activity activity) {
        return MAP_ACTIVITY_SCOPED_CACHE.get(MAP_ACTIVITY_ID.get(activity));
    }

    @SuppressWarnings("unchecked")
    public static <P> P getPresenter(Activity activity, String viewId) {
        ActivityScopedCache scopedCache = getActivityScoped(activity);

        return scopedCache != null ? (P) scopedCache.obtainPresenter(viewId) : null;
    }

    @SuppressWarnings("unchecked")
    public static <VS> VS getViewState(Activity activity, String viewId) {
        ActivityScopedCache scopedCache = getActivityScoped(activity);

        return scopedCache != null ? (VS) scopedCache.obtainViewState(viewId) : null;
    }

    public static Activity getActivity(Context context) {
        if (context == null) {
            Logger.e(TAG, "context is null");
            return null;
        }

        if (context instanceof Activity) {
            return (Activity) context;
        }

        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }

        throw new IllegalStateException("Could not find the surrounding activity");
    }

    static void reset() {
        MAP_ACTIVITY_ID.clear();
        for (ActivityScopedCache scopedCache : MAP_ACTIVITY_SCOPED_CACHE.values()) {
            scopedCache.clear();
        }
        MAP_ACTIVITY_SCOPED_CACHE.clear();
    }

    public static void putPresenter(Activity activity, String viewId, MvpPresenter<? extends MvpView> presenter) {
        if (activity == null) {
            throw new NullPointerException("Activity is null");
        }

        ActivityScopedCache scopedCache = getOrCreateActivityScopedCache(activity);
        scopedCache.putPresenter(viewId, presenter);
    }

    public static void putViewState(Activity activity, String viewId, Object viewState) {
        if (activity == null) {
            throw new NullPointerException("Activity is null");
        }
        ActivityScopedCache scopedCache = getOrCreateActivityScopedCache(activity);
        scopedCache.putViewState(viewId, viewState);
    }

    public static void remove(Activity activity, String viewId) {
        if (activity == null) {
            throw new NullPointerException("Activity is null");
        }

        ActivityScopedCache scopedCache = getActivityScoped(activity);
        if (scopedCache != null) {
            scopedCache.remove(viewId);
        }
    }
}
