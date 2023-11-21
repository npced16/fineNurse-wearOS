package com.example.myapplication_test.presentation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.myapplication_test.presentation.*


class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int{
        return 4
    }

    override fun createFragment(position: Int): Fragment{
        return when (position) {
            0 -> PainFragment()
            1 -> ChangeFragment()
            2 -> ClothesFragment()
            else -> HelpFragment()
        }
    }

}