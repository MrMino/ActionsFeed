package pl.edu.pwr.student.actions_feed.ui.action_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pl.edu.pwr.student.actions_feed.R
import pl.edu.pwr.student.actions_feed.dto.GithubListWorkflows

class ActionListAdapter(private val actionsList: MutableList<GithubListWorkflows.WorkflowItem>) :
    RecyclerView.Adapter<ActionListAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val commitMessage: TextView = view.findViewById(R.id.commitMessageTextView)
        val commitHash: TextView = view.findViewById(R.id.commitHashTextView)
        val committerName: TextView = view.findViewById(R.id.committerNameTextView)
        val workflowName: TextView = view.findViewById(R.id.workflowNameView)
        val branchName: TextView = view.findViewById(R.id.branchNameTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.action_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // TODO Populate committerName in the UI
        // TODO Populate commitMessage in the UI
        // TODO Populate branchName in the UI
        // TODO Set different icon in the UI based on the workflow status
        holder.workflowName.text = actionsList[position].name
        holder.commitHash.text = actionsList[position].headSha
    }

    override fun getItemCount(): Int {
        return actionsList.size
    }

}
