package com.xiami.repairg.widget

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zhy.autolayout.utils.AutoUtils

/**
 * Created by ke_li on 2018/1/8.
 */
abstract class AutoRecyclerAdapter(private val context: Context) : Adapter<AutoRecyclerAdapter.AutoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AutoViewHolder {
        val view = LayoutInflater.from(context).inflate(getResourceId(), parent, false)
        return setViewHolder(view)
    }
    abstract fun getResourceId(): Int


    abstract fun setViewHolder(view: View):AutoViewHolder

   open class AutoViewHolder(val view:View):RecyclerView.ViewHolder(view){
        init {
            AutoUtils.autoSize(view)
        }
    }
}