package com.stormkid.videoplugin.fragment

import android.support.v4.content.ContextCompat
import com.flyco.tablayout.listener.OnTabSelectListener
import com.moudle.basetool.BaseFragment
import com.stormkid.videoplugin.CateGroyBean
import com.stormkid.videoplugin.CateGroyEntity
import com.stormkid.videoplugin.R
import com.stormkid.videoplugin.utils.Constants
import com.stormkid.videoplugin.widget.CateGroyPop
import com.stormkid.videoplugin.widget.SelectorPop
import kotlinx.android.synthetic.main.fragment_categary_list.*

/**
 * Created by ke_li on 2018/2/27.
 */
class CategaryListFragment : BaseFragment() {
    lateinit var entity: CateGroyEntity

    override fun getLayout(): Int = R.layout.fragment_categary_list

    override fun initData() {
    }

    companion object {
        fun getInstance(entity: CateGroyEntity):CategaryListFragment{
          return  CategaryListFragment().apply {
              this.entity = entity
          }
        }
    }

    override fun initView() {
        choose_select.setTabData(arrayOf("排序", "筛选"))
        choose_select.dispatchSetSelected(false)
        choose_select.isSelected = false
        cleanStyle()

    }


    private val listSingle = arrayListOf(
            CateGroyBean("time", "按时间排序", "sss",Constants.sortType, false),
            CateGroyBean("popularity", "按人气排序", "sss", Constants.sortType,false),
            CateGroyBean("hot", "按热度进行排序", "sss", Constants.sortType,false)
    )


    private var changeMap : MutableMap<String,MutableList<CateGroyBean>> = mutableMapOf()

    private fun initSelect(position: Int) {
        showStyle()
        val pop = CateGroyPop(activity!!, listSingle)
        val select = SelectorPop(activity!!,entity,changeMap).apply {
            this.getOk { readMap, selectedMap -> changeMap = readMap

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
    }
}