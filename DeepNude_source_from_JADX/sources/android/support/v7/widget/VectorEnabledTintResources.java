package android.support.v7.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import java.lang.ref.WeakReference;

public class VectorEnabledTintResources extends Resources {
    public static final int MAX_SDK_WHERE_REQUIRED = 20;
    private static boolean sCompatVectorFromResourcesEnabled = false;
    private final WeakReference<Context> mContextRef;

    public VectorEnabledTintResources(Context context, Resources resources) {
        super(resources.getAssets(), resources.getDisplayMetrics(), resources.getConfiguration());
        this.mContextRef = new WeakReference(context);
    }

    public static boolean isCompatVectorFromResourcesEnabled() {
        return sCompatVectorFromResourcesEnabled;
    }

    public static void setCompatVectorFromResourcesEnabled(boolean z) {
        sCompatVectorFromResourcesEnabled = z;
    }

    public static boolean shouldBeUsed() {
        return isCompatVectorFromResourcesEnabled() && VERSION.SDK_INT <= 20;
    }

    public Drawable getDrawable(int i) {
        Context context = (Context) this.mContextRef.get();
        return context != null ? AppCompatDrawableManager.get().onDrawableLoadedFromResources(context, this, i) : super.getDrawable(i);
    }

    final Drawable superGetDrawable(int i) {
        return super.getDrawable(i);
    }
}
