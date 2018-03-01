package com.stormkid.videoplugin.widget

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.moudle.basetool.ui.SelfMainPop
import com.stormkid.videoplugin.CateGroyBean
import com.stormkid.videoplugin.R
import com.xiami.repairg.widget.AutoRecyclerAdapter

/**
 * Created by ke_li on 2018/2/27.
 */
class CateGroyPop( var myContext:Context , val list: MutableList<CateGroyBean>) : SelfMainPop(myContext) {

    private var selecPosition = 0

    override fun initLayout(): Int = R.layout.pop_single

    override fun initPop() {
        val show_single = contentView.findViewById<RecyclerView>(R.id.show_single)
        val manager = LinearLayoutManager(myContext).let {
            it.orientation = LinearLayoutManager.VERTICAL
            it
        }
        show_single.layoutManager = manager

        list.forEachIndexed{position,bean->
            if (bean.isChoose) selecPosition = position
        }
        show_single.adapter = SingleAdapter(myContext).let {
            it.setOnItemClick {  position ->
                val selectHodler = show_single.findViewHolderForLayoutPosition(selecPosition) as SingleHolder?
                if (selectHodler!=null){
                    selectHodler.single_text.setTextColor(ContextCompat.getColor(myContext,R.color.text_666))
                }else{
                    try {
                        it.notifyItemChanged(selecPosition)
                    }catch (e:Exception){

                    }
                }
                list[selecPosition].isChoose = false
                selecPosition = position
                list[selecPosition].isChoose = true
                it.notifyItemChanged(selecPosition)
                dismiss()
            }
            it
        }
    }

    inner class SingleAdapter( context: Context ) : AutoRecyclerAdapter(context){

        lateinit var listener: (position:Int)->Unit
        override fun getResourceId(): Int = R.layout.item_single

        override fun setViewHolder(view: View): AutoViewHolder = SingleHolder(view)

        override fun getItemCount(): Int = list.size

        override fun onBindViewHolder(holder: AutoViewHolder, position: Int) {
            val vH = holder as SingleHolder
            val bean = list[position]
            vH.single_text.text = bean.name
            if (bean.isChoose) vH.single_text.setTextColor(ContextCompat.getColor(myContext,R.color.color_main))
            else  vH.single_text.setTextColor(ContextCompat.getColor(myContext,R.color.text_666))
            vH.itemView.setOnClickListener {
                listener.invoke(position)
            }
        }

         fun setOnItemClick(listener: (position:Int)->Unit){
            this.listener  = listener
        }

    }



    class SingleHolder(view: View) : AutoRecyclerAdapter.AutoViewHolder(view){
         val single_text = view.findViewById<TextView>(R.id.single_text)
    }

}