package pl.edu.pwr.student.actions_feed.ui.repository_selection

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pl.edu.pwr.student.actions_feed.R

class RepositorySelectionListAdapter(private val repositoryList: MutableList<String>) :
    RecyclerView.Adapter<RepositorySelectionListAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.repository_selection_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = repositoryList[position]
    }

    override fun getItemCount(): Int {
        return repositoryList.size
    }

}
