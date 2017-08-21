package com.example.tswords.floatview.Util;

import android.app.AppOpsManager;
import android.content.Context;
import android.os.Binder;
import android.os.Build;
import android.util.TypedValue;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * dp,sp转px
 * Created by Tswords on 2017/8/19.
 */

public class Util {
    public static int dp2px(int dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    public static int sp2px(int dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, dp, context.getResources().getDisplayMetrics());
    }

    /**
     * 判断是否开启浮窗权限,api未公开，使用反射调用
     *
     * @return
     */
    public static boolean hasAuthorFloatWin(Context context) {

        if (Build.VERSION.SDK_INT < 19) {
            return true;
        }
        try {
            AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            Class c = appOps.getClass();
            Class[] cArg = new Class[3];
            cArg[0] = int.class;
            cArg[1] = int.class;
            cArg[2] = String.class;
            Method lMethod = c.getDeclaredMethod("checkOp", cArg);
            //24是浮窗权限的标记

            return (AppOpsManager.MODE_ALLOWED == (Integer) lMethod.invoke(appOps, 24, Binder.getCallingUid(), context.getPackageName()));

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return false;
    }
}
