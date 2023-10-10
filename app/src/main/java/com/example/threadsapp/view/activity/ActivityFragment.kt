package com.example.threadsapp.view.activity

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.neobis_android_chapter8.viewModels.AuthViewModel.UserInfoViewModel
import com.example.threadsapp.R
import com.example.threadsapp.adapters.ActivityAdapter
import com.example.threadsapp.databinding.FragmentActivityBinding

class ActivityFragment : Fragment() {
    private lateinit var binding: FragmentActivityBinding
    private lateinit var viewPager: ViewPager2
    private lateinit var buttons: Array<AppCompatButton>
    private val userInfoViewModel: UserInfoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentActivityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setupViewPager()
        setupButtons()
    }

    private fun setupViews() {
        viewPager = binding.viewPager
        buttons = arrayOf(
            binding.btnActivityComments,
            binding.btnActivityFollowing,
            binding.btnActivityRequests
        )
    }

    private fun setupViewPager() {
        val adapter = ActivityAdapter(parentFragmentManager, lifecycle)
        userInfoViewModel.getInfo()
        userInfoViewModel.profileData.observe(viewLifecycleOwner) { profile ->
            profile?.let {
                if (profile.is_private) {
                    adapter.setFragments(arrayOf(CommentsFragment(), ActivityRequestsFragment()))
                    binding.btnActivityFollowing.visibility = View.GONE
                } else {
                    adapter.setFragments(arrayOf(CommentsFragment(), ActivityFollowingFragment()))
                    binding.btnActivityRequests.visibility = View.GONE
                }
                viewPager.adapter = adapter
                viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        setActiveButton(position)
                    }
                })
            }
        }
    }

    private fun setupButtons() {
        for (i in buttons.indices) {
            buttons[i].setOnClickListener { setActiveButton(i) }
        }
        setActiveButton(0)
    }

    private fun setActiveButton(activeIndex: Int) {
        for (i in buttons.indices) {
            val button = buttons[i]
            val isSelected = (i == activeIndex)
            val backgroundRes = if (isSelected) R.drawable.activity_btn else R.drawable.rounded_rectangle
            val textColor = if (isSelected) Color.WHITE else Color.BLACK

            button.setBackgroundResource(backgroundRes)
            button.setTextColor(textColor)
        }
        viewPager.setCurrentItem(activeIndex, true)
    }
}
