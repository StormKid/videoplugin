package com.moudle.basetool.net

import android.annotation.SuppressLint
import android.content.Context
import android.support.v4.widget.SwipeRefreshLayout
import com.lzy.okgo.OkGo
import com.lzy.okgo.callback.FileCallback
import com.lzy.okgo.model.Progress
import com.lzy.okgo.model.Response
import com.moudle.basetool.net.OkTools.inner.BASE_URL
import com.moudle.basetool.net.OkTools.inner.file
import com.moudle.basetool.net.OkTools.inner.fileKey
import com.moudle.basetool.net.OkTools.inner.fileList
import com.moudle.basetool.net.OkTools.inner.filesKey
import com.moudle.basetool.net.OkTools.inner.params
import com.moudle.basetool.net.OkTools.inner.refresh
import com.moudle.basetool.net.OkTools.inner.url
import com.moudle.basetool.utils.JsonUtil
import com.moudle.basetool.utils.LogUtil
import com.moudle.basetool.utils.LogUtil.Companion.tag
import java.io.File

/**
 * Created by like on 2017/12/17.
 */
class OkTools private constructor() {


    companion object {
        @JvmStatic
        fun builder(): Builder {
            return Builder()
        }

        @JvmStatic
        private fun getInstance(context: Context): OkTools {
            inner.myContext = context
            return inner.instance
        }
    }

    @SuppressLint("StaticFieldLeak")
    private object inner {
        val instance = OkTools()
        lateinit var url: String
        lateinit var tag: Any
        lateinit var params: Map<String, String>
        lateinit var fileKey: String
        lateinit var filesKey: String
        lateinit var file: File
        lateinit var fileList: List<File>
        lateinit var requestBody: String
        lateinit var myContext: Context
        lateinit var BASE_URL: String
        lateinit var refresh: SwipeRefreshLayout
    }


    class Builder {
        //初始化
        init {
            inner.tag = ""
            inner.params = HashMap()
            inner.fileKey = "file"
            inner.filesKey = "files"
            inner.file = File("")
            inner.fileList = ArrayList()
            inner.requestBody = ""
            inner.BASE_URL = "http://172.16.80.100:93/ylb/api/"
            inner.url = ""
        }

        fun setBase(baseUrl: String): Builder {
            inner.BASE_URL = baseUrl
            return this
        }


        fun setUrl(url: String): Builder {
            inner.url = url
            return this
        }

        fun setTag(tag: Any): Builder {
            inner.tag = tag
            return this
        }


        fun setParams(params: Map<String, String>): Builder {
            inner.params = params
            return this
        }

        fun setFileKey(fileKey: String): Builder {
            inner.fileKey = fileKey
            return this
        }

        fun setFilesKey(filesKey: String): Builder {
            inner.filesKey = filesKey
            return this
        }

        fun putFile(file: File): Builder {
            inner.file = file
            return this
        }


        fun putFiles(fileList: List<File>): Builder {
            inner.fileList = fileList
            return this
        }

        fun putRequestBody(body: Any): Builder {
            inner.requestBody = JsonUtil.to(body)
            return this
        }

        fun build(context: Context): OkTools {
            inner.refresh = SwipeRefreshLayout(context)
            return OkTools.getInstance(context)
        }


    }


    fun setRefresh(refreshLayout: SwipeRefreshLayout?): OkTools {
        if (refreshLayout != null)
            refresh = refreshLayout
        return this
    }


    fun get(callback: MyNormalNetCallback) {
        OkGo.get<String>(BASE_URL + url).tag(tag).params(params).execute(MyIntentCallback(inner.myContext, tag, callback, refresh))
    }

    fun post(callback: MyNormalNetCallback) {
        OkGo.post<String>(BASE_URL + url).tag(tag).run {
            if (inner.params.isEmpty()) upJson(inner.requestBody)
            else params(inner.params)
                    .execute(MyIntentCallback(inner.myContext, LogUtil.tag, callback, refresh))
        }
    }


    fun downloadFile(fileCallBack: MyFileNetCallback) {
        OkGo.post<File>(BASE_URL + url).tag(tag).execute(object : FileCallback() {
            override fun onSuccess(response: Response<File>?) {
                if (response!!.code() == 200) {
                    if (response.body() != null) {
                        fileCallBack.success(response.body())
                    } else {
                        fileCallBack.err("文件为空，请重新选择文件下载")
                    }
                } else {
                    fileCallBack.err("网络不给力，请检查网络链接")
                }
            }

            override fun downloadProgress(progress: Progress?) {
                super.downloadProgress(progress)
                if (progress != null)
                    fileCallBack.progress(progress)
            }

            override fun onFinish() {
                super.onFinish()
                OkGo.getInstance().cancelTag(tag)
            }
        })
    }


    /**
     * 上传多个文件
     */
    fun uploadFiles(callback: MyNormalNetCallback) {
        if (fileList.isEmpty()) return
        OkGo.post<String>(BASE_URL + url).addFileParams(filesKey, fileList).params(params).execute(MyIntentCallback(inner.myContext, tag, callback, refresh))
    }


    /**
     * 单文件上传,默认file
     */
    fun uploadFile(callback: MyNormalNetCallback) {
        if (file.exists())
            OkGo.post<String>(BASE_URL + url).params(fileKey, file).execute(MyIntentCallback(inner.myContext, tag, callback, refresh))
    }

}