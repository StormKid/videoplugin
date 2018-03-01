package com.stormkid.videoplugin.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

/**
 * Created by ke_li on 2018/1/2.
 */
class CirclePageAdapter constructor(fragmentManager: FragmentManager, private val fragmentList: MutableList<Fragment>) : FragmentPagerAdapter(fragmentManager) {


    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }
}