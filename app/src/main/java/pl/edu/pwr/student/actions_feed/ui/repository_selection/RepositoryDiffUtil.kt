package pl.edu.pwr.student.actions_feed.ui.repository_selection

import androidx.recyclerview.widget.DiffUtil

class RepositoryDiffUtil(private val oldList:MutableList<String>,
                     private val newList:MutableList<String>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItemPosition == newItemPosition
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}