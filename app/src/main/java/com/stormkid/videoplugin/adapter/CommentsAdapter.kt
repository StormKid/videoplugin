package com.stormkid.videoplugin.adapter

import android.content.Context
import android.view.View
import com.stormkid.videoplugin.R
import com.xiami.repairg.widget.AutoRecyclerAdapter

/**
 * Created by ke_li on 2018/3/8.
 */
class CommentsAdapter( context: Context):AutoRecyclerAdapter(context) {
    override fun getResourceId(): Int  = R.layout.item_comment
    override fun setViewHolder(view: View): AutoViewHolder = CommentHolder(view)

    override fun getItemCount(): Int = 5

    override fun onBindViewHolder(holder: AutoViewHolder, position: Int) {
        val holder = holder as CommentHolder
    }

    inner class CommentHolder(view: View) : AutoViewHolder(view){

    }
}