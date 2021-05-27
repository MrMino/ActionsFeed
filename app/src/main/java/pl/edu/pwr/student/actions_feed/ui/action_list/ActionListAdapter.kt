package pl.edu.pwr.student.actions_feed.ui.action_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pl.edu.pwr.student.actions_feed.R

class ActionListAdapter(private val actionsList: MutableList<String>) :
    RecyclerView.Adapter<ActionListAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.action_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = actionsList[position]
    }

    override fun getItemCount(): Int {
        return actionsList.size
    }

}
