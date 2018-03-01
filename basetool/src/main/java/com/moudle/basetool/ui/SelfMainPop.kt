package com.moudle.basetool.ui

import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow

/**
 * Created by ke_li on 2018/2/27.
 */
abstract class SelfMainPop( context: Context) : PopupWindow() {
    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        contentView = inflater.inflate(initLayout(), null)
        width = context.resources.displayMetrics.widthPixels
        height = WindowManager.LayoutParams.MATCH_PARENT
        isOutsideTouchable = true
        update()
        val dw = ColorDrawable(0)
        setBackgroundDrawable(dw)
        contentView.setOnClickListener { if (isShowing) dismiss()}
    }

    @LayoutRes
    abstract fun initLayout(): Int

    abstract fun initPop()

    override fun showAsDropDown(anchor: View?) {
        if (Build.VERSION.SDK_INT >= 24) {
            val rect = Rect()
            anchor!!.getGlobalVisibleRect(rect)
            val h = anchor.resources.displayMetrics.heightPixels - rect.bottom
            height = h
        }
        super.showAsDropDown(anchor)
    }


   open fun showPopupWindow(parent: View) {
        if (!isShowing) {
                this.showAsDropDown(parent)
        } else {
            this.dismiss()
        }
       initPop()
    }


}