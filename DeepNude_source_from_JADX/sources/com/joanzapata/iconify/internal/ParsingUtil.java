package com.joanzapata.iconify.internal;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v4.view.ViewCompat;
import android.text.SpannableStringBuilder;
import android.util.TypedValue;
import android.widget.TextView;
import com.joanzapata.iconify.Icon;
import com.joanzapata.iconify.internal.HasOnViewAttachListener.OnViewAttachListener;
import java.util.List;

public final class ParsingUtil {
    private static final String ANDROID_PACKAGE_NAME = "android";

    private ParsingUtil() {
    }

    public static float dpToPx(Context context, float f) {
        return TypedValue.applyDimension(1, f, context.getResources().getDisplayMetrics());
    }

    public static int getColorFromResource(Context context, String str, String str2) {
        Resources resources = context.getResources();
        int identifier = resources.getIdentifier(str2, "color", str);
        return identifier <= 0 ? ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED : resources.getColor(identifier);
    }

    public static float getPxFromDimen(Context context, String str, String str2) {
        Resources resources = context.getResources();
        int identifier = resources.getIdentifier(str2, "dimen", str);
        return identifier <= 0 ? -1.0f : resources.getDimension(identifier);
    }

    private static boolean hasAnimatedSpans(SpannableStringBuilder spannableStringBuilder) {
        for (CustomTypefaceSpan isAnimated : (CustomTypefaceSpan[]) spannableStringBuilder.getSpans(0, spannableStringBuilder.length(), CustomTypefaceSpan.class)) {
            if (isAnimated.isAnimated()) {
                return true;
            }
        }
        return false;
    }

    public static CharSequence parse(Context context, List<IconFontDescriptorWrapper> list, CharSequence charSequence, final TextView textView) {
        context = context.getApplicationContext();
        if (charSequence == null) {
            return charSequence;
        }
        CharSequence spannableStringBuilder = new SpannableStringBuilder(charSequence);
        recursivePrepareSpannableIndexes(context, charSequence.toString(), spannableStringBuilder, list, 0);
        if (hasAnimatedSpans(spannableStringBuilder)) {
            if (textView == null) {
                throw new IllegalArgumentException("You can't use \"spin\" without providing the target TextView.");
            } else if (textView instanceof HasOnViewAttachListener) {
                ((HasOnViewAttachListener) textView).setOnViewAttachListener(new OnViewAttachListener() {
                    boolean isAttached = false;

                    /* renamed from: com.joanzapata.iconify.internal.ParsingUtil$1$1 */
                    class C05751 implements Runnable {
                        C05751() {
                        }

                        public void run() {
                            C08691 c08691 = C08691.this;
                            if (c08691.isAttached) {
                                textView.invalidate();
                                ViewCompat.postOnAnimation(textView, this);
                            }
                        }
                    }

                    public void onAttach() {
                        this.isAttached = true;
                        ViewCompat.postOnAnimation(textView, new C05751());
                    }

                    public void onDetach() {
                        this.isAttached = false;
                    }
                });
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(textView.getClass().getSimpleName());
                stringBuilder.append(" does not implement ");
                stringBuilder.append("HasOnViewAttachListener. Please use IconTextView, IconButton or IconToggleButton.");
                throw new IllegalArgumentException(stringBuilder.toString());
            }
        } else if (textView instanceof HasOnViewAttachListener) {
            ((HasOnViewAttachListener) textView).setOnViewAttachListener(null);
        }
        return spannableStringBuilder;
    }

    private static void recursivePrepareSpannableIndexes(Context context, String str, SpannableStringBuilder spannableStringBuilder, List<IconFontDescriptorWrapper> list, int i) {
        Context context2 = context;
        String str2 = str;
        SpannableStringBuilder spannableStringBuilder2 = spannableStringBuilder;
        List<IconFontDescriptorWrapper> list2 = list;
        String spannableStringBuilder3 = spannableStringBuilder.toString();
        int indexOf = spannableStringBuilder3.indexOf("{", i);
        if (indexOf != -1) {
            int indexOf2 = spannableStringBuilder3.indexOf("}", indexOf) + 1;
            if (indexOf2 != -1) {
                int i2 = indexOf + 1;
                String[] split = spannableStringBuilder3.substring(i2, indexOf2 - 1).split(" ");
                int i3 = 0;
                String str3 = split[0];
                Icon icon = null;
                IconFontDescriptorWrapper iconFontDescriptorWrapper = icon;
                for (int i4 = 0; i4 < list.size(); i4++) {
                    iconFontDescriptorWrapper = (IconFontDescriptorWrapper) list2.get(i4);
                    icon = iconFontDescriptorWrapper.getIcon(str3);
                    if (icon != null) {
                        break;
                    }
                }
                Icon icon2 = icon;
                if (icon2 == null) {
                    recursivePrepareSpannableIndexes(context2, str2, spannableStringBuilder2, list2, indexOf2);
                    return;
                }
                int i5 = 1;
                float f = -1.0f;
                float f2 = -1.0f;
                int i6 = ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED;
                boolean z = false;
                boolean z2 = false;
                while (i5 < split.length) {
                    String[] strArr;
                    String str4 = split[i5];
                    if (str4.equalsIgnoreCase("spin")) {
                        strArr = split;
                        z = true;
                    } else if (str4.equalsIgnoreCase("baseline")) {
                        strArr = split;
                        z2 = true;
                    } else {
                        if (str4.matches("([0-9]*(\\.[0-9]*)?)dp")) {
                            f = dpToPx(context2, Float.valueOf(str4.substring(i3, str4.length() - 2)).floatValue());
                        } else if (str4.matches("([0-9]*(\\.[0-9]*)?)sp")) {
                            f = spToPx(context2, Float.valueOf(str4.substring(i3, str4.length() - 2)).floatValue());
                        } else if (str4.matches("([0-9]*)px")) {
                            strArr = split;
                            f = (float) Integer.valueOf(str4.substring(i3, str4.length() - 2)).intValue();
                        } else {
                            String str5 = "Unknown resource ";
                            String str6 = "\"";
                            String str7 = " in \"";
                            StringBuilder stringBuilder;
                            if (str4.matches("@dimen/(.*)")) {
                                strArr = split;
                                f = getPxFromDimen(context2, context.getPackageName(), str4.substring(7));
                                if (f < 0.0f) {
                                    stringBuilder = new StringBuilder();
                                    stringBuilder.append(str5);
                                    stringBuilder.append(str4);
                                    stringBuilder.append(str7);
                                    stringBuilder.append(str2);
                                    stringBuilder.append(str6);
                                    throw new IllegalArgumentException(stringBuilder.toString());
                                }
                            }
                            strArr = split;
                            boolean matches = str4.matches("@android:dimen/(.*)");
                            String str8 = ANDROID_PACKAGE_NAME;
                            if (matches) {
                                f = getPxFromDimen(context2, str8, str4.substring(15));
                                if (f < 0.0f) {
                                    stringBuilder = new StringBuilder();
                                    stringBuilder.append(str5);
                                    stringBuilder.append(str4);
                                    stringBuilder.append(str7);
                                    stringBuilder.append(str2);
                                    stringBuilder.append(str6);
                                    throw new IllegalArgumentException(stringBuilder.toString());
                                }
                            } else if (str4.matches("([0-9]*(\\.[0-9]*)?)%")) {
                                f2 = Float.valueOf(str4.substring(0, str4.length() - 1)).floatValue() / 100.0f;
                            } else if (str4.matches("#([0-9A-Fa-f]{6}|[0-9A-Fa-f]{8})")) {
                                i6 = Color.parseColor(str4);
                            } else {
                                int colorFromResource;
                                if (str4.matches("@color/(.*)")) {
                                    colorFromResource = getColorFromResource(context2, context.getPackageName(), str4.substring(7));
                                    if (colorFromResource == ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED) {
                                        stringBuilder = new StringBuilder();
                                        stringBuilder.append(str5);
                                        stringBuilder.append(str4);
                                        stringBuilder.append(str7);
                                        stringBuilder.append(str2);
                                        stringBuilder.append(str6);
                                        throw new IllegalArgumentException(stringBuilder.toString());
                                    }
                                } else if (str4.matches("@android:color/(.*)")) {
                                    colorFromResource = getColorFromResource(context2, str8, str4.substring(15));
                                    if (colorFromResource == ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED) {
                                        stringBuilder = new StringBuilder();
                                        stringBuilder.append(str5);
                                        stringBuilder.append(str4);
                                        stringBuilder.append(str7);
                                        stringBuilder.append(str2);
                                        stringBuilder.append(str6);
                                        throw new IllegalArgumentException(stringBuilder.toString());
                                    }
                                } else {
                                    stringBuilder = new StringBuilder();
                                    stringBuilder.append("Unknown expression ");
                                    stringBuilder.append(str4);
                                    stringBuilder.append(str7);
                                    stringBuilder.append(str2);
                                    stringBuilder.append(str6);
                                    throw new IllegalArgumentException(stringBuilder.toString());
                                }
                                i6 = colorFromResource;
                            }
                        }
                        strArr = split;
                    }
                    i5++;
                    list2 = list;
                    split = strArr;
                    i3 = 0;
                }
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("");
                stringBuilder2.append(icon2.character());
                spannableStringBuilder2 = spannableStringBuilder2.replace(indexOf, indexOf2, stringBuilder2.toString());
                spannableStringBuilder2.setSpan(new CustomTypefaceSpan(icon2, iconFontDescriptorWrapper.getTypeface(context2), f, f2, i6, z, z2), indexOf, i2, 17);
                recursivePrepareSpannableIndexes(context2, str2, spannableStringBuilder2, list, indexOf);
            }
        }
    }

    public static float spToPx(Context context, float f) {
        return TypedValue.applyDimension(2, f, context.getResources().getDisplayMetrics());
    }
}
