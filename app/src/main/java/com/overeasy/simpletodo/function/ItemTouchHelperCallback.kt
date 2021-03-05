package com.overeasy.simpletodo.function

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.overeasy.simpletodo.view.MainAdapter

class ItemTouchHelperCallback(private val mAdapter: MainAdapter) : ItemTouchHelper.Callback() {
    private var isMoved = false

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        val flagDrags: Int = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        return makeMovementFlags(flagDrags, 0)
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        mAdapter.onItemMove(viewHolder.adapterPosition, target.adapterPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

    }


    override fun isLongPressDragEnabled(): Boolean {
        return true
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return false
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)

        if (isMoved) {
            isMoved = false
            mAdapter.changeMoveEvent()
            // Function executed when drag and drop is finished
            // 드래그 앤 드랍이 끝나면 실행된다
        }
    }

    override fun onMoved(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        fromPos: Int,
        target: RecyclerView.ViewHolder,
        toPos: Int,
        x: Int,
        y: Int
    ) {
        super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y)

        isMoved = true
    }

    // Executed when you drop during drag and drop, that is, when you release your hand.
    // The adapter's notifyDataSetChanged() is executed when the hand is off from screen.
    // 드래그 앤 드롭 중 드롭, 즉 손을 뗄 때 실행된다. 손을 땠을 때 어댑터의 notifyDataSetChanged() 실행.
    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)

        mAdapter.notifyDataSetChanged()
    }
}