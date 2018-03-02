package com.stormkid.videoplugin.activity

import android.content.res.Configuration
import android.view.KeyEvent
import android.widget.ImageView
import com.moudle.basetool.BaseActivity
import com.moudle.ijkplayer.IjkPlayerView
import com.stormkid.videoplugin.CateGroyBean
import com.stormkid.videoplugin.R
import kotlinx.android.synthetic.main.activity_video.*

/**
 * Created by ke_li on 2018/3/2.
 */
class VideoActivity:BaseActivity() {
    override fun getLayout(): Int  = R.layout.activity_video

    override fun initView() {
        tag_P.initChild(arrayListOf(CateGroyBean("0","1p","","",true),
                CateGroyBean("1","2p","","",false),
                CateGroyBean("2","3p","","",false)
                ),{

        })
        play_view.init(this).enableOrientation()
                .setVideoSource(null, "", null, null, null)
                .setMediaQuality(IjkPlayerView.MEDIA_QUALITY_HIGH)
        play_view.mPlayerThumb.scaleType = ImageView.ScaleType.CENTER_CROP
    }

    override fun initEvent() {
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