package com.moudle.basetool.utils

import android.content.Context
import android.content.Intent
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.widget.TextView
import android.widget.Toast
import java.io.Serializable
import java.math.BigDecimal

/**
 * Created by ke_li on 2017/12/15.
 */
object ManagerUtils {
       fun savePull(recyclerView: RecyclerView, swipeRefreshLayout: SwipeRefreshLayout) {
           recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
               override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                   val topRowVerticalPosition = if (recyclerView == null || recyclerView.childCount == 0) 0 else recyclerView.getChildAt(0).top
                   swipeRefreshLayout.isEnabled = topRowVerticalPosition >= 0
               }

               override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                   super.onScrollStateChanged(recyclerView, newState)
               }
           })
       }

      fun startActivity(context: Context,clazz: Class<*>,params:Any?){
          val intent = Intent()
          intent.setClass(context,clazz)
          when (params) {
              null -> {}
              is String -> intent.putExtra(clazz.name,params)
              is Int -> intent.putExtra(clazz.name,params)
              is Map<*, *> -> {
                  try {
                      val keys = params.keys as Set<String>
                      for (key in keys) {
                          val value = params[key] as Serializable
                          intent.putExtra(key, value)
                      }
                  }catch (e:Exception){
                  }
              }
          }
          context.startActivity(intent)
      }


      fun startActivity(context: Context, clazz: Class<*>){
          startActivity(context,clazz,null)
      }

      fun returnActivity(context: Context,clazz: Class<*>){
          val intent = Intent()
          intent.setClass(context,clazz)
          intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK
          intent.flags= Intent.FLAG_ACTIVITY_NEW_TASK
          context.startActivity(intent)
      }

      fun  showToast(context:Context,msg:String){
          Toast.makeText(context,msg,Toast.LENGTH_SHORT).show()
      }


     fun getTextPx(context: Context, point: Int, textView: TextView) {
        val displayMetrics = context.resources.displayMetrics
        val widthPixels = displayMetrics.widthPixels
        val px = widthPixels * point / 240
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, px.toFloat())
    }


    /**
     * 判断订单状态
     * @param ccid
     * @return
     */

    private const val WILLPAY = "待支付"
    private const val WILLDONE = "待接单"
    private const val DONE = "已接单"
    private const val GONE = "已出发"
    private const val ARRIVED = "已到达"
    private const val FINISH = "已完成"
    private const val CANCEL = "已取消"
    private const val WILLSTART = "未出发"
    private const val PAYDONE = "已支付"

    fun getStatus(ccid: String): StatusBean {
        val bean = StatusBean()
        when (ccid) {
            "created", "not-paid-yet" -> {
                bean.statusCode = 0
                bean.statusName = WILLPAY
            }
            "paid" -> {
                bean.statusCode = 1
                bean.statusName = WILLDONE
            }
            "preaccepted", "accepted" -> {
                bean.statusName = DONE
                bean.statusCode = 2
            }
            "started", "late" -> {
                bean.statusCode = 3
                bean.statusName = GONE
            }
            "arrived" -> {
                bean.statusCode = 4
                bean.statusName = ARRIVED
            }
            "completed" -> {
                bean.statusCode = 5
                bean.statusName = FINISH
            }
            "canceled" -> {
                bean.statusName = CANCEL
                bean.statusCode = 6
            }
        }
        return bean

    }


    fun getEngineStatus(ccid: String): StatusBean {
        val bean = StatusBean()
        when (ccid) {
            "preaccepted", "accepted" -> {
                bean.statusName = WILLSTART
                bean.statusCode = 0
            }
            "started", "late" -> {
                bean.statusCode = 1
                bean.statusName = GONE
            }
            "arrived" -> {
                bean.statusCode = 2
                bean.statusName = ARRIVED
            }
            "completed" -> {
                bean.statusCode = 3
                bean.statusName = FINISH
            }
            "canceled" -> {
                bean.statusName = CANCEL
                bean.statusCode = 4
            }
            "paid" -> {
                bean.statusCode = 5
                bean.statusName = PAYDONE
            }
        }
        return bean
    }


    fun sumPrice(priceOne: String, priceTwo: String)="0.0".let{
        val bigDecimal1 = BigDecimal(priceOne)
        val bigDecimal2 = BigDecimal(priceTwo)
        bigDecimal1.add(bigDecimal2).toString()
    }

}