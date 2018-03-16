package com.stormkid.videoplugin.activity

import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import com.moudle.basetool.BaseActivity
import com.moudle.basetool.net.MyNormalNetCallback
import com.moudle.basetool.net.OkTools
import com.moudle.basetool.utils.ManagerUtils
import com.stormkid.videoplugin.R
import com.stormkid.videoplugin.utils.NetConnectConstants
import kotlinx.android.synthetic.main.activity_login.*

/**
 * Created by ke_li on 2018/3/16.
 */
class LoginActivity : BaseActivity() {
    override fun getLayout(): Int = R.layout.activity_login

    override fun initView() {

    }

    override fun initEvent() {
        xx_pwd.setOnClickListener { put_pwd.setText("") }
        xx_username.setOnClickListener { put_username.setText("") }
        addDelete(put_pwd)
        addDelete(put_username)
        to_login.setOnClickListener {
            when {
                TextUtils.isEmpty(put_username.text) -> ManagerUtils.showToast(this@LoginActivity,"请输入用户名")
                TextUtils.isEmpty(put_pwd.text) -> ManagerUtils.showToast(this@LoginActivity,"请输入密码")
                else -> {
                    val params = hashMapOf(Pair("loginName",put_username.text.toString()),Pair("password",put_pwd.text.toString()))
                    OkTools.builder().setTag(this).setUrl(NetConnectConstants.login).setParams(params).build(this@LoginActivity).post(object : MyNormalNetCallback{
                        override fun success(any: String) {
                            ManagerUtils.startActivity(this@LoginActivity,MainActivity::class.java)
                        }

                        override fun err(msg: String) {
                        }

                    })
                }
            }
        }
    }


    private fun addDelete(editText: EditText){
        editText.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                when (editText.id){
                    xx_pwd.id ->if (!TextUtils.isEmpty(s))xx_pwd.visibility=View.VISIBLE
                    xx_username.id ->if (!TextUtils.isEmpty(s))xx_username.visibility=View.VISIBLE
                }
            }

        })
    }


}