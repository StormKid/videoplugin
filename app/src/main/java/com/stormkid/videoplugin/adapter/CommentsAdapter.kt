package com.stormkid.videoplugin.adapter

import android.content.Context
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.moudle.basetool.utils.ImageUtil
import com.stormkid.videoplugin.R
import com.stormkid.videoplugin.RecordComment
import com.xiami.repairg.widget.AutoRecyclerAdapter

/**
 * Created by ke_li on 2018/3/8.
 */
class CommentsAdapter(private val context: Context, private var list : MutableList<RecordComment>):AutoRecyclerAdapter(context) {
    override fun getResourceId(): Int  = R.layout.item_comment
    override fun setViewHolder(view: View): AutoViewHolder = CommentHolder(view)

    override fun getItemCount(): Int = list.size

    fun update(list: MutableList<RecordComment>){
        this.list = list
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: AutoViewHolder, position: Int) {
        val holder = holder as CommentHolder
        val recordComment = list[position]
        val content = recordComment.content
        val timeStr = recordComment.timeStr
        val replyUserName = recordComment.replyUserName
        ImageUtil(context).setUrl(recordComment.userImg).setErr(R.mipmap.cl01).withView(holder.phoeo).getCircleImage()
        if (TextUtils.isEmpty(replyUserName)) holder.repeat.visibility = View.GONE
        else holder.repeat.visibility = View.VISIBLE
        holder.value.text = content
        holder.name.text = recordComment.userName
        holder.time.text = timeStr
        holder.repeat.text = "@$replyUserName"
    }

    inner class CommentHolder(view: View) : AutoViewHolder(view){
        val name = view.findViewById<TextView>(R.id.comment_name)
        val phoeo = view.findViewById<ImageView>(R.id.comment_photo)
        val time = view.findViewById<TextView>(R.id.comment_time)
        val value = view.findViewById<TextView>(R.id.comment_value)
        val repeat = view.findViewById<TextView>(R.id.repeat_name)
    }
}