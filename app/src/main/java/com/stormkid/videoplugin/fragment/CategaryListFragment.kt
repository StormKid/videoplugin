package com.stormkid.videoplugin.fragment

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import com.flyco.tablayout.listener.OnTabSelectListener
import com.moudle.basetool.BaseFragment
import com.moudle.basetool.net.MyNormalNetCallback
import com.moudle.basetool.net.OkTools
import com.moudle.basetool.refresh.OnRefreshListener
import com.moudle.basetool.refresh.PullRefreshLayout
import com.moudle.basetool.utils.JsonUtil
import com.moudle.basetool.utils.ManagerUtils
import com.stormkid.videoplugin.*
import com.stormkid.videoplugin.adapter.CourseListAdapter
import com.stormkid.videoplugin.utils.Constants
import com.stormkid.videoplugin.utils.NetConnectConstants
import com.stormkid.videoplugin.widget.CateGroyPop
import com.stormkid.videoplugin.widget.SelectorPop
import kotlinx.android.synthetic.main.fragment_categary_list.*

/**
 * Created by ke_li on 2018/2/27.
 */
class CategaryListFragment : BaseFragment() {
    lateinit var entity: CateGroyEntity
    private var index = 1
    private lateinit var myContext: Context
    private lateinit var adapter: CourseListAdapter
    private var  isLoading = false
    private var courseList = arrayListOf<Record>()
    private val params = mutableMapOf<String,String>()
    override fun getLayout(): Int = R.layout.fragment_categary_list

    override fun initData() {
        val map = mutableMapOf(Pair("pageSize","10")
            , Pair("index","$index")
        )
        map.putAll(params)

        OkTools.builder().setTag(this).setUrl(NetConnectConstants.course_list).setParams(map).build(myContext).get(object :MyNormalNetCallback{
            override fun success(any: String) {
                refresh.onRefreshComplete()
               val courseListBean = JsonUtil.from(any, CourseListEntity::class.java)

               if (isLoading){
                   if (index > courseListBean.pages){
                       index -=1
                       ManagerUtils.showToast(myContext,Constants.footErr)
                   }else{
                       adapter.loadMore(courseListBean.records,isLoading)
                   }
               }else{
                   adapter.loadMore(courseListBean.records,isLoading)
               }
            }

            override fun err(msg: String) {
                refresh.onRefreshComplete()
            }

        })


    }

    companion object {
        fun getInstance(entity: CateGroyEntity,context: Context):CategaryListFragment{
          return  CategaryListFragment().apply {
              this.entity = entity
              this.myContext = context
          }
        }
    }

    override fun initView() {
        choose_select.setTabData(arrayOf("排序", "筛选"))
        choose_select.dispatchSetSelected(false)
        choose_select.isSelected = false
        GridLayoutManager(myContext,2).apply {
            course_list.layoutManager = this
        }
        adapter =  CourseListAdapter(myContext,courseList)
        course_list.adapter = adapter
        cleanStyle()

    }


    private val listSingle = arrayListOf(
            CateGroyBean("time", "按时间排序", "sss",Constants.sortType, true),
            CateGroyBean("popularity", "按人气排序", "sss", Constants.sortType,false),
            CateGroyBean("hot", "按热度进行排序", "sss", Constants.sortType,false)
    )


    private var changeMap : MutableMap<String,MutableList<CateGroyBean>> = mutableMapOf()

    private fun initSelect(position: Int) {
        showStyle()
        val pop = CateGroyPop(activity!!, listSingle).apply { this.setListener {
            params[it.flag] = it.id
            index = 1
            isLoading = false
            initData()
        } }
        val select = SelectorPop(activity!!,entity,changeMap).apply {
            this.getOk { readMap, selectedMap -> changeMap = readMap
                params.clear()
                selectedMap.keys.forEach {
                    val cateGroyBean = selectedMap[it]
                    params[cateGroyBean!!.flag] = cateGroyBean.id
                    index = 1
                    isLoading = false
                    initData()
                }
            }
            this.setOnDismissListener { cleanStyle() }
        }
        pop.setOnDismissListener { cleanStyle() }
        when (position) {
            0 -> {
                pop.showPopupWindow(choose_select)
                select.dismiss()
            } //三个排序
            1 -> {
                pop.dismiss()
                select.showPopupWindow(choose_select)
            } //进入筛选
        }
    }


    private fun cleanStyle() {
        choose_select.textSelectColor = ContextCompat.getColor(activity!!, R.color.text_666)
        choose_select.indicatorColor = ContextCompat.getColor(activity!!, android.R.color.transparent)
    }

    private fun showStyle() {
        choose_select.textSelectColor = ContextCompat.getColor(activity!!, R.color.color_main)
        choose_select.indicatorColor = ContextCompat.getColor(activity!!, R.color.color_main_trans)
    }


    override fun initEvent() {
        choose_select.setOnTabSelectListener(object : OnTabSelectListener {
            override fun onTabSelect(position: Int) {
                initSelect(position)
            }

            override fun onTabReselect(position: Int) {
                initSelect(position)
            }

        })

        refresh.setMode(PullRefreshLayout.BOTH)
        refresh.setOnRefreshListener(object : OnRefreshListener{
            override fun onPullDownRefresh() {
                index = 1
                isLoading = false
                initData()
            }

            override fun onPullUpRefresh() {
                index +=1
                isLoading = true
                initData()
            }

        })
    }
}