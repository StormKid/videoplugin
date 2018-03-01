package com.moudle.basetool.utils

import android.content.Context
import android.os.Build
import java.util.*

/**
 * Created by ke_li on 2017/12/28.
 */
object ApkUtil {
    fun getVersion(context:Context):String{
        var version = "1.0.0"
        try {
            val pm = context.packageManager
            val pi = pm.getPackageInfo(context.packageName, 0)
            version = pi.versionName
        } catch (e: Exception) {
            LogUtil.logE(e.stackTrace.toString())
        }
        return version
    }

    /**
     * 获取该手机或者平板信息
     * @return
     */
    fun getUniquePsuedoID(): String {
        var serial: String

        val m_szDevIDShort = "35" +
                Build.BOARD.length % 10 + Build.BRAND.length % 10 +

                Build.CPU_ABI.length % 10 + Build.DEVICE.length % 10 +

                Build.DISPLAY.length % 10 + Build.HOST.length % 10 +

                Build.ID.length % 10 + Build.MANUFACTURER.length % 10 +

                Build.MODEL.length % 10 + Build.PRODUCT.length % 10 +

                Build.TAGS.length % 10 + Build.TYPE.length % 10 +

                Build.USER.length % 10 //13 位

        try {
            serial = android.os.Build::class.java.getField("SERIAL").get(null).toString()
            //API>=9 使用serial号
            return UUID(m_szDevIDShort.hashCode().toLong(), serial.hashCode().toLong()).toString()
        } catch (exception: Exception) {
            //serial需要一个初始化
            serial = "serial" // 随便一个初始化
        }

        //使用硬件信息拼凑出来的15位号码
        return UUID(m_szDevIDShort.hashCode().toLong(), serial.hashCode().toLong()).toString()
    }
}