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
import androidx.core.content.ContextCompat.startActivity

import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager


class JobsListAdapter (
    private val jobsList: MutableList<GithubListWorkflowJobs.JobItem>,
    private val context: Context,
) : RecyclerView.Adapter<JobsListAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val jobNameTextView = view.findViewById<TextView>(R.id.jobName)
        val successIcon  = view.findViewById<ImageView>(R.id.jobStatusIconSuccess)
        val failureIcon = view.findViewById<ImageView>(R.id.jobStatusIconFailure)
        val logsButton = view.findViewById<Button>(R.id.logsButton)
        val stepList = view.findViewById<RecyclerView>(R.id.stepList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.jobs_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val job = jobsList[position]
        holder.jobNameTextView.text = job.name

        when (job.conclusion) {
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

        holder.logsButton.setOnClickListener(){
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(job.html_url))
            startActivity(context, browserIntent, null)
        }

        holder.stepList.layoutManager = LinearLayoutManager(context)
        val stepListAdapter = StepListAdapter(job.steps, context)
        holder.stepList.adapter = stepListAdapter

        holder.itemView.setOnClickListener(){
        }
    }

    override fun getItemCount(): Int {
        return jobsList.size
    }

}