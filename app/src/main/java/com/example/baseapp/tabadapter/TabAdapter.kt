package com.example.baseapp.tabadapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class TabAdapter (supportFragmentManager: FragmentManager): FragmentPagerAdapter(supportFragmentManager,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) {
    private val mfragmentlist=ArrayList<Fragment>()
    private val mfragmentTitlelist=ArrayList<String>()


    override fun getCount(): Int {

        return mfragmentlist.size
    }

    override fun getItem(position: Int): Fragment {
        return mfragmentlist[position]

    }


    override fun getPageTitle(position: Int): CharSequence? {
        return mfragmentTitlelist[position]
    }

    fun addFragment(fragment: Fragment, title:String){
        mfragmentlist.add(fragment)
        mfragmentTitlelist.add(title)
    }
}
