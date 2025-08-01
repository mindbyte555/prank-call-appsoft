package com.example.baseapp.onbording

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class pagerAdapter (fm: FragmentManager): FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return 3
    }

    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> {
                return bording_screen1()
            }

            1 -> {
                return bording_screen2()
            }

            2 -> {
                return bording_screen3()
            }
            else -> {
                return bording_screen1()
            }
        }
    }
}