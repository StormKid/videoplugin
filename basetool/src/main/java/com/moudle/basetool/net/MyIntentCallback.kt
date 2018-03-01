package com.moudle.basetool.net

import android.content.Context
import android.support.v4.widget.SwipeRefreshLayout
import android.text.TextUtils
import com.lzy.okgo.OkGo
import com.lzy.okgo.callback.StringCallback
import com.lzy.okgo.model.Response
import com.lzy.okgo.request.base.Request
import com.moudle.basetool.utils.Constants.head
import com.moudle.basetool.utils.ManagerUtils
import com.moudle.basetool.utils.TokenUtil
import org.json.JSONObject


/**
 * Created by ke_li on 2017/12/19.
 */
class MyIntentCallback(val context: Context, private val tag: Any, private val callbackNormal: MyNormalNetCallback, private val refresh: SwipeRefreshLayout?) : StringCallback() {

    val NET_ERR = "网络不给力，请检查网络链接"

    override fun onFinish() {
        OkGo.getInstance().cancelTag(tag)
    }

    override fun onStart(request: Request<String, out Request<Any, Request<*, *>>>?) {
        val token = TokenUtil.getToken(context)
        if (token.isEmpty()) return
        else request!!.headers(head, token)
        if (refresh != null)
            refresh.post {
                if (!refresh.isRefreshing)
                    refresh.isRefreshing = true
            }

    }

    override fun onSuccess(response: Response<String>?) {
        if (response!!.code() == 200) {
            if (response.body() != null) {
                val obj = JSONObject(response.body())
                val message = obj.optString("msg")
                val code = obj.optString("code")
                    if (code == "0") {
                        val result = obj.optString("result")
                        if (TextUtils.isEmpty(result)) {
                            callbackNormal.success("")
                        } else {
                            when {
                                result[0].equals("{".toCharArray()[0], true) -> callbackNormal.success(result)
                                result[0].equals("[".toCharArray()[0], true) -> callbackNormal.success("{\"result\":$result}")
                                else -> callbackNormal.success(result)
                            }
                        }
                    } else {
                        ManagerUtils.showToast(context, message)
                        callbackNormal.err(message)
                    }
            }
        } else {
            ManagerUtils.showToast(context, NET_ERR)
            callbackNormal.err(NET_ERR)
        }
        if (null != refresh && refresh.isRefreshing) refresh.isRefreshing = false
    }

    override fun onError(response: Response<String>?) {
        if (null != refresh && refresh.isRefreshing) refresh.isRefreshing = false
        ManagerUtils.showToast(context, NET_ERR)
        callbackNormal.err(NET_ERR)
    }

}