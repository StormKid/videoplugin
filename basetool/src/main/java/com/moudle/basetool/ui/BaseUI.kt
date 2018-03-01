package com.moudle.basetool

import android.app.Application
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lzy.okgo.OkGo
import com.lzy.okgo.https.HttpsUtils
import com.lzy.okgo.interceptor.HttpLoggingInterceptor
import com.umeng.commonsdk.UMConfigure
import com.zhy.autolayout.AutoLayoutActivity
import okhttp3.OkHttpClient
import org.simple.eventbus.EventBus
import java.util.concurrent.TimeUnit
import java.util.logging.Level


/**
 * Created by ke_li on 2017/12/15.
 */
abstract class BaseActivity : AutoLayoutActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        UMConfigure.setEncryptEnabled(false)
        EventBus.getDefault().register(this)
        setContentView(getLayout())
        initView()
        initEvent()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    @LayoutRes
    protected abstract fun getLayout(): Int

    protected abstract fun initView()

    protected abstract fun initEvent()



}

/**
 * fragment里面申请权限请在fragment里面添加接口回调
 */
abstract class BaseFragment : Fragment() {

    private var isMeet = true
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayout(), container, false)
    }


    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser && isMeet) {
            initData()
            isMeet = false
        }
    }


    @LayoutRes
    protected abstract fun getLayout(): Int


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        EventBus.getDefault().register(this)
        initView()
        initEvent()
    }

    protected abstract fun initData()
    protected abstract fun initView()
    protected abstract fun initEvent()


    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

}

/**
 * TODO 这里进行各种初始化处理
 */
abstract class BaseApplication : Application() {

    private val TIME_OUT: Long = 10

    override fun onCreate() {
        super.onCreate()
        //更新友盟统计
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, "58d2886b3eae2509c3000b7d")
        val ok = initOkHttpClient()
        OkGo.getInstance().init(this).setRetryCount(3)
                .okHttpClient = ok

    }


    private fun initOkHttpClient(): OkHttpClient {
        var client: OkHttpClient.Builder = OkHttpClient.Builder()
        val log = HttpLoggingInterceptor("okgo")
        // 信任所有证书的方式，比较危险
        // TODO 写https证书配置方式
        val sslParams = HttpsUtils.getSslSocketFactory()
        log.setPrintLevel(HttpLoggingInterceptor.Level.BODY)
        log.setColorLevel(Level.WARNING)
        client.addInterceptor(log)
                .readTimeout(TIME_OUT, TimeUnit.SECONDS)
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
        return client.build()
    }
}


