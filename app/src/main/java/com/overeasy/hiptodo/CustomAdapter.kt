package com.overeasy.hiptodo

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class CustomAdapter {
    companion object {
        @BindingAdapter("recyclerViewAdapter")
        @JvmStatic
        fun recyclerViewAdapter(recyclerView: RecyclerView, adapter: RecyclerView.Adapter<*>?) {
            recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
            // recyclerView.addItemDecoration(RecyclerViewDecoration(20))
            recyclerView.adapter = adapter
        }
    }
}