package com.stormkid.videoplugin.activity

import android.content.res.Configuration
import android.os.Handler
import android.text.TextUtils
import android.view.KeyEvent
import android.widget.ImageView
import com.moudle.basetool.BaseActivity
import com.moudle.basetool.net.MyNormalNetCallback
import com.moudle.basetool.net.OkTools
import com.moudle.basetool.refresh.OnRefreshListener
import com.moudle.basetool.refresh.PullRefreshLayout
import com.moudle.basetool.utils.JsonUtil
import com.moudle.ijkplayer.IjkPlayerView
import com.stormkid.videoplugin.CateGroyBean
import com.stormkid.videoplugin.R
import com.stormkid.videoplugin.Video
import com.stormkid.videoplugin.VideoEntity
import com.stormkid.videoplugin.utils.NetConnectConstants
import kotlinx.android.synthetic.main.activity_video.*

/**
 * Created by ke_li on 2018/3/2.
 */
class VideoActivity:BaseActivity() {
    val myVideoUrls = arrayListOf<Video>()
    override fun getLayout(): Int  = R.layout.activity_video

    override fun initView() {
        val url = intent.getStringExtra("url")
        if (TextUtils.isEmpty(url)) {
            val id = intent.getStringExtra(this.javaClass.name)
            //通过ID网络请求
            initNet(id)
        }else{
           initPlayView(url,"P1")
        }

    }

    private fun initPlayView(url:String,title:String){
        play_view.init(this)
                .setTitle(title)
                .enableOrientation()
                .setVideoSource(null, url, null, null, null)
                .setMediaQuality(IjkPlayerView.MEDIA_QUALITY_HIGH)
        play_view.mPlayerThumb.scaleType = ImageView.ScaleType.CENTER_CROP
    }


    private fun initVideo(){
        if (myVideoUrls.isEmpty()) {
            play_view.init(this@VideoActivity).stop()
            finish()
            return
        }
        initPlayView(myVideoUrls[0].httpUrl,"P1")
        val list = arrayListOf<CateGroyBean>()
        myVideoUrls.forEachIndexed { index: Int, video: Video ->
            val getP = index+1
            CateGroyBean("$index","$getP"+"p",video.httpUrl,"", index==0).apply {
                list.add(this)
            }

        }
        tag_P.initChild(list,{
            initPlayView(it.subject,it.name)
            play_view.start()
        })
    }


    private fun initNet(id: String?) {
        if (TextUtils.isEmpty(id))return
        myVideoUrls.clear()
        OkTools.builder().setUrl(NetConnectConstants.video_list)
                .setTag(this).setParams(hashMapOf(Pair("courseId",id!!))).build(this).get(object : MyNormalNetCallback{
                    override fun success(any: String) {
                        val entity = JsonUtil.from(any,VideoEntity::class.java)
                        myVideoUrls.addAll(entity.videos)
                        initVideo()
                    }

                    override fun err(msg: String) {
                       play_view.init(this@VideoActivity).stop()
                        finish()
                    }

                })

    }

    override fun initEvent() {
        refreshLayout.setMode(PullRefreshLayout.BOTH)
        refreshLayout.setOnRefreshListener(object : OnRefreshListener{
            override fun onPullDownRefresh() {
                Handler().post { refreshLayout.onRefreshComplete() }
            }

            override fun onPullUpRefresh() {
                Handler().post { refreshLayout.onRefreshComplete() }
            }

        })
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (play_view.handleVolumeKey(keyCode)) {
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onBackPressed() {
        if (play_view.onBackPressed()) {
            return
        }
        super.onBackPressed()
    }

    override fun onPause() {
        super.onPause()
        play_view.onPause()
    }

    override fun onResume() {
        super.onResume()
        play_view.onResume()
    }


    override fun onDestroy() {
        super.onDestroy()
        play_view.onDestroy()
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        play_view.configurationChanged(newConfig)
    }
}