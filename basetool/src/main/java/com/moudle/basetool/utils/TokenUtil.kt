package com.moudle.basetool.utils

import android.content.Context
import android.text.TextUtils
import com.moudle.basetool.utils.Constants.token


/**
 * Created by ke_li on 2017/12/18.
 */
class TokenUtil {
    companion object {
        fun getToken(context: Context):String{
            val token = SharePerfrenceUtil.get(context,token,"") as String
            return if (TextUtils.isEmpty(token)) ""
            else token
        }

        fun setToken(context: Context,token:String){
            SharePerfrenceUtil.put(context, Constants.token,token)
        }
    }


}