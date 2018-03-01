package com.moudle.basetool.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

/**
 * Created by ke_li on 2017/12/26.
 */
class ImageUtil {
    private var url: Any

    private var resId: Int

    private var iv: ImageView

    private var context: Context

    private var corner : Int

    private var margin :Int

    private var cornerType : RoundedCornersTransformation.CornerType

    fun setUrl(url: Any): ImageUtil {
        this.url = url
        return this
    }

    constructor(context: Context) {
        this.context = context
        this.resId = 0
        this.url = ""
        this.iv = ImageView(context)
        this.corner = 10
        this.margin = 0
        this.cornerType = RoundedCornersTransformation.CornerType.ALL
    }

    fun setErr(id: Int): ImageUtil {
        resId = id
        return this
    }

    fun withView(imageView: ImageView): ImageUtil {
        iv = imageView
        return this
    }

    fun setMargin(margin:Int): ImageUtil {
        this.margin = margin
        return this
    }

    fun setCorner(corner:Int): ImageUtil {
        this.corner = corner
        return this
    }

    /**
     * 获得原始图片
     */
    fun getNormalImage() {
        val err = RequestOptions().error(resId)
        Glide.with(context).load(url).apply(err).into(iv)
    }

    /**
     * 获得圆形图片
     */
    fun getCircleImage() {
        val circleCrop = RequestOptions.errorOf(resId).transform(CircleCrop())
        Glide.with(context).load(url).apply(circleCrop).into(iv)
    }

    /**
     * 获得带圆角的图片
     */
    fun getCornerImage(){
        val conner = RoundedCornersTransformation(corner,margin,cornerType )
        val result = RequestOptions.errorOf(resId).transform(conner)
        Glide.with(context).load(url).apply(result).into(iv)
    }


}