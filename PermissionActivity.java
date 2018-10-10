package com.airsaid.mpermissionutils;

import android.app.AppOpsManager;
import android.content.Context;
import android.os.Binder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.lang.reflect.Method;

/**
 * Created by ligen
 * date:2018/10/10
 * 描述:
 */
public class PermissionActivity extends AppCompatActivity {
    /**
     * 判断 悬浮窗口权限是否打开
     * 19之后开放 19之前默认返回true
     *
     * @return true 允许 false禁止
     */
    public  boolean getAppOps(Context context) {
        try {
            Object object = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                object = context.getSystemService(Context.APP_OPS_SERVICE);
            }else {
                Log.e("ligen", "getAppOps: below 19");
                return true;
            }
            if (object == null) {
                Log.e("ligen", "getAppOps: object is null");
                return true;
            }
            Class localClass = object.getClass();
            Class[] arrayOfClass = new Class[3];
            arrayOfClass[0] = Integer.TYPE;
            arrayOfClass[1] = Integer.TYPE;
            arrayOfClass[2] = String.class;
            Method method = localClass.getMethod("checkOp", arrayOfClass);
            if (method == null) {
                Log.e("ligen", "getAppOps: getMethod is null");
                return true;
            }
            Object[] arrayOfObject1 = new Object[3];
            arrayOfObject1[0] = Integer.valueOf(24);
            arrayOfObject1[1] = Integer.valueOf(Binder.getCallingUid());
            arrayOfObject1[2] = context.getPackageName();
            int m = ((Integer) method.invoke(object, arrayOfObject1)).intValue();
            return m == AppOpsManager.MODE_ALLOWED;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
