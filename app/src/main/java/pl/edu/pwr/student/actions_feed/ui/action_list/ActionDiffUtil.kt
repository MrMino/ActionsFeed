package pl.edu.pwr.student.actions_feed.ui.action_list

import androidx.recyclerview.widget.DiffUtil
import pl.edu.pwr.student.actions_feed.dto.GithubListWorkflows

class ActionDiffUtil (
    private val oldList: MutableList<GithubListWorkflows.WorkflowItem>,
    private val newList: MutableList<GithubListWorkflows.WorkflowItem>
) : DiffUtil.Callback() {

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