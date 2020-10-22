package com.overeasy.hiptodo

import android.widget.CalendarView
import android.widget.CalendarView.OnDateChangeListener
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class CustomAdapter {
    companion object {
        @BindingAdapter("recyclerViewAdapter")
        @JvmStatic
        fun recyclerViewAdapter(recyclerView: RecyclerView, adapter: RecyclerView.Adapter<*>?) {
            recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
            recyclerView.addItemDecoration(RecyclerViewDecoration(20))
            recyclerView.adapter = adapter
        }
    }
}