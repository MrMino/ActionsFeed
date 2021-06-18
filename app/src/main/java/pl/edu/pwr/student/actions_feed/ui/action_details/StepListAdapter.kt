package pl.edu.pwr.student.actions_feed.ui.action_details

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pl.edu.pwr.student.actions_feed.R
import pl.edu.pwr.student.actions_feed.dto.GithubListWorkflowJobs

class StepListAdapter(
    private val stepList: List<GithubListWorkflowJobs.StepItem>,
    private val context: Context)
: RecyclerView.Adapter<StepListAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val stepNameTextView = view.findViewById<TextView>(R.id.stepName)
        val successIcon = view.findViewById<ImageView>(R.id.stepStatusIconSuccess)
        val failureIcon = view.findViewById<ImageView>(R.id.stepStatusIconFailure)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return StepListAdapter.ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.step_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val step = stepList[position]
        holder.stepNameTextView.text = step.name
        when (step.conclusion) {
            "success" -> {
                holder.successIcon.visibility = ImageView.VISIBLE
                holder.failureIcon.visibility = ImageView.INVISIBLE
            }
            "failure" -> {
                holder.successIcon.visibility = ImageView.INVISIBLE
                holder.failureIcon.visibility = ImageView.VISIBLE
            }
            else -> {
                holder.successIcon.visibility = ImageView.INVISIBLE
                holder.failureIcon.visibility = ImageView.INVISIBLE
            }
        }
    }

    override fun getItemCount(): Int {
        return stepList.size
    }
}

