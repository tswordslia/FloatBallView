package com.example.tswords.floatview.View;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

import com.example.tswords.floatview.Manager.FloatViewManager;
import com.example.tswords.floatview.R;


/**
 * Created by Tswords on 2017/8/21.
 */

public class FloatMenuView extends LinearLayout {

    private final TranslateAnimation translateAnimation;
    private final LinearLayout expanded_menu;

    public FloatMenuView(Context context) {
        super(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.float_menu_view,this,false);
        expanded_menu = (LinearLayout) view.findViewById(R.id.id_main_layout);
        translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF,1.0f,Animation.RELATIVE_TO_SELF,0);
        translateAnimation.setDuration(500);
        translateAnimation.setFillAfter(true);
        expanded_menu.setAnimation(translateAnimation);
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                FloatViewManager floatViewManager = FloatViewManager.getInstance(getContext());
                floatViewManager.removeFloatMenuView();
                floatViewManager.showFloatBallView();
            }
        });
        addView(view);
        translateAnimation.start();
    }

}
