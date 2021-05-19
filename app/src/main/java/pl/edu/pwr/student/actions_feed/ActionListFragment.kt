package pl.edu.pwr.student.actions_feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import pl.edu.pwr.student.actions_feed.databinding.FragmentActionListBinding

class ActionListFragment : Fragment() {
    private lateinit var actionListAdapter: ActionListAdapter
    private lateinit var binding: FragmentActionListBinding
    private val actionsViewModel: ActionsViewModel by activityViewModels()
    private val actionsList: MutableList<String> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentActionListBinding.inflate(inflater, container, false)

        binding.actionsList.layoutManager = LinearLayoutManager(context)
        actionListAdapter = ActionListAdapter(actionsList)
        binding.actionsList.adapter = actionListAdapter

        actionsViewModel.actionData.observe(viewLifecycleOwner, {
            actionsList.clear()
            actionsList.addAll(
                it.groupBy { workflowItem -> workflowItem.workflowId }.values.map { vec -> vec.maxByOrNull { item -> item.runNumber }!! }
                    .sortedBy { workflowItem -> workflowItem.status }
                    .map { workflowItem -> "$workflowItem" }
            )
            actionListAdapter.notifyDataSetChanged()
        })

        return binding.root
    }

}
