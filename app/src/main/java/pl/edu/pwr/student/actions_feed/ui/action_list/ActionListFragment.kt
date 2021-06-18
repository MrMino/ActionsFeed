package pl.edu.pwr.student.actions_feed.ui.action_list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import pl.edu.pwr.student.actions_feed.ActionsViewModel
import pl.edu.pwr.student.actions_feed.databinding.FragmentActionListBinding
import pl.edu.pwr.student.actions_feed.dto.GithubListWorkflows

class ActionListFragment : Fragment() {
    private lateinit var actionListAdapter: ActionListAdapter
    private lateinit var binding: FragmentActionListBinding
    private val actionsViewModel: ActionsViewModel by activityViewModels()
    private val actionsList: MutableList<GithubListWorkflows.WorkflowItem> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentActionListBinding.inflate(inflater, container, false)

        binding.actionsList.layoutManager = LinearLayoutManager(context)
        actionListAdapter = ActionListAdapter(actionsList, findNavController())
        binding.actionsList.adapter = actionListAdapter

        actionsViewModel.actionData.observe(viewLifecycleOwner, {
    //        actionsList.clear()
            Log.i("tester", "observed")
            val data: MutableList<GithubListWorkflows.WorkflowItem> = mutableListOf()
            for (item in it.values) {
                data.addAll(
                    item.sortedBy { workflowItem -> workflowItem.status }
                )
    /*            actionsList.addAll(
                    item.sortedBy { workflowItem -> workflowItem.status }
                )*/
            }
            actionListAdapter.refreshList(data)
            actionListAdapter.notifyDataSetChanged()
        })

        fun testActionList() {

        }

        return binding.root
    }

}
