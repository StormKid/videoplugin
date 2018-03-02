package com.stormkid.videoplugin.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.moudle.basetool.utils.ImageUtil
import com.moudle.basetool.utils.ThreadLocalDateUtil
import com.stormkid.videoplugin.R
import com.stormkid.videoplugin.Record
import com.xiami.repairg.widget.AutoRecyclerAdapter

/**
 * Created by ke_li on 2018/3/2.
 */
class CourseListAdapter(val context: Context, var mutableList: MutableList<Record>) : AutoRecyclerAdapter(context) {
    override fun getResourceId(): Int = R.layout.item_course
    override fun setViewHolder(view: View): AutoViewHolder = ViewHodler(view)

    override fun getItemCount(): Int = mutableList.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: AutoViewHolder, position: Int) {
        val hodler = holder as ViewHodler
        val record = mutableList[position]
        val courseName = record.courseName
        val imgUrl = record.imgUrl
        val endTime = record.endTime
        val startTime = record.startTime
        hodler.comments.text = record.comments
        hodler.courseName.text = record.gradeName + ":$courseName"
        hodler.courseType.text = record.subjectName
        hodler.watchPeople.text = record.watchTimes
        ImageUtil(context).setUrl(imgUrl).withView(hodler.courseImg).setErr(R.mipmap.cl01).getNormalImage()
        val startReal = ThreadLocalDateUtil.longToString(startTime, ThreadLocalDateUtil.date_format)
        val endReal = ThreadLocalDateUtil.longToString(endTime, ThreadLocalDateUtil.time_format)
        hodler.courseTime.text = startReal + "- $endReal"
        hodler.schoolName.text = record.schoolName
        if (record.isBoutique == "1") hodler.jingping.visibility = View.VISIBLE
        else hodler.jingping.visibility = View.GONE

}

fun loadMore(mutableList: MutableList<Record>, boolean: Boolean) {
    if (!boolean) this.mutableList.clear()
    this.mutableList.addAll(mutableList)
    notifyDataSetChanged()
}


class ViewHodler(view: View) : AutoViewHolder(view) {
    val courseImg = view.findViewById<ImageView>(R.id.course_img)
    val courseName = view.findViewById<TextView>(R.id.course_name)
    val courseTime = view.findViewById<TextView>(R.id.course_time)
    val courseType = view.findViewById<TextView>(R.id.course_type)
    val comments = view.findViewById<TextView>(R.id.comments)
    val schoolName = view.findViewById<TextView>(R.id.school_name)
    val watchPeople = view.findViewById<TextView>(R.id.watch_people)
    val jingping = view.findViewById<TextView>(R.id.jingping)
}
}