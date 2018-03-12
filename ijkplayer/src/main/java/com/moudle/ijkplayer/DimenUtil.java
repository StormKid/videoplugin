package com.moudle.ijkplayer;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by ke_li on 2018/3/12.
 */

public class DimenUtil {

    public enum DEFULT { SMALL,BIG,NORMAL }
    private int  pointWidth =0;
    private int DEFAULT_SMALL_SIZE = 8;

    private int DEFAULT_SIZE = 10;

    private int DEFAULT_BIG_SIZE = 12;

    private int IMAGE_SIZE = 16;

    private Context context ;
    private DimenUtil(){}
    public static DimenUtil getInstance = new DimenUtil();
    private DEFULT defult = DEFULT.NORMAL ;

    /**
     * 根据
     * @param context
     */
    public DimenUtil build(Context context){
        this.context = context;
        pointWidth = context.getResources().getDisplayMetrics().widthPixels/320;
        return this;
    }

    public void setDefult (DEFULT defult){
        this.defult = defult;
    }

    /**
     * 设定默认方式
     * @param textView
     */
    public void setTextSize(TextView textView){
        switch (defult){
            case BIG:
                setTextSize(DEFAULT_BIG_SIZE,textView);
                break;
            case SMALL:
                setTextSize(DEFAULT_SMALL_SIZE,textView);
                break;
            case NORMAL:
                setTextSize(DEFAULT_SIZE,textView);
                break;
        }
    }



    public void autoSize(ViewGroup viewGroup){
        for (int i = 0 ; i<viewGroup.getChildCount();i++){
            View child = viewGroup.getChildAt(i);
            ViewGroup.LayoutParams layoutParams = child.getLayoutParams();
            if (child instanceof ImageView){
                layoutParams.height = getResultSizeInt(IMAGE_SIZE);
                layoutParams.width = getResultSizeInt(IMAGE_SIZE);
                child.setLayoutParams(layoutParams);
            }else if (child instanceof TextView){
                setTextSize((TextView) child);
            }

        }
    }


    /**
     * 设置字号
     * @param size
     * @param textView
     */
    public void setTextSize(int size,TextView textView){
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResultSizeFloat(size));
    }

    private float getResultSizeFloat(int size) {
        return size*pointWidth;
    }

    private int getResultSizeInt(int size) {
        return size*pointWidth;
    }
}
