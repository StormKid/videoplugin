package com.moudle.basetool.utils

import android.util.Log

/**
 * Created by ke_li on 2017/12/20.
 */
open class LogUtil {
    companion object {

        private val isOpen: Boolean = true
        val tag = "myApp"

        fun logE(msg: String) {
            if (isOpen) logE(tag, msg)
        }


        fun logE(tag: String,msg: String ) {
            if (isOpen) Log.e(tag, msg)
        }

        fun LogI( tag: String,msg: String) {
            if (isOpen) Log.i(tag, msg)
        }

        fun LogI(msg: String) {
            if (isOpen) LogI(tag, msg)
        }

        fun d(msg: String){
            if (isOpen)Log.d(tag, msg)
        }

        fun d(tag: String,msg: String){
            if (isOpen)Log.d(tag,msg)
        }
    }


}