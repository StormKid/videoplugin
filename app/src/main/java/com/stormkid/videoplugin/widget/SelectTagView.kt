package com.stormkid.videoplugin.widget

import android.content.Context
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.moudle.zxing.utils.DimenUtil
import com.stormkid.videoplugin.CateGroyBean
import com.stormkid.videoplugin.R

/**
 * Created by ke_li on 2018/2/28.
 */
class SelectTagView : ViewGroup {

    private var textSize = 0 // tag字体大小

    private var textColor = 0 // tag字体颜色

    private var backGroundColor = 0 // tag背景色

    private var backGroundResource = 0 // tag背景布局

    private var type = NORMAL_CLOUD_TYPE

    companion object {
        const val NORMAL_CLOUD_TYPE = "NORMAL_CLOUD_TYPE" // 普通的操作方式
        const val SINGLE_SELECT_TYPE = "SINGLE_SELECT_TYPE" // 单选
        const val MORE_SELECT_TYPE = "MORE_SELECT_TYPE" // 多选
    }

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    /**
     * init对象
     */
    constructor(context: Context, attrs: AttributeSet?, def: Int) : super(context, attrs, def) {
        val attr = context.obtainStyledAttributes(attrs!!, R.styleable.SelectTagView, def, 0)
        textSize = attr.getInt(R.styleable.SelectTagView_tagTextSize, 8)
        textColor = attr.getResourceId(R.styleable.SelectTagView_tagTextColor, R.color.text_666)
        backGroundColor = attr.getResourceId(R.styleable.SelectTagView_tagBgColor, R.color.f8)
        backGroundResource = attr.getResourceId(R.styleable.SelectTagView_tagBgColor, 0)
        val xmlType= attr.getInt(R.styleable.SelectTagView_tagSelectType, 0)
        when (xmlType){
            0-> type= NORMAL_CLOUD_TYPE
            1-> type = SINGLE_SELECT_TYPE
            2-> type = MORE_SELECT_TYPE
        }
    }

    /**
     * 使用系统自带margin params
     */
    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

    /**
     * 更新type是否为多选或单选或者只点击
     */
    fun setType(type:String){
        this.type = type
    }

    /**
     * 控制子view所在的位置
     */
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        // 初始化子控件位置
        var pointWidth = 0
        var pointHeight = 0

        (0 until childCount)
                .asSequence()
                .map { getChildAt(it) }
                .filter { it.visibility != View.GONE }
                .forEach {
                    val childHeight = it.measuredHeight
                    val childWidth = it.measuredWidth
                    val layoutParams = it.layoutParams as MarginLayoutParams
                    if (pointWidth + childWidth + layoutParams.leftMargin + layoutParams.rightMargin > width) {
                        pointHeight += childHeight + layoutParams.topMargin + layoutParams.bottomMargin
                        pointWidth = 0
                    }
                    val top = layoutParams.topMargin + pointHeight
                    val bottom = layoutParams.bottomMargin + pointHeight + childHeight
                    val left = layoutParams.leftMargin + pointWidth
                    val right = layoutParams.rightMargin + pointWidth + childWidth
                    it.layout(left, top, right, bottom)
                    val tag = it.tag as CateGroyBean
                    if (tag.isChoose) it.setBackgroundResource(R.drawable.shape_selected)
                    else it.setBackgroundResource(R.drawable.shape_no_select)
                    //记录最终view的位置
                    pointWidth += layoutParams.leftMargin + childWidth + layoutParams.rightMargin
                }

    }


    /**
     * 计算子控件大小进行自动换行处理
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val sizeWidth = MeasureSpec.getSize(widthMeasureSpec)
        val sizeHeight = MeasureSpec.getSize(heightMeasureSpec)
        val modeWidth = MeasureSpec.getMode(widthMeasureSpec)
        val modeHeight = MeasureSpec.getMode(heightMeasureSpec)

        //初始化父控件大小
        var resultWidth = 0
        var resultHeight = 0

        // 初始化行控件大小
        var itemWidth = 0
        var itemHeight = 0

        for (i in 0 until childCount) { // 遍历 所有的子元素
            val child = getChildAt(i)
            val layoutParams = child.layoutParams as MarginLayoutParams
            measureChild(child, widthMeasureSpec, heightMeasureSpec) // 先测量

            val childWidth = child.measuredWidth
            val childHeight = child.measuredHeight

            val realWidth = childWidth + layoutParams.leftMargin + layoutParams.rightMargin
            val realHeight = childHeight + layoutParams.topMargin + layoutParams.bottomMargin

            if (sizeWidth < (itemWidth + realWidth)) {//换行
                resultWidth = Math.max(realWidth, itemWidth)
                resultHeight += realHeight
                itemHeight = realHeight
                itemWidth = realWidth
            } else { // 添加
                itemWidth += realWidth
                itemHeight = Math.max(realHeight, itemHeight)
            }

            // 不换行
            if (i == childCount - 1) {
                resultWidth = Math.max(realWidth, itemWidth)
                resultHeight += itemHeight
            }

            setMeasuredDimension(if (modeWidth == View.MeasureSpec.EXACTLY) sizeWidth else resultWidth,
                    if (modeHeight == View.MeasureSpec.EXACTLY) sizeHeight else resultHeight)
        }
    }


    fun initChild(list: MutableList<CateGroyBean>,callbcak:(CateGroyBean)->Unit) = list.apply { removeAllViews() }
            .forEachIndexed { position, categoryBean ->
                val textView = TextView(context)
                textView.setTextColor(ContextCompat.getColor(context, textColor))
                DimenUtil.getInstance(context).getSmallSize(textView)
                textView.setBackgroundColor(ContextCompat.getColor(context, backGroundColor))
                textView.tag = categoryBean
                val layoutParams = MarginLayoutParams(ViewGroup.MarginLayoutParams.WRAP_CONTENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT)
                val margin = DimenUtil.getInstance(context).getWindowWidth() / 45
                layoutParams.rightMargin = margin
                layoutParams.leftMargin = margin
                layoutParams.bottomMargin = margin
                layoutParams.topMargin = margin
                textView.layoutParams = layoutParams
                textView.text = categoryBean.name
                if (backGroundResource != 0) textView.setBackgroundResource(backGroundResource)
                textView.setPadding(margin * 2, margin, margin * 2, margin)
                textView.setOnClickListener {
                    when(type){
                        SINGLE_SELECT_TYPE ->{
                            categoryBean.isChoose = !categoryBean.isChoose
                            it.tag = categoryBean
                            list.forEachIndexed { index, categaryBean ->
                                if (position!=index){
                                    categaryBean.isChoose = false
                                    getChildAt(index).tag = categaryBean
                                }
                            }
                            requestLayout()
                        }
                        MORE_SELECT_TYPE ->{
                            categoryBean.isChoose = !categoryBean.isChoose
                            it.tag = categoryBean
                            requestLayout()
                        }
                        NORMAL_CLOUD_TYPE ->{
                            categoryBean.isChoose = true
                            it.tag = categoryBean
                            list.forEachIndexed { index, categaryBean ->
                                if (position!=index){
                                    categaryBean.isChoose = false
                                    getChildAt(index).tag = categaryBean
                                }
                            }
                            requestLayout()
                        }

                    }

                    if (categoryBean.isChoose) callbcak.invoke(categoryBean)
                }
                addView(textView)
            }


}