package com.stormkid.videoplugin

import java.io.Serializable

/**
 * Created by ke_li on 2017/12/1.
 */
data class ProgressData(var time: Long,var progress:Int)
data class RebackData( var hls:Container, var rtmp:Container,var type:Int)
data class Container(var  host:String,var app:String,var path:String)
/**
 * subject ： 类别
 */
data class CateGroyBean(val id:String, val name:String, val subject:String,val flag:String, var isChoose:Boolean)

/**
 * 所有的选择分类
 */
data class CateGroyEntity(val sys_subject:MutableMap<String,String>,val sys_grade:MutableMap<String,String>
                          ,val sys_course_type:MutableMap<String,String>)
    :Serializable



object ModelUtil{
    fun getCateGroyBeanList(map: MutableMap<String,String>,tag:String,flag: String)= arrayListOf<CateGroyBean>().apply {
        map.forEach {
            val bean = CateGroyBean(it.key,it.value,tag,flag,false)
            this.add(bean)
        }
    }
}