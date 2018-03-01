package com.moudle.basetool.ui;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.moudle.basetool.R;
import com.moudle.basetool.utils.ManagerUtils;

/**
 * Created by like on 2017/9/24.
 */

public class ItemView extends FrameLayout {
    private Context mContext;
    /**
     * 左侧图片
     */
    private ImageView item_iv;
    /**
     * 中间内容
     */
    private TextView item_tv;
    /**
     * 右侧右括弧
     */
    private ImageView item_to;
    /**
     * 右边属性的值
     */
    private TextView item_value;
    private ViewGroup item_contain;
    private TextView item_title;

    public ItemView(Context context) {
        this(context, null);
        this.mContext = context;
    }

    public ItemView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
        this.mContext = context;
    }

    public ItemView(final Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_view, this);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.ItemView, defStyleAttr, 0);
        int resourceId = a.getResourceId(R.styleable.ItemView_left_img, 0);
        String title = a.getString(R.styleable.ItemView_mid_title);
        String value = a.getString(R.styleable.ItemView_right_value);
        String head  =  a.getString(R.styleable.ItemView_left_head);
        int bg_res = a.getResourceId(R.styleable.ItemView_bg_res, 0);
        boolean isVisible = a.getBoolean(R.styleable.ItemView_right_visible, false);
        boolean leftVisible = a.getBoolean(R.styleable.ItemView_left_visible, true);
        boolean leftGone = a.getBoolean(R.styleable.ItemView_left_gone , false);
        boolean rightGone = a.getBoolean(R.styleable.ItemView_right_gone,false);
        int right_tv_size = a.getInt(R.styleable.ItemView_right_tv_size, 10);
        int title_size = a.getInt(R.styleable.ItemView_title_size, 10);
        int textColor = a.getResourceId(R.styleable.ItemView_text_color, R.color.text_333);
        int right_img = a.getResourceId(R.styleable.ItemView_right_img, 0);
        int title_gravity = a.getInt(R.styleable.ItemView_title_gravity, 1);
        boolean isbanner = a.getBoolean(R.styleable.ItemView_is_banner, false);
        int title_color = a.getResourceId(R.styleable.ItemView_title_color, R.color.text_333);
        int head_size =  a.getInt(R.styleable.ItemView_head_size,11);
        item_iv = inflate.findViewById(R.id.item_iv);
        item_tv = inflate.findViewById(R.id.item_tv);
        item_to = inflate.findViewById(R.id.item_to);
        item_value = inflate.findViewById(R.id.item_value);
        item_contain = inflate.findViewById(R.id.item_contain);
        item_title = inflate.findViewById(R.id.item_title);
        item_iv.setImageResource(resourceId);
        item_to.setImageResource(right_img);
        item_value.setText(value);
        item_tv.setText(title);
        item_title.setText(head);
        item_tv.setTextColor(ContextCompat.getColor(context, textColor));
        item_value.setTextColor(ContextCompat.getColor(context, textColor));
        item_contain.setBackgroundResource(bg_res);
        item_title.setTextColor(ContextCompat.getColor(context, title_color));
        ManagerUtils.INSTANCE.getTextPx(context, title_size, item_tv);
        ManagerUtils.INSTANCE.getTextPx(context,head_size,item_title);
        ManagerUtils.INSTANCE.getTextPx(context, right_tv_size, item_value);
        switch (title_gravity) {
            case 0:
                item_tv.setGravity(Gravity.CENTER);
                break;
            case 1:
                item_tv.setGravity(Gravity.LEFT);
                break;
            case 2:
                item_tv.setGravity(Gravity.RIGHT);
                break;
        }
        if (isVisible) item_to.setVisibility(VISIBLE);
        else item_to.setVisibility(INVISIBLE);
        if (leftVisible) item_iv.setVisibility(VISIBLE);
        else item_iv.setVisibility(INVISIBLE);
        if (leftGone) item_iv.setVisibility(GONE);
        if (rightGone)item_to.setVisibility(GONE);
        if (isbanner) {
            item_iv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (context instanceof Activity) ((Activity) context).finish();
                }
            });
        }
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setLayoutParams(params);
    }


    public void setleftLogo(int Res) {
        item_iv.setImageResource(Res);
    }

    public void setleftVisible(boolean b) {
        if (b) item_iv.setVisibility(VISIBLE);
        else item_iv.setVisibility(INVISIBLE);
    }


    public void setMidText(String middTitle) {
        item_tv.setText(middTitle);
    }

    public void setValueText(String valueText) {
        item_value.setText(valueText);
    }

    public void setRightVisible(boolean b) {
        if (b) item_to.setVisibility(VISIBLE);
        else item_to.setVisibility(INVISIBLE);
    }

    public void setLeftGone(boolean  b){
        if (b) item_iv.setVisibility(GONE);
    }

    public void setRightGone(boolean b){
        if (b) item_to.setVisibility(GONE);
    }

    public void setLeftImgClickListener(OnClickListener listener) {
        if (listener != null) item_iv.setOnClickListener(listener);
    }

    public void setRightImgClickListener(OnClickListener listener) {
        if (listener != null) item_to.setOnClickListener(listener);
    }

    /**
     * 刷新标题布局为系统默认布局
     */
    public void initSetMid(){
        item_tv.setMaxLines(Integer.MAX_VALUE);
        item_tv.setEllipsize(null);
    }

    public void serRightTextClickListener(OnClickListener listener){
        if (listener!=null)item_value.setOnClickListener(listener);
    }


}
