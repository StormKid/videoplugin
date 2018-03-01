package com.moudle.basetool.net

import com.lzy.okgo.model.Progress
import java.io.File

/**
 * Created by ke_li on 2017/12/18.
 */
interface MyNormalNetCallback{
    fun success(any:String)
    fun err(msg:String)
}

interface MyFileNetCallback{
    fun success(file:File)
    fun progress(process: Progress)
    fun err(msg:String)
}