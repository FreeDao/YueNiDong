
package com.yuenidong.util;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

@SuppressLint("NewApi")
public class SmartBarUtils {

    /**
     * uc等在使用的方法(新旧版flyme均有效)，
     * 此方法需要配合requestWindowFeature(Window.FEATURE_NO_TITLE
     * )使用,缺点是程序无法使用系统actionbar
     * 
     * @param decorView window.getDecorView
     */
    public static void hide(View decorView) {
        if (!isMeizu())
            return;

        try {
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

//            Class[] arrayOfClass = new Class[1];
//            arrayOfClass[0] = Integer.TYPE;
//            Method localMethod = View.class.getMethod("setSystemUiVisibility", arrayOfClass);
//            Field localField = View.class.getField("SYSTEM_UI_FLAG_HIDE_NAVIGATION");
//            Object[] arrayOfObject = new Object[1];
//            try {
//                arrayOfObject[0] = localField.get(null);
//            } catch (Exception ignored) {
//
//            }
//            localMethod.invoke(decorView, arrayOfObject);
        } catch (Exception ignored) {
        }
    }

    /**
     * 新发现的方法(新旧版flyme均有效) 需要使用顶部系统actionbar的应用请使用此方法
     * 
     * @param context
     * @param window
     */
    public static void hide(Context context, Window window) {
        if (!isMeizu()) {
            return;
        }
        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            return;
        }

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        int statusBarHeight = CommonUtils.getStatusBarHeight(context);

        window.getDecorView().setPadding(0, statusBarHeight, 0, 0);
    }

    public static void hide(Activity activity) {
        Window window = activity.getWindow();
        // flyme3.7.6体验版有效
        if (Build.VERSION.SDK_INT > 18) {
            ActionBar actionBar = activity.getActionBar();
            if (!checkIfNull(actionBar, "activity.getActionBar() failed")) {
                return;
            }

            Object mActionView = SmartBarUtils.GetFieldValue(actionBar, "mActionView");
            if (!checkIfNull(mActionView, "get mActionView failed")) {
                return;
            }

            ViewGroup mSplitView = (ViewGroup) SmartBarUtils.GetFieldValue(mActionView,
                    "mSplitView");
            if (!checkIfNull(mSplitView, "get mSplitView failed")) {
                return;
            }

            if (!checkIfNull(window, "activity.getWindow() failed")) {
                return;
            }

            View decorView = window.getDecorView();
            if (!checkIfNull(decorView, "window.getDecorView() failed")) {
                return;
            }

            Object mUiOptions = SmartBarUtils.GetFieldValue(window, "mUiOptions");
            if (!checkIfNull(mUiOptions, "get windows mUiOptions failed")) {
                return;
            }

            mSplitView.setVisibility(View.GONE);

            for (int i = 0; i < mSplitView.getChildCount(); i++) {
                mSplitView.getChildAt(i).setVisibility(View.GONE);
            }
        } else {
            if (isMeizu()
                    && (window.getAttributes().flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) != WindowManager.LayoutParams.FLAG_FULLSCREEN) {
                try {
                    ViewGroup decorChild = (ViewGroup) ((ViewGroup) window.getDecorView())
                            .getChildAt(0);
                    decorChild.getChildAt(decorChild.getChildCount() - 1).setVisibility(View.GONE);
                } catch (Exception e) {
                }

            }
        }
    }

    public static boolean isMeizu() {
        return Build.MANUFACTURER.equalsIgnoreCase("meizu");
    }

    private static boolean checkIfNull(Object obj, String error) throws NullPointerException {
        if (obj == null) {
            Log.e("smartutils", error);
            return false;
        }

        return true;
    }

    public static Object GetFieldValue(Object aObject, String aFieldName) {
        Field field = GetClassField(aObject.getClass(), aFieldName);
        if (field != null) {
            field.setAccessible(true);
            try {
                return field.get(aObject);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    public static Field GetClassField(Class aClazz, String aFieldName) {
        Field[] declaredFields = aClazz.getDeclaredFields();
        for (Field field : declaredFields) {
            if (field.getName().equals(aFieldName)) {
                return field;
            }
        }

        Class superclass = aClazz.getSuperclass();
        if (superclass != null) {
            return GetClassField(superclass, aFieldName);
        }
        return null;
    }

}
