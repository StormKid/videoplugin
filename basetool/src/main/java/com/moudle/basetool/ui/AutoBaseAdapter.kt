package com.xiami.repairg.widget

import android.content.Context
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.zhy.autolayout.utils.AutoUtils

/**
 * Created by ke_li on 2018/1/23.
 */
abstract class AutoBaseAdapter<M>(open val context: Context, open val arrayList: ArrayList<M>) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View = run{
        var view = convertView
        val viewholder: Any?
        if (view == null) {
            view = LayoutInflater.from(context).inflate(getLayout(), parent, false)
            viewholder = ViewHolder(view)
        } else
            viewholder = view.tag
        AutoUtils.autoSize(view)
        initView(view!!, position)
        return view
    }


    abstract fun getLayout(): Int

    abstract fun initView(view: View, position: Int)


    inner class ViewHolder(view: View) {
        init {
            view.tag = this
        }
    }


    /**
     * 代替viewHolder
     */
    fun <T : View> View.findViewOften(viewId: Int): T {
        var viewHolder: SparseArray<View> = tag as? SparseArray<View> ?: SparseArray()
        tag = viewHolder
        var childView: View? = viewHolder.get(viewId)
        if (null == childView) {
            childView = findViewById(viewId)
            viewHolder.put(viewId, childView)
        }
        return childView as T
    }

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = arrayList.size

    override fun getItem(position: Int): M = arrayList[position]

}