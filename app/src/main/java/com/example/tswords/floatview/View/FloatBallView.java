package com.example.tswords.floatview.View;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.example.tswords.floatview.Util.Util;

/**
 * Created by Tswords on 2017/8/19.
 */

public class FloatBallView extends View {
    private int widthDp=50;
    private int heightDp=50;
    private int amplitudeDp=8;    //水波浪起始振幅
    private float amplitude;    //水波浪起始振幅
    private int mWaveLength;    //波浪长度，和view同宽
    private float processNow;
    private int offset=0;
    private int mWaveCount=2;   //波浪数
    private float process=35f;     //进度
    private int width;
    private int height;
    private String mtext;
    private Context mContext;
    private final Path mPath=new Path();
    private final Paint circlePaint = new Paint();
    private final Paint textPaint = new Paint();
    private final Paint progressPaint =new Paint();
    private Bitmap bitmap;
    private Canvas bitmapCanvas;
    private ValueAnimator mValueAnimator;
    private boolean flag=false;

    public FloatBallView(Context context) {
        super(context);
        mContext=context;
        width= Util.dp2px(widthDp,mContext);
        height= Util.dp2px(heightDp,mContext);
        mWaveLength=width;
//        processNow =height/2;
        initPaints();

    }

    public FloatBallView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FloatBallView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    private void initPaints(){
//        circlePaint.setColor(Color.GRAY);
        circlePaint.setColor(Color.argb(0xff,0x3a,0x8c,0x6c));
        circlePaint.setAntiAlias(true);

       // progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setAntiAlias(true);
        progressPaint.setStrokeWidth(1);
       // progressPaint.setColor(Color.RED);
        progressPaint.setColor(Color.argb(0xff,0x4e,0xc9,0x63));
        progressPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        //为了达到悬浮球色彩叠加的效果，使用了下面两个参数
        bitmap = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
        bitmapCanvas = new Canvas(bitmap);

        textPaint.setTextSize(30);
        textPaint.setColor(Color.WHITE);
        textPaint.setAntiAlias(true);
        textPaint.setFakeBoldText(true);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(width,height);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        bitmapCanvas.drawCircle(width/2,height/2,width/2,circlePaint);
        processNow=height*(1-(process/(float)100)); //当前进度的像素位置
        mtext=Integer.toString((int)process)+"%";
        mPath.reset();
        if(!flag){
            //没有波浪
            mPath.moveTo(0,processNow);
            mPath.lineTo(width,processNow);   //height-(process/100*height)
            mPath.lineTo(width,height);
            mPath.lineTo(0,height);
        }else{
            //波浪效果

            mPath.moveTo(-mWaveLength, processNow);
            mPath.lineTo(-mWaveLength+offset, processNow); //阻止曲线拉长
            for (int i = 0; i < mWaveCount; i++) {
                mPath.quadTo(-mWaveLength * 3 / 4 + i * mWaveLength + offset, processNow + amplitude,-mWaveLength / 2 + i * mWaveLength + offset, processNow);
                mPath.quadTo(-mWaveLength / 4 + i * mWaveLength + offset, processNow - amplitude,i * mWaveLength + offset, processNow);
            }
            mPath.lineTo(width,height);
            mPath.lineTo(0,height);
        }



        mPath.close();
        bitmapCanvas.drawPath(mPath,progressPaint);
        canvas.drawBitmap(bitmap,0,0,null);


//        计算使文字居中
        float textwidth=textPaint.measureText(mtext);
        float x=width/2-textwidth/2;
        Paint.FontMetrics metrics=textPaint.getFontMetrics();
        float dy=-(metrics.descent+metrics.ascent)/2;
        float y=height/2+dy;
        canvas.drawText(mtext,x,y,textPaint);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(flag){
                    return false;
                }else {
                    flag=true;
                    amplitude= Util.dp2px(amplitudeDp,mContext);
                    mValueAnimator = ValueAnimator.ofInt(0, mWaveLength);
                    mValueAnimator.setDuration(750);    //动画间隔
                    mValueAnimator.setInterpolator(new LinearInterpolator());
                    mValueAnimator.setRepeatCount(ValueAnimator.INFINITE);
                    mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            offset = (int) animation.getAnimatedValue();

                            amplitude-=0.06; //振幅下降
                            if(amplitude<=0){
                                mValueAnimator.cancel();
                                flag=false;
                            }
                            invalidate();
                        }
                    });
                    mValueAnimator.start();
                }
                break;
        }
        return super.onTouchEvent(event);

    }
}
