package com.stormkid.videoplugin.widget

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.CheckBox
import android.widget.ExpandableListView
import android.widget.TextView
import com.moudle.basetool.ui.SelfMainPop
import com.stormkid.videoplugin.CateGroyBean
import com.stormkid.videoplugin.CateGroyEntity
import com.stormkid.videoplugin.ModelUtil
import com.stormkid.videoplugin.R
import com.stormkid.videoplugin.utils.Constants


/**
 * Created by ke_li on 2018/2/28.
 */
class SelectorPop(val context: Context, val entity: CateGroyEntity, mapParams: MutableMap<String, MutableList<CateGroyBean>>?) : SelfMainPop(context) {
    override fun initLayout(): Int = R.layout.select_pop
    private val selectedMap = mutableMapOf<String, CateGroyBean>()
    private var readMap = if (mapParams == null || mapParams.isEmpty()) initCateGroyType() else mapParams
    private lateinit var listener: (MutableMap<String, MutableList<CateGroyBean>>,MutableMap<String,CateGroyBean>) -> Unit

    override fun initPop() {
        val my_cate_groy = contentView.findViewById<ExpandableListView>(R.id.my_cate_groy)
        val do_now = contentView.findViewById<TextView>(R.id.do_now)
        val re_set = contentView.findViewById<TextView>(R.id.re_set)
        val check = contentView.findViewById<CheckBox>(R.id.check)
        val adaper = MySelectAdapter(readMap)
        my_cate_groy.setAdapter(adaper)
        my_cate_groy.setGroupIndicator(null)

        re_set.setOnClickListener {
            readMap = initCateGroyType()
            adaper.update(readMap)
        }
        do_now.setOnClickListener {
            val myCheckResult = if (check.isChecked) 1 else 0
            selectedMap["check"] = CateGroyBean("$myCheckResult","","",Constants.type,check.isChecked)
            listener.invoke(readMap,selectedMap)
            dismiss()
        }
    }

    private fun initCateGroyType() = mutableMapOf<String, MutableList<CateGroyBean>>().let {
        val sys_course_type = ModelUtil.getCateGroyBeanList(entity.sys_course_type, Constants.sys_course_type,Constants.courseType)
        val sys_grade = ModelUtil.getCateGroyBeanList(entity.sys_grade, Constants.sys_grade,Constants.gradeId)
        val sys_subject = ModelUtil.getCateGroyBeanList(entity.sys_subject, Constants.sys_subject,Constants.subjectId)
        it[Constants.sys_grade] = sys_grade
        it[Constants.sys_course_type] = sys_course_type
        it[Constants.sys_subject] = sys_subject
        it
    }


    inner class MySelectAdapter(var map: MutableMap<String, MutableList<CateGroyBean>>) : BaseExpandableListAdapter() {

        private val keys = arrayListOf(Constants.sys_subject, Constants.sys_grade, Constants.sys_course_type)

        fun update(map: MutableMap<String, MutableList<CateGroyBean>>){
            this.map = map
            notifyDataSetChanged()
        }

        override fun getGroup(groupPosition: Int) = keys[groupPosition]


        override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean = false

        override fun hasStableIds(): Boolean = false

        override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View {
            return LayoutInflater.from(context).inflate(R.layout.item_single, parent, false).apply {
                val singleText = this.findViewById<TextView>(R.id.single_text)
                singleText.gravity = left
                singleText.text = keys[groupPosition]
            }
        }

        override fun getChildrenCount(groupPosition: Int): Int = 1

        override fun getChild(groupPosition: Int, childPosition: Int): Any = map[keys[groupPosition]]!![childPosition]

        override fun getGroupId(groupPosition: Int): Long = groupPosition.toLong()

        override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup?): View {
            return LayoutInflater.from(context).inflate(R.layout.item_child, parent, false).apply {
                val child_tag = this.findViewById<SelectTagView>(R.id.child_tag)
                child_tag.setType(SelectTagView.SINGLE_SELECT_TYPE)
                child_tag.initChild(map[keys[groupPosition]]!!, { cateGroyBean ->
                    selectedMap[cateGroyBean.subject] = cateGroyBean
                })

            }
        }

        override fun getChildId(groupPosition: Int, childPosition: Int): Long = childPosition.toLong()

        override fun getGroupCount(): Int = keys.size

    }

    fun getOk(listener: (MutableMap<String, MutableList<CateGroyBean>>,MutableMap<String,CateGroyBean>) -> Unit) {
        this.listener = listener
    }

}