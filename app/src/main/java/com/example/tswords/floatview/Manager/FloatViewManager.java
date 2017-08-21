package com.example.tswords.floatview.Manager;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.text.Layout;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

import com.example.tswords.floatview.Util.Util;
import com.example.tswords.floatview.View.FloatBallView;
import com.example.tswords.floatview.View.FloatMenuView;

import java.lang.reflect.Field;

import static java.security.AccessController.getContext;

/**
 * Created by Tswords on 2017/8/19.
 */

public class FloatViewManager {
    private static FloatViewManager mfloatViewManager;
    private final WindowManager wm;
    private FloatBallView mfloatBallView;
    private FloatMenuView mfloatMenuView;
    private float startX;
    private float startY;
    private float consumeX;
    private float consumeY;
    private LayoutParams params;
    private Context mContext;
    private FloatViewManager(Context context) {
        mContext = context;
        wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mfloatBallView = new FloatBallView(context);
        final GestureDetector gestureDetector=new GestureDetector(new myGestureDetectorListener());
        View.OnTouchListener ballViewTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getRawX();
                        consumeX = startX;
                        startY = event.getRawY();
                        consumeY = startY;
                        gestureDetector.onTouchEvent(event);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        Float tmpX = event.getRawX();
                        Float tmpY = event.getRawY();
                        float dx = tmpX - startX;
                        float dy = tmpY - startY;
                        params.x += dx;
                        params.y += dy;

                        if (params.y > getScreenHeight()) {     // y轴上下边界处理
                            params.y = getScreenHeight() - mfloatBallView.getHeight();
                        } else if (params.y < 0) {
                            params.y = 0;
                        }
                        wm.updateViewLayout(mfloatBallView, params);
                        startX = tmpX.intValue(); //取整减少斜拉动引起的偏差
                        startY = tmpY.intValue();
                        break;
                    case MotionEvent.ACTION_UP:
                        int tmp = getScreenWidth();
                        if (event.getRawX() > tmp / 2) {  //靠边
                            params.x = tmp - mfloatBallView.getWidth();
                        } else {
                            params.x = 0;
                        }
                        wm.updateViewLayout(mfloatBallView, params);
                        if (Math.abs(consumeX - event.getRawX()) > 5 || Math.abs(consumeY - event.getRawY()) > 5) { //消费事件
                            return true;
                        }
                        gestureDetector.onTouchEvent(event);
                        break;
                    default:
                        gestureDetector.onTouchEvent(event);
                        break;
                }
                return false;
            }
        };
        mfloatBallView.setOnTouchListener(ballViewTouchListener);

    }

    public void removeFloatMenuView() {
        wm.removeView(mfloatMenuView);
        mfloatMenuView=null;
    }

    class myGestureDetectorListener extends GestureDetector.SimpleOnGestureListener {
        public myGestureDetectorListener(){
        }
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            wm.removeView(mfloatBallView);
            showFloatMenuView();
//            mfloatMenuView.StartAnimation();
            return true;
        }
    }
    private int getScreenWidth(){
        Point tp=new Point();
        wm.getDefaultDisplay().getSize(tp);
        return tp.x;
    }
    private int getScreenHeight(){
        Point tp=new Point();
        wm.getDefaultDisplay().getSize(tp);
        return tp.y;
    }
    private int getStatusBarHeight() {
        Class<?> c = null;

        Object obj = null;

        Field field = null;

        int x = 0, sbar = 0;

        try {

            c = Class.forName("com.android.internal.R$dimen");

            obj = c.newInstance();

            field = c.getField("status_bar_height");

            x = Integer.parseInt(field.get(obj).toString());

            sbar = mContext.getResources().getDimensionPixelSize(x);

        } catch (Exception e1) {

            e1.printStackTrace();

        }

        return sbar;
    }
    public static FloatViewManager getInstance(Context context){
        if(mfloatViewManager==null){
            synchronized (FloatViewManager.class){
                if(mfloatViewManager==null){
                    mfloatViewManager=new FloatViewManager(context);
                }
            }
        }
        return mfloatViewManager;
    }
    public void showFloatMenuView(){
        mfloatMenuView = new FloatMenuView(mContext);
        LayoutParams params = new LayoutParams();
        params.width=getScreenWidth();
        params.height=getScreenHeight()-getStatusBarHeight();
        params.gravity= Gravity.BOTTOM|Gravity.LEFT;
        params.x=0;
        params.y=0;
        boolean tmpbool=Util.hasAuthorFloatWin(mContext);   //判断权限
        if(tmpbool){
            params.type= LayoutParams.TYPE_PHONE;
        }else{
            params.type=LayoutParams.TYPE_TOAST;
        }
        params.flags= LayoutParams.FLAG_NOT_FOCUSABLE| LayoutParams.FLAG_NOT_TOUCH_MODAL;
        params.format= PixelFormat.RGBA_8888;
        wm.addView(mfloatMenuView, params);
    }
    public void showFloatBallView(){
        if(params==null){
            params = new LayoutParams();
            params.width=LayoutParams.WRAP_CONTENT;
            params.height=LayoutParams.WRAP_CONTENT;
            params.gravity= Gravity.TOP|Gravity.LEFT;
            params.x=0;
            params.y=0;
            boolean tmpbool=Util.hasAuthorFloatWin(mContext);   //判断权限
            if(tmpbool){
                params.type= LayoutParams.TYPE_PHONE;
            }else{
                params.type=LayoutParams.TYPE_TOAST;
            }

//        params.type= LayoutParams.TYPE_PHONE;
            params.flags= LayoutParams.FLAG_NOT_FOCUSABLE| LayoutParams.FLAG_NOT_TOUCH_MODAL;
            params.format= PixelFormat.RGBA_8888;

        }
        wm.addView(mfloatBallView, params);
    }
}
