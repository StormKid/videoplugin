package com.stormkid.videoplugin.activity

import android.app.Activity
import android.content.Intent
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.Gravity
import com.google.gson.Gson
import com.moudle.basetool.BaseActivity
import com.moudle.basetool.net.MyNormalNetCallback
import com.moudle.basetool.net.OkTools
import com.moudle.basetool.utils.JsonUtil
import com.moudle.basetool.utils.ManagerUtils
import com.moudle.basetool.utils.PermissionUtil
import com.moudle.zxing.CaptureActivity
import com.stormkid.videoplugin.CateGroyEntity
import com.stormkid.videoplugin.R
import com.stormkid.videoplugin.RebackData
import com.stormkid.videoplugin.adapter.CirclePageAdapter
import com.stormkid.videoplugin.fragment.CategaryListFragment
import com.stormkid.videoplugin.utils.NetConnectConstants
import com.yanzhenjie.permission.Permission
import kotlinx.android.synthetic.main.activity_main.*
import org.simple.eventbus.EventBus

class MainActivity : BaseActivity() {
    private val requestCode = 100
    override fun getLayout(): Int = R.layout.activity_main

    override fun initView() {

        val params = mutableMapOf(Pair("keys","sys_subject,sys_grade,sys_course_type"))
        val fragments = arrayListOf<Fragment>()
        val adapter = CirclePageAdapter(supportFragmentManager,fragments)

        OkTools.builder().setTag(this).setUrl(NetConnectConstants.category_type).setParams(params).build(this).get(object : MyNormalNetCallback{
            override fun success(any: String) {
                val bean = JsonUtil.from(any,CateGroyEntity::class.java)
                scroll_contain.adapter = adapter
                fragments.add(CategaryListFragment.getInstance(bean,this@MainActivity))
                adapter.notifyDataSetChanged()
            }

            override fun err(msg: String) {
            }

        })


    }

    override fun initEvent() {
        main_top.setRightImgClickListener {
            PermissionUtil(this).excuteSelfPermission({ startActivityForResult(Intent(this, CaptureActivity::class.java)
                    , requestCode) }, Permission.CAMERA)
        }
        search_bar.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val value = s?.toString()
                EventBus.getDefault().post(value,"search")
            }
        })

        main_top.setLeftImgClickListener {
            draw_layout.openDrawer(Gravity.START)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK&&requestCode == this.requestCode){
            val bundle = data!!.extras
            if (null != bundle) {
                val result = bundle.getString("result")
                if (TextUtils.isEmpty(result))
                    ManagerUtils.showToast(this, "未扫到")
                else {
                    try {
                        val bean  =   Gson().fromJson(result, RebackData::class.java)
                        val url  = "http://"+bean.hls.host+"/"+bean.hls.app+"/"+bean.hls.path+".m3u8"
                        if (url.isEmpty()) return
                         val intent : Intent = Intent(this,VideoActivity::class.java)
                        intent.putExtra("url",url)
                        startActivity(intent)
                    }catch (e : Exception){
                        ManagerUtils.showToast(this,"解析数据出错")
                    }
                }
            }
        }
    }

}
