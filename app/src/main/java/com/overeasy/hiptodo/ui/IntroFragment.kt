package com.overeasy.hiptodo.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.overeasy.hiptodo.R
import com.overeasy.hiptodo.databinding.FragmentIntroBinding


class IntroFragment(
    private val title: String,
    private val description: String,
    private val image: Int?,
    private val backgroundColor: Int,
    private val descriptionColor: String) : Fragment() {
    private lateinit var binding: FragmentIntroBinding
    val backgroundColorSave = backgroundColor

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_intro, container, false)

        binding.apply {
            title = this@IntroFragment.title
            Glide.with(this@IntroFragment).load(image).into(imageView)
            description = this@IntroFragment.description
            constraintLayout.setBackgroundColor(resources.getColor(backgroundColor))

            if (descriptionColor != "")
                textView3.setTextColor(Color.parseColor(descriptionColor))
        }
        return binding.root
    }
}