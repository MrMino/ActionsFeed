package pl.edu.pwr.student.actions_feed.ui.repository_selection

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import pl.edu.pwr.student.actions_feed.R

class RepositorySelectionListAdapter(private val repositoryList: MutableList<String>) :
    RecyclerView.Adapter<RepositorySelectionListAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.textView)

        //For LongClick Deletion
        //val holderView = view
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.repository_selection_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = repositoryList[position]

        //Deletion On Long Click
        /*
        holder.holderView.setOnLongClickListener {
            val builder = AlertDialog.Builder(holder.holderView.context)
            builder.setTitle("Are you sure!?")
            builder.setMessage("Do you want to delete this item?")
            builder.setPositiveButton("Yes (Not yet)") { _: DialogInterface, _: Int ->
                //deleteItem(position)
            }
            builder.setNegativeButton("No") { _: DialogInterface, _: Int -> }
            builder.show()

            return@setOnLongClickListener true
        }*/
    }

    fun deleteItem(path: String) {
        val tmpList: MutableList<String> = mutableListOf()
        for (i in 0 until repositoryList.size)
            if (repositoryList[i] != path)
                tmpList.add(repositoryList[i])
        val diffUtilCallback = RepositoryDiffUtil(repositoryList, tmpList)
        val diffResult = DiffUtil.calculateDiff(diffUtilCallback)

        repositoryList.clear()
        repositoryList.addAll(tmpList)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun getItemCount(): Int {
        return repositoryList.size
    }

}
