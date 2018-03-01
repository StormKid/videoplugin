package com.moudle.basetool.ui

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.dialog_twobutton.*
import com.moudle.basetool.R
import com.moudle.basetool.R.style

/**
 * Created by ke_li on 2017/12/28.
 */
class MyAlertDialog private constructor(context: Context?) : Dialog(context, style.normalDialog) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_twobutton)
    }

    companion object instance {
        private lateinit var positiveText: String
        private lateinit var nagativeText: String
        private var isShowNagative: Boolean = true
        private lateinit var title: String
    }


    class Builder {
        init {
            positiveText = "确定"
            nagativeText = "取消"
            title = ""
        }

        fun setTitle(title: String): Builder {
            instance.title = title
            return this
        }

        fun setPositiveText(text: String): Builder {
            positiveText = text
            return this
        }

        fun setNagativeText(text: String): Builder {
            nagativeText = text
            return this
        }

        fun setNagativeShow(boolean: Boolean): Builder {
            isShowNagative = boolean
            return this
        }

        fun build(context: Context?): MyAlertDialog {
            return MyAlertDialog(context)
        }

    }

    fun excute(callback: DialogCallback) {
        if (!isShowing)
            show()
        dialog_title.text = title
        dialog_yes.text = positiveText
        dialog_no.text = nagativeText
        if (isShowNagative) dialog_no.visibility = View.VISIBLE
        else dialog_no.visibility = View.GONE
        dialog_yes.setOnClickListener {
            close()
            callback.ok()
        }
        dialog_no.setOnClickListener {
            close()
            callback.no()
        }
        dialog_all.setOnClickListener {
            close()
        }
    }

    private fun close() {
        if (isShowing)
            this.dismiss()
    }
}