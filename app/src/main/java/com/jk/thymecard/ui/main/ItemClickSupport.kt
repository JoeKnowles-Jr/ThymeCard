package com.jk.thymecard.ui.main

import android.view.View
import android.view.View.OnLongClickListener
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnChildAttachStateChangeListener
import com.jk.thymecard.R

class ItemClickSupport private constructor(private val recyclerView: RecyclerView) {
    private var onItemClickListener: OnItemClickListener? = null
    private var onItemLongClickListener: OnItemLongClickListener? = null
    private val onClickListener =
        View.OnClickListener { view ->
            if (onItemClickListener != null) {
                val holder = recyclerView.getChildViewHolder(view)
                onItemClickListener!!.onItemClicked(recyclerView, holder.adapterPosition, view)
            }
        }
    private val onLongClickListener =
        OnLongClickListener { view ->
            if (onItemLongClickListener != null) {
                val holder = recyclerView.getChildViewHolder(view)
                return@OnLongClickListener onItemLongClickListener!!.onItemLongClicked(
                    recyclerView,
                    holder.adapterPosition,
                    view
                )
            }
            false
        }

    init {
        recyclerView.setTag(R.id.item_click_support, this)
        val attachListener: OnChildAttachStateChangeListener =
            object : OnChildAttachStateChangeListener {
                override fun onChildViewAttachedToWindow(view: View) {
                    if (onItemClickListener != null) {
                        view.setOnClickListener(onClickListener)
                    }
                    if (onItemLongClickListener != null) {
                        view.setOnLongClickListener(onLongClickListener)
                    }
                }

                override fun onChildViewDetachedFromWindow(view: View) {}
            }
        recyclerView.addOnChildAttachStateChangeListener(attachListener)
    }

    //    public static ItemClickSupport removeFrom(@NonNull RecyclerView view) {
    //        ItemClickSupport support = (ItemClickSupport) view.getTag(R.id.item_click_support);
    //        if (support != null) {
    //            support.detach(view);
    //        }
    //        return support;
    //    }
    fun setOnItemClickListener(listener: OnItemClickListener): ItemClickSupport {
        onItemClickListener = listener
        return this
    }

    fun setOnItemLongClickListener(listener: OnItemLongClickListener?) {
        onItemLongClickListener = listener
        //        return this;
    }

    //    private void detach(@NonNull RecyclerView view) {
    //        view.removeOnChildAttachStateChangeListener(attachListener);
    //        view.setTag(R.id.item_click_support, null);
    //    }
    interface OnItemClickListener {
        fun onItemClicked(recyclerView: RecyclerView?, position: Int, view: View?)
    }

    interface OnItemLongClickListener {
        fun onItemLongClicked(recyclerView: RecyclerView?, position: Int, view: View?): Boolean
    }

    companion object {
        fun addTo(view: RecyclerView): ItemClickSupport {
            var support = view.getTag(R.id.item_click_support) as ItemClickSupport
            if (support == null) {
                support = ItemClickSupport(view)
            }
            return support
        }
    }
}
